package predict.stock.base;

import com.google.inject.Injector;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import predict.stock.config.spConfig;
import predict.stock.log.SPLog;
import predict.stock.obj.GlobalCfg;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Optional;



public abstract class SPTask implements ISPLogable {
    protected Vertx mVertx;
    private final String mTaskName;
    private SPData taskInput;
    private Marker mMarker;

    private final SPLog mLogger;

    private final Injector injector;

    private final spConfig spConfig;

    public SPTask(ISPLogable logable, String taskName) {
        //each task instance will be belonged to only one process
        mVertx = logable.getVertx();
        mTaskName = taskName;
        mMarker = MarkerManager.getMarker(mTaskName);
        injector = logable.getInjector();
        mLogger = logable.getLogger();
        injector.injectMembers(this);
        spConfig = injector.getInstance(spConfig.class);
    }

    public void run(SPData input, Handler<SPData> whenDone) {
        this.taskInput = input;
        exec(input, spData -> {
            if (whenDone != null) {
                whenDone.handle(spData);
            }
        });
    }

    /**
     * Run task without callback
     *
     * @param input task's input
     */
    public void run(SPData input) {
        run(input, null);
    }


    protected abstract void exec(SPData input, Handler<SPData> whenDone);

    @Override
    public Vertx getVertx() {
        return mVertx;
    }

    @Override
    public SPLog getLogger() {
        return mLogger;
    }

    public String getResultKey() {
        return mTaskName;
    }

    private SPData getInput(SPData input) {
        return input != null ? input : taskInput;
    }

    @Override
    public Injector getInjector() {
        return injector;
    }

    protected void sendEventBus(String mTaskName, SPData input, Handler<AsyncResult<Message<SPData>>> handler){
        var timeout = Optional.ofNullable(spConfig.getVerticlesByName(mTaskName))
                .filter(globalCfg -> "EVENT_BUS_SENT_TIMEOUT".equalsIgnoreCase(globalCfg.getSubKey()))
                .map(GlobalCfg::getContent).map(Integer::parseInt).orElse(120000);
        getVertx().eventBus().request(mTaskName, input, new DeliveryOptions().setSendTimeout(timeout), handler);
    }
}


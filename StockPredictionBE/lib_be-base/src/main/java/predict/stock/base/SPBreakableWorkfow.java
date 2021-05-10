package predict.stock.base;

import io.vertx.core.Handler;

import java.util.List;

public class SPBreakableWorkfow extends SPWorkflow {

    public static final String BREAK_FLAG_KEY = "spBreakableWorkfow_BREAK_FLAG_KEY";

    public SPBreakableWorkfow(ISPLogable logable, String taskName) {
        super(logable, taskName);
    }

    public SPBreakableWorkfow(ISPLogable logable, String taskName, boolean stopWhenFail) {
        super(logable, taskName, stopWhenFail);
    }

    @Override
    protected void serialRun(List<SPTask> taskList, SPData input, Handler<SPData> whenAllDone) {
        runOneTask(taskList, input, whenAllDone, 0);
    }

    @Override
    public void exec(SPData input, Handler<SPData> whenDone) {
        serialRun(mWorkerList, input, whenDone);
    }

    protected boolean getStopFlag(SPData input) {
        boolean needStop = false;

        final Object stopFlag = input.popExtra(BREAK_FLAG_KEY);
        if(stopFlag != null) needStop = ((Boolean) stopFlag);

        return needStop;
    }

    protected void runOneTask(List<SPTask> taskList, SPData input, Handler<SPData> whenAllDone, final int step) {
        // check if flow need stop
        if (getStopFlag(input)) {
            whenAllDone.handle(input);
            return;
        }

        if (step == taskList.size() || mStopWhenFail && !input.getResult()) {
            //this is the end => do return here
            whenAllDone.handle(input);
        } else {
            SPTask task = taskList.get(step);
            task.run(input, entries -> runOneTask(taskList, entries, whenAllDone, step + 1));
        }
    }

    public static void breakFlow(SPData input, Handler<SPData> whenDone) {
        input.putExtra(SPBreakableWorkfow.BREAK_FLAG_KEY, true);
        whenDone.handle(input);
    }
}

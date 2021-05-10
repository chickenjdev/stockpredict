package predict.stock.base;

import io.vertx.core.Handler;

import java.util.List;


public class SPWorkflow extends SPProcess {


    public SPWorkflow(ISPLogable verticle, String workflowName) {
        //workflow is a serial flow
        super(verticle, workflowName);
    }

    public SPWorkflow(ISPLogable verticle, String workflowName, boolean stopWhenFail) {
        //workflow is a serial flow
        super(verticle, workflowName, stopWhenFail);
    }

    protected void serialRun(final List<SPTask> taskList, final SPData input, final Handler<SPData> whenAllDone) {
        runOneTask(taskList, input, whenAllDone, 0);
    }

    @Override
    public void exec(SPData input, Handler<SPData> whenDone) {
        serialRun(mWorkerList, input, whenDone);
    }

    private void runOneTask(List<SPTask> taskList, SPData input, Handler<SPData> whenAllDone, final int step) {
        if (step == taskList.size() || (mStopWhenFail && input != null && !input.getResult())) {
            //this is the end => do return here
            whenAllDone.handle(input);
        } else {
            SPTask task = taskList.get(step);
            task.run(input, entries -> runOneTask(taskList, entries, whenAllDone, step + 1));
        }

    }
}

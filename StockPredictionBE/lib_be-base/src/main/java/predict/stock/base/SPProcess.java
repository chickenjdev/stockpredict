package predict.stock.base;

import java.util.ArrayList;
import java.util.List;

public abstract class SPProcess extends SPTask {

    boolean mStopWhenFail = true; // default

    List<SPTask> mWorkerList = new ArrayList<>();

    SPProcess(ISPLogable logable, String taskName) {
        super(logable, taskName);
    }

    SPProcess(ISPLogable logable, String taskName, boolean stopWhenFail) {
        super(logable, taskName);
        mStopWhenFail = stopWhenFail;
    }


    protected void clearTask() {
        mWorkerList.clear();
    }


    public SPProcess addTask(SPTask task) {
        mWorkerList.add(task);
        return this;
    }

    public SPProcess addTasks(List<SPTask> tasks) {
        mWorkerList.addAll(tasks);
        return this;
    }

    public String getResultKey() {
        return "spProcess";
    }

}

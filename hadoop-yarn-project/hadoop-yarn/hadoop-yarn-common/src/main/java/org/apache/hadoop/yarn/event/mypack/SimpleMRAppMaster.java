package org.apache.hadoop.yarn.event.mypack;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.CompositeService;
import org.apache.hadoop.yarn.event.AsyncDispatcher;
import org.apache.hadoop.yarn.event.Dispatcher;
import org.apache.hadoop.yarn.event.EventHandler;

public class SimpleMRAppMaster extends CompositeService {

    private final String jobId;
    private final int taskNumber;
    private final String[] taskIds;
    private AsyncDispatcher dispatcher;

    public SimpleMRAppMaster(String name, String jobId, int taskNumber) {
        super(name);
        this.jobId = jobId;
        this.taskNumber = taskNumber;
        this.taskIds = new String[taskNumber];
        for (int i = 0; i < taskNumber; i++) {
            taskIds[i] = new String(jobId + "_task_" + i);
        }
    }

    @Override
    protected void serviceInit(Configuration conf) throws Exception {
        this.dispatcher = new AsyncDispatcher();
        // 注册待处理的事件和与之对应的处理器.
        dispatcher.register(TaskEventType.class, new TaskEventDispatcher());
        dispatcher.register(JobEventType.class, new JobEventDispatcher());
        addService(dispatcher);
        super.serviceInit(conf);
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    private class JobEventDispatcher implements EventHandler<JobEvent> {

        @Override
        public void handle(JobEvent event) {
            if (event.getType() == JobEventType.JOB_KILL) {
                System.out.println("Kill all the task.");
                for (int i = 0; i < taskNumber; i++) {
                    dispatcher.getEventHandler().handle(new TaskEvent(taskIds[i], TaskEventType.T_KILL));
                }
            } else if (event.getType() == JobEventType.JOB_INIT) {
                System.out.println("init job");
                for (int i = 0; i < taskNumber; i++) {
                    dispatcher.getEventHandler().handle(new TaskEvent(taskIds[i], TaskEventType.T_SCHEDULER));
                }
            }
        }
    }

    private class TaskEventDispatcher implements EventHandler<TaskEvent> {

        @Override
        public void handle(TaskEvent event) {
            switch (event.getType()) {
                case T_KILL:
                    System.out.println("KILL: " + event.getTaskId());
                    break;
                case T_SCHEDULER:
                    System.out.println("SCHEDULER: " + event.getTaskId());
                    break;
                default:
                    break;
            }
        }
    }
}

package org.apache.hadoop.yarn.event.mypack;

import org.apache.hadoop.yarn.event.AbstractEvent;

public class TaskEvent extends AbstractEvent<TaskEventType> {

    private static String taskId;

    public TaskEvent(String taskId, TaskEventType taskEventType) {
        super(taskEventType);
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}

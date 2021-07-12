package org.apache.hadoop.yarn.state.mystat;

import org.apache.hadoop.yarn.event.Event;
import org.apache.hadoop.yarn.event.EventHandler;
import org.apache.hadoop.yarn.event.mypack.JobEvent;
import org.apache.hadoop.yarn.event.mypack.JobEventType;
import org.apache.hadoop.yarn.state.StateMachine;

/**
 * @author wuv1up@163.com
 * @date 2020-09-23 21:43
 */
public class TestJobState {

    public static void main(String[] args) {
        MyJobStateMachine machine = new MyJobStateMachine("job-xx-1", new EventHandler() {
            @Override
            public void handle(Event event) {
                System.out.println(event);
            }
        });
        System.out.println("hello world");
        System.out.println(machine.getInternalState());
        machine.handle(new JobEvent("job-xx-1", JobEventType.JOB_COMPLETE));
    }

}

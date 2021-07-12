package org.apache.hadoop.yarn.state.mystat;

import org.apache.hadoop.yarn.state.MultipleArcTransition;
import org.apache.hadoop.yarn.state.SingleArcTransition;
import org.apache.hadoop.yarn.state.StateMachine;
import org.apache.hadoop.yarn.state.StateMachineFactory;

import java.util.EnumSet;
import java.util.Set;

public class TestMachine {

    private final String name;

    public TestMachine(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        StateMachineFactory<TestMachine, TaskStatus, TaskEventType, TaskEvent> machineFactory = new StateMachineFactory<>(TaskStatus.NEW);
        Set<TaskStatus> finalStatus = EnumSet.of(TaskStatus.FAILED, TaskStatus.FINAL);
        machineFactory = machineFactory
                .addTransition(TaskStatus.NEW, TaskStatus.INIT, TaskEventType.SETUP, new SingleArcTransition<TestMachine, TaskEvent>() {
                    @Override
                    public void transition(TestMachine testMachine, TaskEvent taskEvent) {
                        System.out.println(testMachine.getName() + "  : event: " + taskEvent);
                    }
                }).addTransition(TaskStatus.INIT, finalStatus, TaskEventType.START, new MultipleArcTransition<TestMachine, TaskEvent, TaskStatus>() {
                    @Override
                    public TaskStatus transition(TestMachine testMachine, TaskEvent taskEvent) {
                        System.out.println(taskEvent);
                        return TaskStatus.FINAL;
                    }
                });
        StateMachine<TaskStatus, TaskEventType, TaskEvent> machine = machineFactory.installTopology().make(new TestMachine("Hello Machine"));
        TaskEvent taskEvent = new TaskEvent(TaskEventType.SETUP);
        System.out.println(machine.getCurrentState());
        machine.doTransition(taskEvent.getTaskEventType(), taskEvent);
        System.out.println(machine.getCurrentState());
        TaskEvent startEvent = new TaskEvent(TaskEventType.START);
        machine.doTransition(startEvent.getTaskEventType(), startEvent);
        System.out.println(machine.getCurrentState());
    }

    enum TaskStatus {
        NEW,
        INIT,
        READY,
        RUNNING,
        SUCCESS,
        FAILED,
        FINAL;
    }

    enum TaskEventType {
        SETUP,
        START,
        STOP;
    }

    static class TaskEvent {

        private TaskEventType taskEventType;

        public TaskEvent(TaskEventType eventType) {
            this.taskEventType = eventType;
        }

        public TaskEventType getTaskEventType() {
            return this.taskEventType;
        }

        @Override
        public String toString() {
            return "TaskEvent{" +
                    "taskEventType=" + taskEventType +
                    '}';
        }
    }

    public String getName() {
        return name;
    }

}

package org.apache.hadoop.yarn.state.mystat;

import org.apache.hadoop.yarn.event.EventHandler;
import org.apache.hadoop.yarn.event.mypack.JobEvent;
import org.apache.hadoop.yarn.event.mypack.JobEventType;
import org.apache.hadoop.yarn.state.InvalidStateTransitonException;
import org.apache.hadoop.yarn.state.SingleArcTransition;
import org.apache.hadoop.yarn.state.StateMachine;
import org.apache.hadoop.yarn.state.StateMachineFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wuv1up@163.com
 * @date 2020-09-21 22:39
 */
public class MyJobStateMachine<E extends Enum<E>>
        implements EventHandler<JobEvent>
{

    protected static final StateMachineFactory<MyJobStateMachine, JobStateInternal, JobEventType, JobEvent> stateMachineFactory =
            new StateMachineFactory<MyJobStateMachine, JobStateInternal, JobEventType, JobEvent>(JobStateInternal.NEW)
                    .addTransition(JobStateInternal.NEW, JobStateInternal.INITED, JobEventType.JOB_INIT, new InitTransition())
                    .addTransition(JobStateInternal.INITED, JobStateInternal.SETUP, JobEventType.JOB_START, new StartTransition())
                    .installTopology();

    private final String jobID;
    private final Lock writeLock;
    private final Lock readLock;

    private final StateMachine<JobStateInternal, JobEventType, JobEvent> stateMachine;
    private EventHandler eventHandler;

    public MyJobStateMachine(String jobId, EventHandler eventHandler) {
        this.jobID = jobId;
        this.eventHandler = eventHandler;
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
        stateMachine = stateMachineFactory.make(this);
    }

    @Override
    public void handle(JobEvent event) {
        try {
            writeLock.lock();
            JobStateInternal oldState = getInternalState();
            try {
                getStateMachine().doTransition(event.getType(), event);
            } catch (InvalidStateTransitonException e) {
                System.out.println("can't handle this event at current state");
            }
            if (oldState != getInternalState()) {
                System.out.println("Job Transitioned from " + oldState + " to " + getInternalState());
            }
        } finally {
            writeLock.unlock();
        }
    }

    protected StateMachine<JobStateInternal, JobEventType, JobEvent> getStateMachine() {
        return stateMachine;
    }

    public JobStateInternal getInternalState() {
        readLock.lock();
        try {
            return getStateMachine().getCurrentState();
        } finally {
            readLock.unlock();
        }
    }

    public enum JobStateInternal {
        NEW,
        SETUP,
        INITED,
        RUNNING,
        SUCCESSED,
        KILLED;
    }

    public static class InitTransition implements SingleArcTransition<MyJobStateMachine, JobEvent> {

        @Override
        public void transition(MyJobStateMachine job, JobEvent jobEvent) {
            System.out.println("Receiving event " + jobEvent);
            job.eventHandler.handle(new JobEvent(job.jobID, JobEventType.JOB_START));
        }
    }

    public static class StartTransition implements SingleArcTransition<MyJobStateMachine, JobEvent> {

        @Override
        public void transition(MyJobStateMachine job, JobEvent jobEvent) {
            System.out.println("Receiving event " + jobEvent);
            job.eventHandler.handle(new JobEvent(job.jobID, JobEventType.JOB_SETUP_COMPLETED));
        }
    }
}

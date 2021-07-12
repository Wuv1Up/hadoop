package org.apache.hadoop.yarn.event.mypack;

import org.apache.hadoop.yarn.event.AbstractEvent;

public class JobEvent extends AbstractEvent<JobEventType> {

    private String jobId;

    public JobEvent(String jobId, JobEventType jobEventType) {
        super(jobEventType);
        this.jobId = jobId;
    }

    public String getJobId() {
        return jobId;
    }

}

package org.apache.hadoop.yarn.event.mypack;


import org.apache.hadoop.yarn.conf.YarnConfiguration;

public class SimpleMRAppMasterTest {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        String jobId = "job_helloworld";
        SimpleMRAppMaster master = new SimpleMRAppMaster("MyMaster", jobId, 2);
        YarnConfiguration configuration = new YarnConfiguration();
        master.init(configuration);
        master.start();
        master.getDispatcher().getEventHandler().handle(new JobEvent(jobId, JobEventType.JOB_KILL));
        master.getDispatcher().getEventHandler().handle(new JobEvent(jobId, JobEventType.JOB_INIT));
        master.waitForServiceToStop(10);
    }
}

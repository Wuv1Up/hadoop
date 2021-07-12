package org.apache.hadoop.service;

import org.apache.hadoop.conf.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTestService {

    private static final Logger logger = LoggerFactory.getLogger(MyTestService.class);
    private Service service;


    private static class MyService extends AbstractService {

        /**
         * Construct the service.
         *
         * @param name service name
         */
        public MyService(String name) {
            super(name);
        }

        @Override
        public void init(Configuration conf) {
            logger.info("my service init");
            super.init(conf);
        }

        @Override
        protected void serviceInit(Configuration conf) throws Exception {
            super.serviceInit(conf);
        }
    }

    @Before
    public void init() {
        service = new MyService("init");
    }

    @Test
    public void testMyServiceLifecycle() {
        AbstractService.registerGlobalListener(new LoggingStateChangeListener());
        service.init(new Configuration());
        service.start();
        service.stop();
    }

    @Test
    public void testMyService() {
        AbstractService.registerGlobalListener(new LoggingStateChangeListener());
        service.init(new Configuration());
        service.start();
        service.stop();
    }

}

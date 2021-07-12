package org.apache.hadoop.service.myservice;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.CompositeService;

public class MyService extends CompositeService {

    public MyService(String name) {
        super(name);
    }

    public static void main(String[] args) {
        MyService atm = new MyService("ATM");
        atm.init(new Configuration());
        atm.start();
        atm.stop();
    }

    @Override
    protected void serviceInit(Configuration conf) throws Exception {
        System.out.println(conf.toString());
        System.out.println("服务初始化状态");
        super.serviceInit(conf);
    }

    @Override
    protected void serviceStart() throws Exception {
        System.out.println("服务启动成功");
        super.serviceStart();
    }

    @Override
    protected void serviceStop() throws Exception {
        System.out.println("服务停止");
        super.serviceStop();
    }
}

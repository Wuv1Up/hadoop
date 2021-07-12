package org.apache.hadoop.ipc.myipc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

import java.io.IOException;

/**
 * 编写RPC 协议, 也就是描述接口含有哪些方法.
 */
public class MyService {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        try {
            Server server = new RPC.Builder(config)
                    .setProtocol(MyClientProtocol.class)
                    .setInstance(new MyClientProtocolImpl())
                    .setBindAddress("localhost")
                    .setPort(8888)
                    .setNumHandlers(4).build();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


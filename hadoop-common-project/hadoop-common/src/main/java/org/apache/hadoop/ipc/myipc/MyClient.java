package org.apache.hadoop.ipc.myipc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MyClient {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        InetSocketAddress local = new InetSocketAddress("localhost", 8888);
        try {
            MyClientProtocol myClientProtocol = RPC.getProxy(MyClientProtocol.class, MyClientProtocol.versionID, local, config);
            int addResult = myClientProtocol.add(1, 2);
            String result = myClientProtocol.echo("This is my result:" + addResult);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

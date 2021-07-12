package org.apache.hadoop.ipc.myipc;

import org.apache.hadoop.ipc.VersionedProtocol;

import java.io.IOException;

public interface MyClientProtocol extends VersionedProtocol {

    public static final long versionID = 1L;

    public String echo(String value) throws IOException;

    public int add(int v1, int v2) throws IOException;
}

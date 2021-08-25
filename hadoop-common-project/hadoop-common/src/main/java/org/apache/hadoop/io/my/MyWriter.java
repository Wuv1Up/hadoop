package org.apache.hadoop.io.my;

import org.apache.hadoop.io.Writable;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * wuv1:writable -> 测试 Writable 接口
 */
public class MyWriter {

  public static void main(String[] args) throws IOException {
    MyBook myBook = new MyBook();
    myBook.name = "hello";
    // 字节数组输出流, 底层有 buffer
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    // DataOutputStream 装饰类型
    DataOutputStream dataOutputStream = new DataOutputStream(out);
    myBook.write(dataOutputStream);
    dataOutputStream.flush();
    byte[] bytes = out.toByteArray();

    ByteArrayInputStream in = new ByteArrayInputStream(bytes);
    DataInputStream dis = new DataInputStream(in);
    MyBook read = new MyBook();
    read.readFields(dis);
    System.out.println(read.name);
    System.out.println(myBook.name);
  }

  /**
   * 继承 Writable 接口, 需要实现 write 和 readFields 方法.
   */
  private static class MyBook implements Writable {

    private String name;

    @Override
    public void write(DataOutput out) throws IOException {
      byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
      // 需要写入长度, 要不然不知道要读取多少个字节.
      out.writeInt(nameBytes.length);
      out.write(nameBytes);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
      int length = in.readInt();
      byte[] nameBytes = new byte[length];
      in.readFully(nameBytes);
      name = new String(nameBytes, StandardCharsets.UTF_8);
    }
  }

}

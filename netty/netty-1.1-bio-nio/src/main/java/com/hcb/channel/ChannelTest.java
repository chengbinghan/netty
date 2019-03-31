package com.hcb.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author ice
 * @date 18:49  2019-03-30
 * @description
 */
public class ChannelTest {
    public static void main(String[] args) throws IOException {

        ReadableByteChannel src = Channels.newChannel(System.in);
        WritableByteChannel dest = Channels.newChannel(System.out);

        //copy1(src, dest);
        copy2(src, dest);;

    }


    public static void copy1(ReadableByteChannel srcChannel, WritableByteChannel descChannel) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(8 * 1024);

        while ((srcChannel.read(buffer)) != -1) {

            buffer.flip();
            descChannel.write(buffer);
            //读过的压缩丢弃掉
            buffer.compact();

        }
        buffer.flip();
        while (buffer.hasRemaining()) {
            descChannel.write(buffer);
        }
    }


    public static void copy2(ReadableByteChannel srcChannel, WritableByteChannel desc) throws IOException {


        ByteBuffer buffer = ByteBuffer.allocate(8 * 1024);
        while (srcChannel.read(buffer) != -1) {

            buffer.flip();
            while (buffer.hasRemaining()) {
                desc.write(buffer);
            }

            buffer.clear();


        }


    }


}

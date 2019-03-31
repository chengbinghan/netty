package com.hcb.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * @author ice
 * @date 18:42  2019-03-29
 * @description
 */
public class ByteBufferApiTest {
    public static void main(String[] args) {
       // flipTest();
        compactTest();

    }

    public static void flipTest(){
         ByteBuffer buffer = ByteBuffer.allocateDirect(10);
         buffer.put((byte) 'H').put((byte) 'e').put((byte) 'l').put((byte) 'l').put((byte) 'o');
         int position = buffer.position();
        System.out.println(position);

        final byte beforeFlip = buffer.get();
        System.out.println("before flip get():" +beforeFlip);

        System.out.println("before flip limit is :" +buffer.limit());
        System.out.println("before flip position is :" +buffer.position());
        buffer.flip();//相当于buffer.limit(buffer.position()).position(0);
        System.out.println("after flip limit is :" +buffer.limit());
        System.out.println("after flip position is :" +buffer.position());

        final byte afterFlip = buffer.get();
        System.out.println("after flip get():" +afterFlip);

        System.out.println("get 前position" + buffer.hasRemaining() + ":" + buffer.remaining());
        final byte b = buffer.get();
        System.out.println("after get position 减小：" + buffer.hasRemaining() + ":" + buffer.remaining());
    }

    public static void compactTest(){
        final CharBuffer buffer = CharBuffer.allocate(10);
        buffer.put('H').put( 'e').put( 'l').put( 'l').put('o');
       /* System.out.println("compact 之前mark:" + buffer.mark());
        System.out.println("compact 之前position:" + buffer.position());
        System.out.println("compact 之前limit:" + buffer.limit());
        System.out.println("compact 之前capacity:" + buffer.capacity());
        final int remaining = buffer.remaining();
        buffer.flip();
        for (int i = 0 ; i < remaining; i++){

            System.out.print(buffer.get());
            System.out.print(" ");

        }
        System.out.println("\t\n");*/

        //position 左移1
        buffer.limit(buffer.position());
        buffer.flip();
       buffer.get();
       buffer.get();

       // final int remaining = buffer.remaining();
        int remaining = buffer.limit() - buffer.position();
        buffer.compact();

        buffer.flip();
        for (int i = 0 ; i < remaining; i++){

            System.out.print(buffer.get());
            System.out.print(" ");

        }



    }
}

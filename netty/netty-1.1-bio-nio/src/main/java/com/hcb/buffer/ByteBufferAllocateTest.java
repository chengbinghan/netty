package com.hcb.buffer;

import java.nio.ByteBuffer;

/**
 * @author ice
 * @date 20:58  2019-03-23
 * @description 证明ByteBuffer.allocateDirect(memory);分配的内存在heap 外，ByteBuffer.allocate(memory); 在heap 内
 */


public class ByteBufferAllocateTest {
    static int memory = 10240000;//不要设置的太小
    public static void main(String[] args) {

     // allocateJVM();
        allocateDirect();

    }

    static void allocateJVM(){
         long start = Runtime.getRuntime().freeMemory();
        System.out.println("起始的freememory: " + start);

        System.out.println("分配:" + memory);
        ByteBuffer buffer = ByteBuffer.allocate(memory);
        long end = Runtime.getRuntime().freeMemory();
        System.out.println("分配后的freememory: " + end);
        System.out.println("相差了：" + (start - end));

    }

    static void allocateDirect(){
        long start = Runtime.getRuntime().freeMemory();
        System.out.println("起始的freememory: " + start);
        int memory = 10240000;//不要设置的太小
        System.out.println("分配:" + memory);
         ByteBuffer buffer = ByteBuffer.allocateDirect(memory);
        long end = Runtime.getRuntime().freeMemory();
        System.out.println("分配后的freememory: " + end);
        System.out.println("相差了：" + (start - end));

    }
}

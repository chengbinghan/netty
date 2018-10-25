package com.hcb.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author ChengBing Han
 * @date 20:32  2018/10/21
 * @description
 */
public class HelloFuture {

    static KitchenWare kitchenWare = null;

    public static void main(String[] args) {

        final long start = System.currentTimeMillis();

        Callable<KitchenWare> callable = new Callable<KitchenWare>() {
            public KitchenWare call() throws Exception {
                System.out.println("开始购买厨具");
                Thread.sleep(5000);

                return new KitchenWare();
            }
        };


        FutureTask<KitchenWare> futureTask = new FutureTask<KitchenWare>(callable);
        final Thread buyKitchenWare = new Thread(futureTask);
        buyKitchenWare.start();
        //开始购买食材
        System.out.println("开始购买食材");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("食材购买完毕");

        while (!futureTask.isDone()){
            try {
               kitchenWare = futureTask.get();
                System.out.println("购买厨具完毕");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println("开始cook....");
        final long end = System.currentTimeMillis();
        System.out.println("total time : " + (end -start));


    }

    private static class KitchenWare {

        @Override
        public String toString() {
            return "食材是：XX,XXX....";
        }
    }
}

package com.hcb.netty.callback;

/**
 * @author ChengBing Han
 * @date 16:23  2018/10/21
 * @description
 */
public class TestCallBack {
    public static void main(String[] args) {
        final FetcherImpl fetcher = new FetcherImpl();
        fetcher.fetchData(new FetchCallBack() {
            public void onSuccess() {
                System.out.println("success...................");
            }

            public void onError() {
                System.out.println("error..................");
            }
        });
    }
}

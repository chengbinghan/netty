package com.hcb.netty.callback;

import java.util.Random;

/**
 * @author ChengBing Han
 * @date 16:20  2018/10/21
 * @description
 */
public class FetcherImpl implements Fetcher {

    public void fetchData(FetchCallBack fetchCallBack) {
        final Random random = new Random();
        final int i = random.nextInt();
        if(i %2 == 0){
            fetchCallBack.onSuccess();
        }else {
            fetchCallBack.onError();
        }

    }
}

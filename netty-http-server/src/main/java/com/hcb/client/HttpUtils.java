package com.hcb.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import java.security.cert.X509Certificate;

/**
 * @author ice
 * @date 21:36  2019-06-05
 * @description
 */
@Slf4j
public class HttpUtils {



    private static final Integer SSL_PORT = 443;//443 是http ssl port
    private static final Integer HTTP_CONNECTION_TIMEOUT = 30*1000;
    private static final Integer HTTP_SOCKET_TIMEOUT = 30*1000;

    public static void main(String[] args) {
        post("{\"name\":\"张三\"}","http://127.0.0.1:8080:/api/test/json");
    }

    public static String post(String param, String url) {


        DefaultHttpClient httpClient = null;
        try {


            httpClient = new DefaultHttpClient();//主要对象

            //https 转http, https 证书会过期，这是一个问题
            if (url.startsWith("https")) {
                httpClient = wrapClientNew(httpClient);
            }
            initClient(httpClient);
            HttpPost post = new HttpPost(url);


            StringEntity s = new StringEntity(param, HTTP.UTF_8);// 避免乱码
            s.setContentEncoding("UTF-8");
            post.setEntity(s);
            post.addHeader("Content-type", "application/json;charset=utf-8");
            post.addHeader("Accept", "application/json");
           // post.addHeader("Authorization", authorization);


            CloseableHttpResponse response = httpClient.execute(post);

            handleResponse(response);


        } catch (Exception e) {
            log.error("failed to send request to sms platform, {},{}", e.getMessage(), e.getStackTrace());

        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
        }

        return null;

    }

    static void handleResponse(CloseableHttpResponse response) {
        try {
            if (response != null && response.getStatusLine() != null && response.getStatusLine().getStatusCode() == 200) {

                String responseJsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
                JSONObject jsonObject = JSON.parseObject(responseJsonStr);
                String code = jsonObject.getString("code");

                log.info("response code:{}, response entity:{}",code,jsonObject);


            }
        } catch (Exception e) {
            log.error("response form platform ,parse response failed !");
        }

    }


    /**
     * 包装http client
     *
     * @param base
     * @return
     */
    private static DefaultHttpClient wrapClientNew(DefaultHttpClient base) {
        try {
            TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] certificate, String authType) {
                    return true;
                }
            };
            SSLSocketFactory sf = new SSLSocketFactory(acceptingTrustStrategy, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            //base.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, sf));

            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();


            sr.register(new Scheme("https", SSL_PORT, sf));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {


            ex.printStackTrace();
            return null;
        }
    }


    /**
     * 初始化httpclient,主要是设置超时时间等属性
     *
     * @param httpClient
     */
    private static void initClient(DefaultHttpClient httpClient) {


        if (httpClient != null) {
            HttpParams httpParams = httpClient.getParams();
            //避免网络抖动,假死
            HttpConnectionParams.setConnectionTimeout(httpParams, HTTP_CONNECTION_TIMEOUT); // http.connection.timeout
            HttpConnectionParams.setSoTimeout(httpParams, HTTP_SOCKET_TIMEOUT); // http.socket.timeout
        }
    }


}

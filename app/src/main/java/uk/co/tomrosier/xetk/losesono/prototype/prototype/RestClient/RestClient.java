package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import java.security.KeyStore;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.Constants;

/**
 * Created by xetk on 05/03/15.
 */
public class RestClient {

    private static final String BASE_URL = Constants.BASE_URL;

    private AsyncHttpClient client = new AsyncHttpClient();

    public RestClient(Context context) {

        setupSSL();

        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        client.setCookieStore(cookieStore);

    }

    public RestClient() {
        setupSSL();
    }


    private void setupSSL() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);

            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            client.setSSLSocketFactory(sf);
        } catch (Exception e) {

        }
    }

    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        String uri = getCompleteURL(url);

        System.out.println("Rest Get URL: " + uri);

        client.get(uri, params, responseHandler);
    }

    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String uri = getCompleteURL(url);

        System.out.println("Rest Post URL: " + uri);

        client.post(uri, params, responseHandler);
    }

    public static String getCompleteURL(String relURL) {
        return BASE_URL + '/' + relURL;
    }
}

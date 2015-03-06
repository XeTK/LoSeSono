package uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.Constants;

/**
 * Created by xetk on 05/03/15.
 */
public class RestClient {

    protected static final String BASE_URL = Constants.BASE_URL;

    protected static AsyncHttpClient client = new AsyncHttpClient();

    protected static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String uri = getCompleteURL(url);

        System.out.println("Rest Get URL: " + uri);

        client.get(uri, params, responseHandler);
    }

    protected static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String uri = getCompleteURL(url);

        System.out.println("Rest Post URL: " + uri);

        client.post(uri, params, responseHandler);
    }

    protected static String getCompleteURL(String relURL) {
        return BASE_URL + '/' + relURL;
    }
}

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
 * This is the core of dealing with HTTP REST requests, it enables different types of requests to the backend services.
 */
public class RestClient {

    // Get the base URL that we work with, then add the endpoints that we want to work with.
    private static final String BASE_URL = Constants.BASE_URL;

    // Keep the HTTP client alive while we are doing RESTful requests.
    private AsyncHttpClient client = new AsyncHttpClient();

    // Default constructor where we can pass in the context which will stick our cookies to to keep the application authenticated.
    public RestClient(Context context) {

        // Setup a SSL connection to ensure our data is secure.
        setupSSL();

        // This is global store for all the cookies within the application, we pin it to the context so we can use it in different locations within the application.
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        client.setCookieStore(cookieStore);
    }

    // Setup a SSL connection for none cookie dependant connections, so we still have SSL\TLS security on the connection.
    public RestClient() {
        setupSSL();
    }

    // Configure a SSL\TLS protected connection ready for our data to be exchanged.
    private void setupSSL() {

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

            trustStore.load(null, null);

            // This is the key factory it stores all the encryption keys needed for the SSL to work correctly.
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);

            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            // Attach the certificates to the http client.
            client.setSSLSocketFactory(sf);
        } catch (Exception e) {

        }
    }

    // Do a get request to the server, with all the parameters setup.
    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        // Join the URL together to get the correct endpoint.
        String uri = getCompleteURL(url);

        System.out.println("Rest Get URL: " + uri);

        // Do a get request to the server.
        client.get(uri, params, responseHandler);
    }

    // Same again for POST requests.
    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        // Join the URL's together so we have the correct URL for the endpoint.
        String uri = getCompleteURL(url);

        System.out.println("Rest Post URL: " + uri);

        // Post the data to the server.
        client.post(uri, params, responseHandler);
    }

    // Join the BASE_URL so the address of the server to the end point link.
    public static String getCompleteURL(String relURL) {
        return BASE_URL + '/' + relURL;
    }
}

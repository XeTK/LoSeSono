package uk.co.tomrosier.xetk.losesono.prototype.prototype.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.RestClient;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.activities.LoginActivity;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.entities.User;

/**
 * Created by xetk on 09/03/15.
 */
public class Login {

    private AsyncHttpClient client = new AsyncHttpClient();

    private Context applicationContext;

    public static User user;

    public Login(Context context) {
        this.applicationContext = context;

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);

            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            client.setSSLSocketFactory(sf);

            PersistentCookieStore cookieStore = new PersistentCookieStore(context);
            client.setCookieStore(cookieStore);
        } catch (Exception e) {
        }
    }


    public void loginUser(String username, String password, final AjaxCompleteHandler callback ) {

        String uri = RestClient.getCompleteURL("login");

        System.out.println("Rest Get URL: " + uri);

        client.setBasicAuth(username, password);

        client.get(
                uri,
                null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (statusCode == 200) {
                            System.out.println("User has been logged in + " + response.toString());


                            try {
                                user = new User(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            callback.handleAction("Success");
                        } else {
                            System.err.println("Getting Messages failed with status code of " + statusCode);
                            callback.handleAction("Failed");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                        System.out.println("Error: " + statusCode);

                        callback.handleAction("Failed");
                    }
                }
        );

    }
    public void autoLogin(final AjaxCompleteHandler handler) {

        final Context context = applicationContext;

        SharedPreferences sharedPref = context.getSharedPreferences("LoginPrefs", context.MODE_PRIVATE);

        String username = sharedPref.getString("username", "");
        String password = sharedPref.getString("password", "");

        if ((username != null && password != null) && (username.length() > 0 && password.length() > 0)) {
            if (username.length() > 0 && password.length() > 0) {

                Login login = new Login(context);

                login.loginUser(
                        username,
                        password,
                        new AjaxCompleteHandler() {
                            @Override
                            public void handleAction(Object someData) {

                                String text = (String)someData;

                                if (text.equals("Success")) {

                                    if (handler != null)
                                        handler.handleAction("Success");

                                } else {
                                    Intent myIntent = new Intent(context, LoginActivity.class);
                                    context.startActivity(myIntent);

                                    if (handler != null)
                                        handler.handleAction("Failed");
                                }
                            }
                        }
                );

            }
        } else {
            Intent myIntent = new Intent(context, LoginActivity.class);
            context.startActivity(myIntent);
        }
    }

    public void autoLogin() {
        autoLogin(null);
    }
}
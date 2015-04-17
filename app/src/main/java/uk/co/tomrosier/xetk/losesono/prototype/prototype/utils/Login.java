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
 * This authenticates the application with the back end, it has automatic login and dealing with all of the requests.
 */
public class Login {

    // Get a Async HTTP Client so we can do some calls to the backend to authenticate it.
    private AsyncHttpClient client = new AsyncHttpClient();

    // Keep the context of the activity that has invoked the login state.
    private Context applicationContext;

    // Keep track of the user that has been logged into the application.
    public static User user;

    // This is run when the class is instantiated, it will setup the SSL connection that is needed for the application to run.
    public Login(Context context) {
        this.applicationContext = context;

        try {
            // Get the place to store the keys from SSL.
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

            trustStore.load(null, null);

            // Keep track of the socket.
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);

            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            client.setSSLSocketFactory(sf);

            PersistentCookieStore cookieStore = new PersistentCookieStore(context);
            client.setCookieStore(cookieStore);
        } catch (Exception e) {
        }
    }

    // This logs the user into the rest API.
    public void loginUser(String username, String password, final AjaxCompleteHandler callback ) {

        // Get the URL that we need to send the request to.
        String uri = RestClient.getCompleteURL("login");

        System.out.println("Rest Get URL: " + uri);

        // Setup the Basic Auth that is needed to authenticate the user.
        client.setBasicAuth(username, password);

        // Send a request to login to the server.
        client.get(
                uri,
                null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If the responce was successful, then we can proceed and set things up :)
                        if (statusCode == 200) {
                            System.out.println("User has been logged in + " + response.toString());

                            // Save the user so we can access it later.
                            try {
                                user = new User(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Send the status back to the other method, so we can work on doing the things after we are authenticated.
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

    // This automatically logs users in with saved credentials, this is useful for when the application starts or we are dealing with notifications.
    public void autoLogin(final AjaxCompleteHandler handler) {

        // Save the application context so we can work with it.
        final Context context = applicationContext;

        // Get the preferences we saved earlier.
        SharedPreferences sharedPref = context.getSharedPreferences("LoginPrefs", context.MODE_PRIVATE);

        // Get the values we saved earlier eg the username and password that we need to sign in.
        String username = sharedPref.getString("username", "");
        String password = sharedPref.getString("password", "");

        // Check the details we are processing are good enough to proceed.
        if ((username != null && password != null) && (username.length() > 0 && password.length() > 0)) {

            loginUser(
                    username,
                    password,
                    new AjaxCompleteHandler() {
                        @Override
                        public void handleAction(Object someData) {

                            // Convert the response data to a string to work with.
                            String text = (String)someData;

                            // If the user was successfully logged in the n we tell the method that called login that we logged in successfully
                            if (text.equals("Success")) {

                                if (handler != null)
                                    handler.handleAction("Success");

                            } else {
                                // If we failed then we show the login dialog to correct the details.
                                Intent myIntent = new Intent(context, LoginActivity.class);
                                context.startActivity(myIntent);

                                if (handler != null)
                                    handler.handleAction("Failed");
                            }
                        }
                    }
            );
        } else {
            // if all else fails get the login dialog up for the user to login.
            Intent myIntent = new Intent(context, LoginActivity.class);
            context.startActivity(myIntent);
        }
    }

    // Do login without having ajax type functions.
    public void autoLogin() {
        autoLogin(null);
    }
}
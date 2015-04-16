package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.RestClient.RestClient;

/**
 * This is for handling registration of a user within the application.
 */

public class RegisterActivity extends ActionBarActivity {

    // This is the text fields for the applications, they are needed for registering the user.
    private EditText txtUserName;
    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtEmail;
    private EditText txtPassword;

    // This is the button that confirms registration of the user.
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Grab the textfiends to make them more easy to access.
        txtUserName  = (EditText) findViewById(R.id.txtUserName);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName  = (EditText) findViewById(R.id.txtLastName);
        txtEmail     = (EditText) findViewById(R.id.txtEmail);
        txtPassword  = (EditText) findViewById(R.id.txtPassword);

        // Grab the button to make it more easy to access.
        btnRegister  = (Button) findViewById(R.id.btnRegister);

        // This is handles pressing the registration button.
        btnRegister.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    // Prepare a list of parameters ready to pass to posting data to the server.
                    RequestParams params = new RequestParams();

                    // Bind the information to a post request ready to use.
                    params.put("firstname", txtFirstName.getText().toString().trim());
                    params.put("lastname", txtLastName.getText().toString().trim());
                    params.put("username", txtUserName.getText().toString().trim());
                    params.put("email", txtEmail.getText().toString().trim());
                    params.put("password", txtPassword.getText().toString().trim());

                    // Get the rest client ready for use.
                    RestClient restClient = new RestClient();

                    // Call the server passing the information needed to register the user, note that there is no extra security on this.
                    restClient.post("register", params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                if (statusCode == 200) {
                                    // If the user registered correctly then we tell the user that they completed correctly.
                                    Toast.makeText(getApplicationContext(), "Registration Complete", Toast.LENGTH_LONG).show();
                                    // Close the form as we are done registering the user.
                                    finish();
                                } else {
                                    System.err.println("Getting Messages failed with status code of " + statusCode);
                                }
                            }
                        }
                    );
                }
            }
        );
    }
}

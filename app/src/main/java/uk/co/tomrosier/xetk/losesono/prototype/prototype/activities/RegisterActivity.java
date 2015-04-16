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


public class RegisterActivity extends ActionBarActivity {

    EditText txtUserName;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtEmail;
    EditText txtPassword;

    Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        txtUserName  = (EditText) findViewById(R.id.txtUserName);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName  = (EditText) findViewById(R.id.txtLastName);
        txtEmail     = (EditText) findViewById(R.id.txtEmail);
        txtPassword  = (EditText) findViewById(R.id.txtPassword);

        btnRegister  = (Button) findViewById(R.id.btnRegister);


        btnRegister.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    RequestParams params = new RequestParams();

                    params.put("firstname", txtFirstName.getText().toString().trim());
                    params.put("lastname", txtLastName.getText().toString().trim());
                    params.put("username", txtUserName.getText().toString().trim());
                    params.put("email", txtEmail.getText().toString().trim());
                    params.put("password", txtPassword.getText().toString().trim());

                    RestClient restClient = new RestClient();

                    restClient.post("register", params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                if (statusCode == 200) {
                                    Toast.makeText(getApplicationContext(), "Registration Complete", Toast.LENGTH_LONG).show();
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

package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.AjaxCompleteHandler;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.utils.Login;

/**
 * This activity handles the login dialog for the end user to login into the application.
 */

public class LoginActivity extends ActionBarActivity {

    // Make the buttons accessable to the whole class.
    private Button btnRegister;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Get the preferences ready to save the login details for the user.
        SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        // Get the edittext items where the user details have been entered.
        EditText txtUsername = (EditText) findViewById(R.id.txtUsernameLA);
        EditText txtPassword = (EditText) findViewById(R.id.txtPasswordLA);

        // Replace the text within the text fields with the stored details ready to allow people to login.
        txtUsername.setText(sharedPref.getString("username", ""));
        txtPassword.setText(sharedPref.getString("password", ""));

        // Get the buttons from the page so they can be accessed.
        btnRegister = (Button) findViewById(R.id.btnRegisterLA);
        btnLogin    = (Button) findViewById(R.id.btnLoginLA);

        // This loads up the registration activity ready for users to register to use the application.
        btnRegister.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // Create the intent to switch to the registration activity.
                    Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                }
            }
        );

        // This handles what is done when the button is pressed to login.
        btnLogin.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    // Get the class that handles login into the application.
                    Login login = new Login(getApplicationContext());

                    // Get the information from the text fields with the user details in. As they are not accessable from the other scope.
                    EditText txtUsername = (EditText) findViewById(R.id.txtUsernameLA);
                    EditText txtPassword = (EditText) findViewById(R.id.txtPasswordLA);

                    // If they are not empty and not null then we can do things.
                    if (txtUsername != null && txtPassword != null) {

                        // Get the text that has been entered into the textfields and get the strings that we want to manipulate.
                        final String username = txtUsername.getText().toString();
                        final String password = txtPassword.getText().toString();

                        // Authenticate the user to use the application.
                        login.loginUser(
                                username,
                                password,
                                new AjaxCompleteHandler() {
                                    @Override
                                    public void handleAction(Object someData) {

                                        String text = (String)someData;

                                        // What do we do if the user is authenticated successfully.
                                        if (text.equals("Success")) {

                                            // Tell them they logged in succesfully, useful dialog for them.
                                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_LONG).show();

                                            // Get the saved prefrences for the application, so we can ammend it.
                                            SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

                                            // Get the tools to edit the saved prefrences.
                                            SharedPreferences.Editor editor = sharedPref.edit();

                                            // Save the login details ready for use to use them again later.
                                            editor.putString("username", username);
                                            editor.putString("password", password);

                                            // Commit that data to the prefrences ready for us to use later.
                                            editor.commit();

                                            // Close the activity once we have authenticated the user.
                                            finish();
                                        } else {
                                            // Tell the user they got there details wrong.
                                            Toast.makeText(LoginActivity.this, "Please try again.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                        );
                    } else {
                        // Error state if something is null when its not supost to be.
                        Toast.makeText(LoginActivity.this, "Something is null...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        );
    }
}

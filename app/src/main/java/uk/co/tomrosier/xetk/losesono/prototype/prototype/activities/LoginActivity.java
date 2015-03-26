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

public class LoginActivity extends ActionBarActivity {

    Button btnRegister;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        EditText txtUsername = (EditText) findViewById(R.id.txtUsernameLA);
        EditText txtPassword = (EditText) findViewById(R.id.txtPasswordLA);

        txtUsername.setText(sharedPref.getString("username", ""));
        txtPassword.setText(sharedPref.getString("password", ""));


        btnRegister = (Button) findViewById(R.id.btnRegisterLA);
        btnLogin    = (Button) findViewById(R.id.btnLoginLA);


        btnRegister.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                }
            }
        );

        btnLogin.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Login login = new Login(getApplicationContext());

                    EditText txtUsername = (EditText) findViewById(R.id.txtUsernameLA);
                    EditText txtPassword = (EditText) findViewById(R.id.txtPasswordLA);

                    if (txtUsername != null && txtPassword != null) {

                        final String username = txtUsername.getText().toString();
                        final String password = txtPassword.getText().toString();

                        login.loginUser(
                                username,
                                password,
                                new AjaxCompleteHandler() {
                                    @Override
                                    public void handleAction(Object someData) {

                                        String text = (String)someData;

                                        if (text.equals("Success")) {
                                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_LONG).show();

                                            SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString("username", username);
                                            editor.putString("password", password);
                                            editor.commit();


                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Please try again.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                        );
                    } else {
                        Toast.makeText(LoginActivity.this, "Something is null...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        );
    }
}

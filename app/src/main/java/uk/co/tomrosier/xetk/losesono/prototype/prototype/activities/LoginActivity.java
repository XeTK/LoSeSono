package uk.co.tomrosier.xetk.losesono.prototype.prototype.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import uk.co.tomrosier.xetk.losesono.prototype.prototype.R;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.services.AjaxCompleteHandler;
import uk.co.tomrosier.xetk.losesono.prototype.prototype.services.Login;

public class LoginActivity extends ActionBarActivity {

    Button btnRegister;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

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

                        String username = txtUsername.getText().toString();
                        String password = txtPassword.getText().toString();

                        login.loginUser(
                                username,
                                password,
                                new AjaxCompleteHandler() {
                                    @Override
                                    public void handleAction(String someData) {
                                        if (someData.equals("Success")) {
                                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

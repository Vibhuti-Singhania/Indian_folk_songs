package com.example.android.indian_folk_songs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Vibhuti Singhania on 12-08-2018.
 */

public class LoginActivity extends AppCompatActivity{


    private Button mLoginButton;
    private TextView mRegisterText;
    private EditText mUsername, mPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton =(Button)findViewById(R.id.login_button);
        mUsername =(EditText)findViewById(R.id.username_editText);
        mPassword = (EditText)findViewById(R.id.password_editText);
        mRegisterText =(TextView)findViewById(R.id.register_text);

        mRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
              startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                mLoginButton.setEnabled(false);
                   userLogin();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void userLogin() {

        final String username = mUsername.getText().toString().trim();
        final String password = mPassword.getText().toString();

        if(TextUtils.isEmpty(username)){
           mUsername.setHint("Username must not be empty!");
           mUsername.setHintTextColor(Color.RED);
        }
        if(TextUtils.isEmpty(password)){
           mPassword.setHint("Password must not be empty!");
           mPassword.setHintTextColor(Color.RED);
        }

        class UserLogin extends AsyncTask<Void,Void,String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);

                //if no error in response
                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    //getting the user from the response
                    JSONObject userJson = obj.getJSONObject("user");

                    //creating a new user object
                    Users user = new Users(
                            userJson.getInt("id"),
                            userJson.getString("username"),
                            userJson.getString("useremail")
                    );

                    //storing the user in shared preferences
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                    //starting the main activity
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
        }
    }
    UserLogin ul = new UserLogin();
    ul.execute();
    }
}

package com.example.deepakrattan.apirequestdemo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deepakrattan.apirequestdemo.R;
import com.example.deepakrattan.apirequestdemo.apiConfiguration.ApiConfiguration;
import com.example.deepakrattan.apirequestdemo.httpRequsetProcessor.HttpRequestProcessor;
import com.example.deepakrattan.apirequestdemo.httpRequsetProcessor.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by deepak.rattan on 12/24/2016.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText edtName, edtPasswd;
    private Button btnLogin, btnRegister;
    private String name, passwd;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String baseURL, urlLogin, jsonStringToPost, jsonResponseString;
    private boolean success;
    private String message, address, emailID, phone, password, userName;
    private int userID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //findViewById

        edtName = (EditText) findViewById(R.id.edtNameLogin);
        edtPasswd = (EditText) findViewById(R.id.edtPswdLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister1);

        //Initialization
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();

        //Getting base url
        baseURL = apiConfiguration.getApi();
        urlLogin = baseURL + "Registration/GetLogin";


        //On clicking login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting name and password
                name = edtName.getText().toString();
                passwd = edtPasswd.getText().toString();

                new LoginTask().execute(name, passwd);

            }
        });

        //On clicking Register Button move to registration screen

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

    }

    public class LoginTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            name = params[0];
            passwd = params[1];

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserName", name);
                jsonObject.put("Password", passwd);

                jsonStringToPost = jsonObject.toString();
                response = httpRequestProcessor.pOSTRequestProcessor(jsonStringToPost, urlLogin);
                jsonResponseString = response.getJsonResponseString();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonResponseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d("Response String", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                success = jsonObject.getBoolean("success");
                Log.d("Success", String.valueOf(success));
                message = jsonObject.getString("message");
                Log.d("message", message);

                if (success) {
                    JSONArray responseData = jsonObject.getJSONArray("responseData");
                    for (int i = 0; i < responseData.length(); i++) {
                        JSONObject object = responseData.getJSONObject(i);
                        userID = object.getInt("UserId");
                        Log.d("userId", String.valueOf(userID));
                        name = object.getString("Name");
                        Log.d("name", name);
                        address = object.getString("Address");
                        Log.d("address", address);
                        emailID = object.getString("EmailId");
                        Log.d("emailId", emailID);
                        phone = object.getString("Phone");
                        Log.d("phone", phone);
                        userName = object.getString("UserName");
                        Log.d("userName", userName);
                        password = object.getString("Password");
                        Log.d("password", password);
                    }
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

package com.example.boatpoolingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class login extends AppCompatActivity implements View.OnClickListener{
    Button signin_btn;
    TextView newUser,forgotPass,Verify;
    Dialog dialog;
    private AwesomeValidation awesomeValidation;
    ProgressDialog pDialog;

    EditText etemail,etpassword;
    String email,password;
   public static String id,name,dob,lastName,firstName,gender,emergencyMobile,mobile,city,driverID,bio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initialization
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        etemail=findViewById(R.id.email);
        Verify=findViewById(R.id.Verify);
        etpassword=findViewById(R.id.password);
        forgotPass=findViewById(R.id.forgotPass);
       // dialog = new Dialog(this);
        newUser=(TextView)findViewById(R.id.newUser);
        signin_btn=(Button)findViewById(R.id.signin_btn);
        pDialog = new ProgressDialog(login.this);
        pDialog.setCancelable(false);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
       // awesomeValidation.addValidation(this, R.id.password, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.passworderror);

        signin_btn.setOnClickListener(this);
        newUser.setOnClickListener(this);
        Verify.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signin_btn) {
            email = etemail.getText().toString();
            password = etpassword.getText().toString();

            if ((awesomeValidation.validate())&& (validation())) {
                new AsyncLogin().execute(email, password);

            }
        }
        else if(v.getId()==R.id.newUser){
            Intent i = new Intent(login.this,SignUp.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.forgotPass){
            Intent i = new Intent(login.this,ForgotPassword.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.Verify){
            Intent i = new Intent(login.this,VerifyEmail.class);
            startActivity(i);
        }

    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(login.this);
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.getting_address));

            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.login);
                Log.d("params====",""+params.toString());

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("username", params[0]));
                nameValuePairs.add(new BasicNameValuePair("password",params[1]));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse httpResponse=httpClient.execute(httpPost);
                return_text=readResponse(httpResponse);
                Log.d("LOGIN RESPONSE1",""+return_text);

            }catch(Exception e){
                e.printStackTrace();

            }
            return return_text;
        }
        public String readResponse(HttpResponse res) {
            InputStream is=null;
            String return_text="";
            try {
                is=res.getEntity().getContent();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String line="";
                StringBuffer sb=new StringBuffer();
                while ((line=bufferedReader.readLine())!=null)
                {
                    sb.append(line);
                }
                return_text=sb.toString();
            } catch (Exception e)
            {
            }
            return return_text;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("LOGIN RESPONSE",""+result);
            // pdLoading.dismiss();
            pDialog.dismiss();
            try{
                if(result.toString().contains("Please verify your email first.")){
                    Toast.makeText(login.this, "Please verify your email first.", Toast.LENGTH_LONG).show();

                    Log.d("Response", "onPostExecute: "+"in verify");
                }
                else {
                    JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject!=null){
                        Log.d("RESULT", jsonObject.toString()+""+jsonObject.getString("message"));
                        if(jsonObject.getString("message").contains("Login Successfull."))
                        {

                            id=  jsonObject.getString("id");

                            name= jsonObject.getString("Name");

                            JSONObject jsonObject1=jsonObject.getJSONObject("data");

                            firstName= jsonObject1.getString("firstName");
                            lastName= jsonObject1.getString("lastName");
                            dob= jsonObject1.getString("dob");
                            gender=  jsonObject1.getString("gender");


                            if(jsonObject1.has("emergencyMobile")){
                                emergencyMobile=  jsonObject1.getString("emergencyMobile");

                            }
                            if(jsonObject1.has("mobile")){
                                mobile=jsonObject1.getString("mobile") ;

                            }
                            if(jsonObject1.has("driverID")){
                                driverID= jsonObject1.getString("driverID");

                            }

                            if(jsonObject1.has("city")){
                                city=  jsonObject1.getString("city");
                            }
                            if(jsonObject1.has("bio")){
                                city=  jsonObject1.getString("bio");
                            }
                            // Toast.makeText(login.this, getString(R.string.toast_login_success), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(login.this,Dashboard.class);
                            i.putExtra("id",id);
                            startActivity(i);

                        }
                        else if(jsonObject.getString("message").contains("Invalid Username/Password Combination.")){
                            Toast.makeText(login.this,"Invalid Username/Password Combination.", Toast.LENGTH_LONG).show();

                        }


                    }
                }






            }catch (Exception e){
                Toast.makeText(login.this, "Server Error "+e, Toast.LENGTH_LONG).show();
                Log.d("Response", "onPostExecute: "+ e);

                e.printStackTrace();
            }
        }
    }
    public boolean validation(){
        password=etpassword.getText().toString();
        boolean validate=true;
        if(password.isEmpty())
        {
            etpassword.setError(getString(R.string.invalid_password));
            validate= false;
        }

        return validate;
    }
}


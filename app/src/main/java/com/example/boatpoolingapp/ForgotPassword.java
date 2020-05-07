package com.example.boatpoolingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    EditText etemail;
    ProgressDialog pDialog;
    Button verify;
    String email;
    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etemail=findViewById(R.id.email);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);

        verify=(Button)findViewById(R.id.verify);
        pDialog=new ProgressDialog(ForgotPassword.this);
        verify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.verify) {
            email = etemail.getText().toString();
            if ((awesomeValidation.validate())){
                new SendEmail().execute(email);

            }

        }
    }
    class SendEmail extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.check_email));
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text = "";

            try {


                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Urls.forgot_password_seller);
                //  StringEntity httpiEntity=new StringEntity(jsonObject.toString());

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("username", params[0]));



                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);
                return_text= readResponse(response);
                return return_text;
            } catch (ClientProtocolException e) {

            } catch (IOException e) {

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
            Log.d("result===", "" + result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            try {
              //  JSONObject jsonObject = new JSONObject(result);

                if (result.contains("Please Check your email for reset code.")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.email_message), Toast.LENGTH_SHORT).show();

                    Intent i =new Intent(ForgotPassword.this,Verification.class);
                    startActivity(i);

                }else if(result.contains("Email not registered.")) {

                    Toast.makeText(getApplicationContext(), getString(R.string.email_error), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

package com.example.boatpoolingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import static com.example.boatpoolingapp.R.id.Password;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button signup_btn;
    Dialog dialog;
    boolean validate;
    DatePickerDialog datePickerDialog;
    private TextInputLayout inputLayout_username, inputLayout_password;
    ProgressDialog pDialog;
TextView oldUser;
    EditText  etdob, etemail, etPassword, etconfirmPassword,etfirst_name,etlast_name;
    String full_name,fname,lname, dob, email, Password, confirmPassword,gender;
    private AwesomeValidation awesomeValidation;
    Spinner genderSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
         genderSpinner = (Spinner) findViewById(R.id.genderSpinner);

        etfirst_name = findViewById(R.id.first_name);
        etlast_name = findViewById(R.id.last_name);

        etdob = findViewById(R.id.dob);
        etemail = findViewById(R.id.email);
        etPassword = findViewById(R.id.Password);
        etconfirmPassword = findViewById(R.id.confirmPassword);
        oldUser= findViewById(R.id.oldUser);

        List<String> list = new ArrayList<String>();
        list.add("Female");
        list.add("Male");
        list.add("Other");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);


        pDialog = new ProgressDialog(SignUp.this);
        pDialog.setCancelable(false);
            signup_btn = (Button) findViewById(R.id.signup_btn);

        awesomeValidation.addValidation(this, R.id.first_name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.last_name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);

        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        //   awesomeValidation.addValidation(this, R.id.Password, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.passworderror);
        //  awesomeValidation.addValidation(this, R.id.Password, "^(?=.*\\d)(?=.*[A-Z])([@$%&#])[0-9a-zA-Z]{4,}$", R.string.passworderror);
        //awesomeValidation.addValidation(this, R.id.Password, "^(?=.*\\d)(?=.*[A-Z])([@$%&#])[0-9a-zA-Z]{4,}$", R.string.passworderror);
        String regexPassword = ".{8,}";
        awesomeValidation.addValidation(this, R.id.Password, regexPassword, R.string.invalid_password);


        signup_btn.setOnClickListener(this);
        oldUser.setOnClickListener(this);
        etdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                      DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        int s=monthOfYear+1;
                        String a = dayOfMonth+"/"+s+"/"+year;
                        etdob.setText(""+a);
                    }
                };

                Time date = new Time();
                DatePickerDialog d = new DatePickerDialog(SignUp.this, dpd, date.year ,date.month, date.monthDay);
                d.show();

            }
        });

    }
    private AdapterView.OnItemSelectedListener OnCatSpinnerCL = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
            ((TextView) parent.getChildAt(0)).setTextSize(5);

        }

        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    public boolean validation(){
        Password=etPassword.getText().toString();
        confirmPassword=etconfirmPassword.getText().toString();
        dob=etdob.getText().toString();
        boolean validate=true;
        if(!Password.matches(confirmPassword))
        {
            etconfirmPassword.setError(getString(R.string.invalid_confirm_password));
            validate= false;
        }
        if(dob.isEmpty())
        {
            etdob.setError(getString(R.string.toast_dob));
            validate= false;
        }

        return validate;
    }
    @Override
    public void onClick(View v) {
        if (v == signup_btn) {
            gender = genderSpinner.getSelectedItem().toString();
            email = etemail.getText().toString();
            lname = etlast_name.getText().toString();
            fname = etfirst_name.getText().toString();
            full_name=fname+" "+lname;
            dob = etdob.getText().toString();
            Password = etPassword.getText().toString();
            confirmPassword = etconfirmPassword.getText().toString();

            if ((awesomeValidation.validate()) && (validation()))  {
                new SignUp.AsyncSignup().execute(fname,lname, email, Password, dob,gender);


                //process the data further
            }


        }
        else if(v==oldUser){
            Intent i = new Intent(SignUp.this,login.class);
            startActivity(i);
        }


    }

    private class AsyncSignup extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(SignUp.this);
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
                HttpPost httpPost=new HttpPost(Urls.signup);

                Log.d("params====",""+params[0]+params[1]+params[2]+params[3]);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("firstName", params[0]));
                nameValuePairs.add(new BasicNameValuePair("lastName", params[1]));
                nameValuePairs.add(new BasicNameValuePair("username",params[2]));
                nameValuePairs.add(new BasicNameValuePair("password",params[3]));
                nameValuePairs.add(new BasicNameValuePair("dob",params[4]));
                nameValuePairs.add(new BasicNameValuePair("gender",params[5]));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse httpResponse=httpClient.execute(httpPost);

                return_text=readResponse(httpResponse);
                Log.d("RESPONSE",""+return_text);

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
            Log.d("result===",""+result);
            // pdLoading.dismiss();


            pDialog.dismiss();
            try{
                    //JSONObject obj=new JSONObject(result);
                if(result.toString().contains("Sign up successfull. Please check your email for verification code.")){

                    Toast.makeText(SignUp.this, getString(R.string.toast_login_success), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(SignUp.this,VerifyEmail.class);
                    startActivity(i);

                }else {
                    Toast.makeText(SignUp.this, "Unsuccessfull", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position,
//                               long id) {
//        // TODO Auto-generated method stub
//        Toast.makeText(this, "YOUR SELECTION IS : " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
//
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//        // TODO Auto-generated method stub
//
//    }
}

package com.example.boatpoolingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.boatpoolingapp.Fragments.AccountFragment;
import com.example.boatpoolingapp.Fragments.AvailableRides;
import com.example.boatpoolingapp.Fragments.CurrentRides;
import com.example.boatpoolingapp.Fragments.DetailsFragment;
import com.example.boatpoolingapp.Fragments.FindARide;
import com.example.boatpoolingapp.Fragments.MessageFragment;
import com.example.boatpoolingapp.Fragments.OfferRide;
import com.example.boatpoolingapp.Fragments.ProfileFragment;
import com.example.boatpoolingapp.Fragments.RatingsFragment;
import com.example.boatpoolingapp.Fragments.RidesFragment;
import com.example.boatpoolingapp.Fragments.bookedRide;
import com.example.boatpoolingapp.Fragments.inboxFragment;
import com.example.boatpoolingapp.Fragments.leftRatings;
import com.example.boatpoolingapp.Fragments.notificationFragment;
import com.example.boatpoolingapp.Fragments.recievedRatings;
import com.example.boatpoolingapp.model.reservedUserUtils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements FindARide.OnFragmentInteractionListener , AvailableRides.OnFragmentInteractionListener, CurrentRides.OnFragmentInteractionListener, OfferRide.OnFragmentInteractionListener ,bookedRide.OnFragmentInteractionListener, RatingsFragment.OnFragmentInteractionListener, recievedRatings.OnFragmentInteractionListener, leftRatings.OnFragmentInteractionListener , inboxFragment.OnFragmentInteractionListener,MessageFragment.OnFragmentInteractionListener, notificationFragment.OnFragmentInteractionListener, AccountFragment.OnFragmentInteractionListener, DetailsFragment.OnFragmentInteractionListener {
    private Toolbar mToolbar;
    BottomNavigationView bottom_navigation;
    ProgressDialog pDialog;
    ArrayList<reservedUserUtils> reservedList=new ArrayList<>();
    public static String USERID;
    public static String _id,user_id,name,dearture_city,departure_time,ride_type,seats,Name,status,booking_status,driver_id,is_review;
    public  static   String is_driver,is_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        pDialog = new ProgressDialog(Dashboard.this);
        pDialog.setCancelable(false);
       if (login.id==null){
            USERID=  getIntent().getStringExtra("id");
        }
       else
       {
           USERID=  login.id;
       }
        setItemSelectedListener(bottom_navigation);
    }


    private void setItemSelectedListener(BottomNavigationView bottom_navigation) {
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {



                    case R.id.action_Rides:
                        if (!item.isChecked())
                            Toast.makeText(Dashboard.this, "Messages", Toast.LENGTH_LONG).show();
                       // addFragment(RidesFragment.newInstance());
                        new rideCall().execute();

                        //  addFragment(HomeFragment.newInstance());
                        return true;
//                    case R.id.action_Rides:
//                        if (!item.isChecked())
//                          //  addFragment(ProfileFragment.newInstance());
//                       // {
////                            Fragment fr = new RidesFragment();
////                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
////
////                        transaction.add(R.id.container, fr).commit();
////                        //{
//                            // new rideCall().execute();
//                           // Toast.makeText(Dashboard.this, "Action Rides", Toast.LENGTH_LONG).show();
//
//                    //   }
//
//
//                        return true;

                    case R.id.action_message:
                        if (!item.isChecked())
                            Toast.makeText(Dashboard.this, "Notifications", Toast.LENGTH_LONG).show();
                           addFragment(notificationFragment.newInstance());


                        // addFragment(RatingsFragment.newInstance());
                        return true;

                    case R.id.action_Profile:
                        if (!item.isChecked())
                            Toast.makeText(Dashboard.this, "Profile", Toast.LENGTH_LONG).show();


                  // addFragment(ProfileFragment.newInstance());
                        addFragment(DetailsFragment.newInstance());
                        return true;

//                    case R.id.action_rating:
//                        if (!item.isChecked())
//                            Toast.makeText(Dashboard.this, "Profile", Toast.LENGTH_LONG).show();
//
//
//                        // addFragment(ProfileFragment.newInstance());
//                        addFragment(DetailsFragment.newInstance());
//                        return true;
                }
                return false;
            }
        });
    }
    public void addFragment(final Fragment fragment) {
       // getSupportFragmentManager().beginTransaction().addToBackStack("tag").add(R.id.container, fragment).commit();
       // getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("my_fragment").commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {

        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                            moveTaskToBack(true);
                            Dashboard.super.onBackPressed();
                        }
                    }).create().show();
         //  finish();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                          Dashboard.super.onBackPressed();
                        }
                    }).create().show();
//            finishA();
// super.onBackPressed();
        }



//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//        }
//        else if (getSupportFragmentManager().getBackStackEntryCount()==0){
////            super.onBackPressed();
//            finish();
//          //
//        }
//        else{
//            finish();
//        }

    }
    private class rideCall extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Dashboard.this);
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
                HttpPost httpPost=new HttpPost(Urls.findPendingRides);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("user_id", USERID));
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
            pdLoading.dismiss();


            pDialog.dismiss();
            try{
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject!=null){
                    Log.d("result===","inside");

                    if(jsonObject.getString("error").contains("false")) {
//                        String dataObj = (String) jsonObject.get("data").toString();
//                        Log.d("result==Updated", "data: "+dataObj);
                      Log.d("result===", "inside false");

                        is_driver = jsonObject.getString("is_driver");
                        is_user = jsonObject.getString("is_user");

                        Log.d("result==", "data: "+jsonObject.get("data"));

                        if (is_user.contains("0") && is_driver.contains("0"))
                        {
                            is_review = jsonObject.getString("is_review");
                            if(is_review.contains("false") )
                            {
                                JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                                _id = jsonObject2.getString("_id");
                                status =jsonObject2.getString("status");
                                driver_id=jsonObject2.getString("user_id");

                                // user_id=jsonObject.getString("user_id");
                                dearture_city = jsonObject2.getString("dearture_city");
                                seats = jsonObject2.getString("seats");
                                departure_time = jsonObject2.getString("departure_time");
                                name = jsonObject2.getString("name");
                                Log.d("result===0", "User and Driver with pending review");
                                ShowDialog();
                            }
                            else{
                                Log.d("result===0", "user and driver ");

                                addFragment(RidesFragment.newInstance());
                            }

//                            if(jsonObject.getString("status").contains("review")&&(!jsonObject.getString("user_id").equals(login.id))){
//                                ShowDialog();
//                            }
//                            else{
//                                addFragment(RidesFragment.newInstance());
//                            }

                        }
                        else{
                            JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                            _id = jsonObject2.getString("_id");
                            status =jsonObject2.getString("status");
                            driver_id=jsonObject2.getString("user_id");

                            // user_id=jsonObject.getString("user_id");
                            dearture_city = jsonObject2.getString("dearture_city");
                            seats = jsonObject2.getString("seats");
                            departure_time = jsonObject2.getString("departure_time");
                            name = jsonObject2.getString("name");
                            Log.d("result===1111", "" + is_driver + is_user + dearture_city + departure_time + _id + seats);
                            JSONArray jsonArray1 = jsonObject2.getJSONArray("reserved_users");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                user_id = jsonObject1.getString("user_id");
                                Name = jsonObject1.getString("name");
                                booking_status = jsonObject1.getString("booking_status");
                                reservedUserUtils r = new reservedUserUtils(user_id, Name, booking_status);
                                reservedList.add(r);
                            }

                            if (is_driver.contains("1"))
                            {
                                Log.d("result===1", "driver");

                                // getSupportFragmentManager().beginTransaction().addToBackStack("tag").add(R.id.container, CurrentRides.newInstance()).commit();
                                Fragment fr = new CurrentRides();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                Bundle args = new Bundle();
                                args.putString("destination", dearture_city);
                                args.putString("seats", seats);

                                args.putString("dateTime", departure_time);
                                //args.putSerializable("valuesArray", reservedList);
                                args.putString("rides_id", _id);

                                fr.setArguments(args);
                                transaction.add(R.id.container, fr).addToBackStack("book").commit();
                            }
//                            else if (is_user.contains("1")) {
//                                Log.d("result===1", "user");
//
//                                //  getSupportFragmentManager().beginTransaction().addToBackStack("tag").add(R.id.container, AvailableRides.newInstance()).commit();
//                                Fragment fr = new bookedRide();
//                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                                Bundle args = new Bundle();
//                                args.putString("name", name);
//                                args.putString("rides_id", _id);
//                                args.putString("departure_time", departure_time);
//                                args.putString("dearture_city", dearture_city);
//                                args.putString("booking_status", booking_status);
//
//
//                                fr.setArguments(args);
//                                transaction.add(R.id.container, fr).addToBackStack("fragment").commit();
//                            }

                            else if (is_user.contains("1")) {
                                Log.d("result===1", "user");

                                //  getSupportFragmentManager().beginTransaction().addToBackStack("tag").add(R.id.container, AvailableRides.newInstance()).commit();
                                Fragment fr = new bookedRide();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                Bundle args = new Bundle();
                                args.putString("name", name);
                                args.putString("rides_id", _id);
                                args.putString("departure_time", departure_time);
                                args.putString("dearture_city", dearture_city);
                                args.putString("booking_status", booking_status);


                                fr.setArguments(args);
                                transaction.add(R.id.container, fr).addToBackStack("fragment").commit();
                            }

//                            else if (is_user.contains("1")&& status.contains("review")) {
//                                new checkReviewCall().execute(_id);
//                            }
                        }

//                        if (!dataObj.equalsIgnoreCase("null"))
//                        {
//                            JSONObject jsonObject2 = jsonObject.getJSONObject("data");
//
//                            _id = jsonObject2.getString("_id");
//                            // user_id=jsonObject.getString("user_id");
//                            dearture_city = jsonObject2.getString("dearture_city");
//                            seats = jsonObject2.getString("seats");
//                            departure_time = jsonObject2.getString("departure_time");
//                            name = jsonObject2.getString("name");
//                            Log.d("result===1111", "" + is_driver + is_user + dearture_city + departure_time + _id + seats);
//
//                            JSONArray jsonArray1 = jsonObject2.getJSONArray("reserved_users");
//                            for (int i = 0; i < jsonArray1.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
//                                user_id = jsonObject1.getString("user_id");
//                                Name = jsonObject1.getString("name");
//                                booking_status = jsonObject1.getString("booking_status");
//                                reservedUserUtils r = new reservedUserUtils(user_id, Name, booking_status);
//                                reservedList.add(r);
//                            }
//                        }
//                        else if(dataObj.equalsIgnoreCase("null")){
//
//
//
//                        // }
//                        if (is_driver.contains("1"))
//                        {
//                            Log.d("result===1", "driver");
//
//                            // getSupportFragmentManager().beginTransaction().addToBackStack("tag").add(R.id.container, CurrentRides.newInstance()).commit();
//                            Fragment fr = new CurrentRides();
//                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                            Bundle args = new Bundle();
//                            args.putString("destination", dearture_city);
//                            args.putString("dateTime", departure_time);
//                            //args.putSerializable("valuesArray", reservedList);
//                            args.putString("rides_id", _id);
//
//                            fr.setArguments(args);
//                            transaction.add(R.id.container, fr).commit();
//                        }
//                        else if (is_user.contains("1")) {
//                            Log.d("result===1", "user");
//
//                            //  getSupportFragmentManager().beginTransaction().addToBackStack("tag").add(R.id.container, AvailableRides.newInstance()).commit();
//                            Fragment fr = new bookedRide();
//                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                            Bundle args = new Bundle();
//                            args.putString("name", name);
//                            args.putString("departure_time", departure_time);
//                            args.putString("dearture_city", dearture_city);
//
//                            fr.setArguments(args);
//                            transaction.add(R.id.container, fr).commit();
//                        }
//                        else if (is_user.contains("0") && is_driver.contains("0")) {
//                            Log.d("result===0", "user and driver");
//
//                            addFragment(RidesFragment.newInstance());
//
//                        }
//                    }
                    }
                    else{
                        Toast.makeText(Dashboard.this, "Error", Toast.LENGTH_LONG).show();

                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public void ShowDialog()
    {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);

        LinearLayout linearLayout = new LinearLayout(this);
        final RatingBar rating = new RatingBar(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT

        );
        lp.setMargins(40, 20, 0, 0);

        rating.setLayoutParams(lp);
        rating.setNumStars(5);
        rating.setStepSize(1);


        //add ratingBar to linearLayout
        linearLayout.addView(rating);


        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("Add Rating for "+name);

        //add linearLayout to dailog
        popDialog.setView(linearLayout);



        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                System.out.println("Rated val:"+v);
            }
        });



        // Button OK
        popDialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //  textView.setText(String.valueOf(rating.getProgress()));
                        new reviewAddCall().execute(String.valueOf(rating.getRating()));
                        dialog.dismiss();

                        addFragment(RidesFragment.newInstance());

                    }

                })

                // Button Cancel
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        popDialog.create();
        popDialog.show();

    }
    private class reviewAddCall extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Dashboard.this);
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
                HttpPost httpPost=new HttpPost(Urls.addReview);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("rider_User_id", login.id));
                nameValuePairs.add(new BasicNameValuePair("rider_name", login.name));
                nameValuePairs.add(new BasicNameValuePair("review", params[0]));
                nameValuePairs.add(new BasicNameValuePair("driver_id", driver_id));
                nameValuePairs.add(new BasicNameValuePair("ride_id", _id));
                nameValuePairs.add(new BasicNameValuePair("driver_name", name));

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
            pdLoading.dismiss();
            pDialog.dismiss();
            try{

            if(result.contains("Successfull")){
                Toast.makeText(Dashboard.this, "Review Added Successfully", Toast.LENGTH_LONG).show();

            }
            else{

            }

            }catch (Exception e){
                pdLoading.dismiss();
                pDialog.dismiss();
                e.printStackTrace();
            }
        }
    }


}

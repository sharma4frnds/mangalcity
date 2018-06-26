package net.clamour.mangalcity.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.clamour.mangalcity.Home.DrawerBaseActivity;
import net.clamour.mangalcity.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends DrawerBaseActivity {

    String gender[] = {"Select Gender", "Male", "Female"};
    String marital[] = {"Select Marrital Status", "Single", "Married"};
    @BindView(R.id.mobile_pro)
    EditText mobilePro;
    @BindView(R.id.email_pro)
    EditText emailPro;
    @BindView(R.id.fulladdress_pro)
    EditText fulladdressPro;
    @BindView(R.id.proffession_pro)
    EditText proffessionPro;
    @BindView(R.id.gender_spinner)
    Spinner genderSpinner;
    @BindView(R.id.marital_spinner)
    Spinner maritalSpinner;
    @BindView(R.id.dob_pro)
    EditText dobPro;



    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "UserProfile";

    Uri selectedImageUri;
    Bitmap bitmap;
    String path;


    String country[] = {"India"};
    String state[] = {"Select State", "Karnatka", "Sikkim", "Kerala", "Uttar Pradesh"};
    String district[] = {"Select District", "Mathura", "Firozabad", "Agra"};
    String city[] = {"Select City", "Faridpuri", "Baheri"};
    @BindView(R.id.country_spinner)
    Spinner countrySpinner;
    @BindView(R.id.state_spinner)
    Spinner stateSpinner;
    @BindView(R.id.district_spinner)
    Spinner districtSpinner;
    @BindView(R.id.block_spinner)
    Spinner blockSpinner;
    @BindView(R.id.country_spinner_home)
    Spinner countrySpinnerHome;
    @BindView(R.id.state_spinner_home)
    Spinner stateSpinnerHome;
    @BindView(R.id.district_spinner_home)
    Spinner districtSpinnerHome;
    @BindView(R.id.block_spinner_home)
    Spinner blockSpinnerHome;
    @BindView(R.id.person_profile_image)
    CircleImageView personProfileImage;
    @BindView(R.id.camera_icon)
    ImageView cameraIcon;


    ArrayList<String> Country_array;
    ArrayList<ModalClassName>State_array;
    ArrayList<ModalClassName>District_array;
    ArrayList<String>City_array;

    ArrayList<CountryDataStorage>countryarray_modal;
    ArrayList<CountryDataStorage>statearray_modal;
    ArrayList<CountryDataStorage>districtarray_modal;
    ArrayList<CountryDataStorage>cityarray_modal;

    String country_name,country_id,state_name,state_id,district_name,district_id,city_name,city_id;
    ProgressDialog pDialog;
    String firstname_st,lastname_st,mobile_st,email_st,fulladdress_st,proffession_st,male_st,marital_st,dob_st;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN

        );

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar1.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar1.setTitle("Profile");
        setSupportActionBar(toolbar1);
        setDrawer();


        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captureImage();
            }
        });

        State_array=new ArrayList<>();
        statearray_modal=new ArrayList<>();
        District_array=new ArrayList<>();
        districtarray_modal=new ArrayList<>();

        showStateData();
        showDistrictData();




    /* Get the real path from the URI */


        GenderAdapter customAdapter = new GenderAdapter(getApplicationContext(), gender);
        genderSpinner.setAdapter(customAdapter);
//
        GenderAdapter customAdapter1 = new GenderAdapter(getApplicationContext(), marital);
        maritalSpinner.setAdapter(customAdapter1);
//
        GenderAdapter customAdapter_country = new GenderAdapter(getApplicationContext(), country);
        countrySpinner.setAdapter(customAdapter_country);
//
////        CustomAdapterSpinner customAdapter_state = new CustomAdapterSpinner(getApplicationContext(), state);
////        stateSpinner.setAdapter(customAdapter_state);
//
//        CustomAdapterSpinner customAdapter2 = new CustomAdapterSpinner(getApplicationContext(), district);
//        districtSpinner.setAdapter(customAdapter2);
//
//        CustomAdapterSpinner customAdapter3 = new CustomAdapterSpinner(getApplicationContext(), city);
//        blockSpinner.setAdapter(customAdapter3);
//
//
//        CustomAdapterSpinner customAdapter_countryhome = new CustomAdapterSpinner(getApplicationContext(), country);
//        countrySpinnerHome.setAdapter(customAdapter_countryhome);
//
////        CustomAdapterSpinner customAdapter_statehome = new CustomAdapterSpinner(getApplicationContext(), state);
////        stateSpinnerHome.setAdapter(customAdapter_statehome);
//
//        CustomAdapterSpinner customAdapter2home = new CustomAdapterSpinner(getApplicationContext(), district);
//        districtSpinnerHome.setAdapter(customAdapter2home);
//
//        CustomAdapterSpinner customAdapter3home = new CustomAdapterSpinner(getApplicationContext(), city);
//        blockSpinnerHome.setAdapter(customAdapter3home);






    }
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    public void captureImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    path = getPathFromURI(selectedImageUri);
                    Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView
                    // profile_imagel.setImageURI(selectedImageUri);

                    try {
                        //getting bitmap object from uri
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                        // profile_prefrence.edit().remove("guset_profileimage").apply();

                        //displaying selected image to imageview
                        personProfileImage.setImageBitmap(bitmap);

                        //calling the method uploadBitmap to upload image

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public void setStateSpinner() {
        Log.i("setspinner", "setspinner");
//        State_spinner.notify();
        CustomAdapterSpinner customAdapter_state = new CustomAdapterSpinner(UserProfile.this,State_array);
        stateSpinner.setAdapter(customAdapter_state);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                state_id = statearray_modal.get(position).getState_id();
                state_name = statearray_modal.get(position).getState_name();
              //  Log.i("state_name", state_name);
             //   Log.i("state_id", state_id);
                District_array.clear();
              //  showDistrictData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void showStateData() {

        pDialog = new ProgressDialog(UserProfile.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);

        Log.i("instatedata", "instatedata");

        // pDialog.show();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/getstate",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG).show();

                        Log.i("responsestatelist", response);


                        //   arrayList=new ArrayList<>();

                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        Log.e("response=", response);


                        try {


                            JSONObject jsonObject=new JSONObject(response);
                            String sts=jsonObject.getString("success");
                            Log.d(TAG, "onResponse: "+sts);
                            String res=jsonObject.getString("data");
                            Log.d(TAG, "onResponse: "+res);

                            JSONArray jsonArray = new JSONArray(res);

                            for (int i = 0; i <= jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CountryDataStorage countryDataStorage = new CountryDataStorage();
                                ModalClassName modalClassName=new ModalClassName();

                                state_name=jsonObject1.getString("name");
                                Log.i("statename",state_name);
                               state_id=jsonObject1.getString("id");
                               Log.i("state_id",state_id);

                                countryDataStorage.setState_id(state_id);
                                modalClassName.setStatename(state_name);
                                State_array.add(modalClassName);
                                statearray_modal.add(countryDataStorage);




                            }





                        } catch (Exception e) {
                        }

                        setStateSpinner();


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(JobDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.i("errorr", error.toString());
                    }
                })


        {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjU0LCJpc3MiOiJodHRwOi8vZW1lcmdpbmduY3IuY29tL21hbmdhbGNpdHkvYXBpL2F1dGgvbG9naW4iLCJpYXQiOjE1Mjk0MTU2MDIsImV4cCI6MTUyOTQxOTIwMiwibmJmIjoxNTI5NDE1NjAyLCJqdGkiOiJsODBxZGRlTXhCdTE0RnFHIn0.L59KGWq23VgB0rnXCYJIO95Xa2JTL-uvJwK1t-3Qzt0");


                return params;
            }

        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);


    }

 public void showDistrictData(){
     pDialog = new ProgressDialog(UserProfile.this);
     pDialog.setMessage("Please wait...");
     pDialog.setCancelable(true);

     Log.i("instatedata", "instatedata");

     // pDialog.show();

     StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/getdistict",
             new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {
                     //Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG).show();

                     Log.i("responsedistrictlist", response);


                     //   arrayList=new ArrayList<>();

                     if (pDialog.isShowing())
                         pDialog.dismiss();
                     Log.e("response=", response);
//                        Intent intent=new Intent(GuestRegistration.this,OtpScreen.class);
//                        intent.putExtra("mobile_no",mobile_no_st);
//                        startActivity(intent);


                     try {


                         JSONObject jsonObject=new JSONObject(response);
                         String sts=jsonObject.getString("success");
                         Log.d(TAG, "onResponse: "+sts);
                         String res=jsonObject.getString("data");
                         Log.d(TAG, "onResponse: "+res);

                         JSONArray jsonArray = new JSONArray(res);

                         for (int i = 0; i <= jsonArray.length(); i++) {
                             JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                             CountryDataStorage countryDataStorage = new CountryDataStorage();
                             ModalClassName modalClassName=new ModalClassName();

                             district_name=jsonObject1.getString("name");
                             Log.i("statename",district_name);
                             district_id=jsonObject1.getString("id");
                             Log.i("state_id",district_id);

                             countryDataStorage.setDistrict_id(district_id);
                             modalClassName.setDistrictname(district_name);
                             District_array.add(modalClassName);
                             statearray_modal.add(countryDataStorage);




                         }


                     } catch (Exception e) {
                     }

                     setDistrictSpinner();


                 }

             },
             new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {
                     //Toast.makeText(JobDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                     Log.i("errorr", error.toString());
                 }
             })

     {


         @Override
         public Map<String, String> getParams() {
             Map<String, String> params = new HashMap<String, String>();
             params.put("id", "10");
             params.put("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjU0LCJpc3MiOiJodHRwOi8vZW1lcmdpbmduY3IuY29tL21hbmdhbGNpdHkvYXBpL2F1dGgvbG9naW4iLCJpYXQiOjE1Mjk0MTU2MDIsImV4cCI6MTUyOTQxOTIwMiwibmJmIjoxNTI5NDE1NjAyLCJqdGkiOiJsODBxZGRlTXhCdTE0RnFHIn0.L59KGWq23VgB0rnXCYJIO95Xa2JTL-uvJwK1t-3Qzt0");


             return params;
         }

     };

     RequestQueue requestQueue1 = Volley.newRequestQueue(this);
     requestQueue1.add(stringRequest1);

 }

public void setDistrictSpinner(){

    DistrictAdapter customAdapter_state = new DistrictAdapter(UserProfile.this,District_array);
    stateSpinner.setAdapter(customAdapter_state);

    stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//            state_id = statearray_modal.get(position).getState_id();
//            state_name = statearray_modal.get(position).getCountry_name();
//            Log.i("state_name", state_name);
//            Log.i("state_id", state_id);
//            City_array.clear();
//            showCityData();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });


}

public void showCityData(){
    pDialog = new ProgressDialog(UserProfile.this);
    pDialog.setMessage("Please wait...");
    pDialog.setCancelable(true);

    Log.i("instatedata", "instatedata");

    // pDialog.show();

    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/getdistict",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG).show();

                    Log.i("responsedistrictlist", response);


                    //   arrayList=new ArrayList<>();

                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    Log.e("response=", response);
//                        Intent intent=new Intent(GuestRegistration.this,OtpScreen.class);
//                        intent.putExtra("mobile_no",mobile_no_st);
//                        startActivity(intent);


                    try {


                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            CountryDataStorage countryDataStorage = new CountryDataStorage();

                            district_name = jsonObject.getString("name");
                            Log.i("state_name", state_name);
                            state_id = jsonObject.getString("id");
                            countryDataStorage.setState_id(jsonObject.getString("id"));
                          //  State_array.add(state_name);
                            statearray_modal.add(countryDataStorage);


                        }


                    } catch (Exception e) {
                    }

                    setCitySpinner();


                }

            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(JobDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                    Log.i("errorr", error.toString());
                }
            })

    {


        @Override
        public Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("country_id", country_id);


            return params;
        }

    };

    RequestQueue requestQueue1 = Volley.newRequestQueue(this);
    requestQueue1.add(stringRequest1);


}
public void setCitySpinner(){
   // stateSpinner.setAdapter(new ArrayAdapter<String>(UserProfile.this, android.R.layout.simple_spinner_dropdown_item, State_array));

    stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            state_id = statearray_modal.get(position).getState_id();
            state_name = statearray_modal.get(position).getCountry_name();
            Log.i("state_name", state_name);
            Log.i("state_id", state_id);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });


}

}

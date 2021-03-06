package net.clamour.mangalcity.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soundcloud.android.crop.Crop;
import com.theartofdev.edmodo.cropper.CropImage;

import net.clamour.mangalcity.Home.DrawerBaseActivity;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.webservice.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

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
    @BindView(R.id.coverImage)
    ImageView CoverImage;
    @BindView(R.id.editText_name)
    EditText editText_name;
    @BindView(R.id.editText_last)
    EditText editText_last;


    @BindView(R.id.linear_current)
    LinearLayout layout_homelocation;


    private static final int SELECT_PICTURE_PROFILE = 100;
    private static final int SELECT_PICTURE_COVER = 101;

    private static final String TAG = "UserProfile";

    Uri selectedImageUri;
    Bitmap bitmap;
    String profilepath, path;
    Boolean isSucessprofile, isSucesscover;


    String countryHome[] = {"India"};
    String countryCurrent[] = {"India"};

    // For Current Location

    @BindView(R.id.country_spinnercurrentLocation)
    Spinner countrySpinnerCurrentLocation;

    @BindView(R.id.state_spinnercurrentLocation)
    Spinner stateSpinnercurrentLocation;

    @BindView(R.id.district_spinnercurrentLocation)
    Spinner districtSpinnercurrentLocation;

    @BindView(R.id.block_spinnercurrentLocation)
    Spinner blockSpinnercurrentLocation;

    //For Home Location


    @BindView(R.id.country_spinner_home)
    Spinner countrySpinnerHome;

    @BindView(R.id.state_spinner_home)
    Spinner stateSpinnerHome;

    @BindView(R.id.district_spinner_home)
    Spinner districtSpinnerHome;

    @BindView(R.id.block_spinner_home)
    Spinner blockSpinnerHome;


    @BindView(R.id.person_profile_image)
    ImageView personProfileImage;
    @BindView(R.id.profile_update)
    Button profile_update;

    @BindView(R.id.checkbox_home)
    CheckBox checkBox;

    ApiInterface apiInterface;


    ArrayList<ModalClassName> State_array;
    ArrayList<ModalClassName> District_array;
    ArrayList<ModalClassName> Block_array;

    ArrayList<CountryDataStorage> countryarray_modal;
    ArrayList<CountryDataStorage> statearray_modal;
    ArrayList<CountryDataStorage> districtarray_modal;
    ArrayList<CountryDataStorage> Block_ArrayModal;

    SharedPreferences LoginPrefrences;
    SharedPreferences profileprefrences;
    String UserToken;
    Boolean isSucessProfileUpdate;

    String firstname_get, lastname_get, mobile_get, dob_get, emailid_get, fulladdress_get, profession_get, gender_get, marital_status_get, statecurrent_get, districtcurrent_get, city_current_get, profileimage_get, coverImageget, currentlocationStatus, mobilehiddenget, dobhiddenget;


    String country_name, country_id, state_name, state_id, district_name, district_id, city_name, city_id;
    ProgressDialog pDialog;
    String firstname_st, lastname_st, mobile_st, email_st, fulladdress_st, proffession_st, male_st, marital_st, dob_st;
    Boolean isSucessget;
    String hiddenmobile_st,hiddendob_set;
    // @BindView(R.id.camera_cover)
    // ImageView cameraCover;
    //@BindView(R.id.profile_camera)
    // ImageView profileCamera;

    String current_location, location_checked_get;
    @BindView(R.id.image_mob)
    ImageView imageMob;
    @BindView(R.id.image_email)
    ImageView imageEmail;
    @BindView(R.id.image_address)
    ImageView imageAddress;
    @BindView(R.id.image_proffession)
    ImageView imageProffession;
    @BindView(R.id.image_dob)
    ImageView imageDob;
    @BindView(R.id.checkbox_mobile)
    CheckBox checkboxMobile;
    @BindView(R.id.checkbox_dob)
    CheckBox checkboxDob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar1.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar1.setTitle("Profile");
        setSupportActionBar(toolbar1);
        setDrawer();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        UserToken = LoginPrefrences.getString("userToken", "");
        Log.i("token", UserToken);
        profileprefrences= this.getSharedPreferences("net.clamour.mangalcity.profile.UserProfile", MODE_PRIVATE);


        getProfileData();

        imageAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fulladdressPro.requestFocus();
                fulladdressPro.setSelection(fulladdressPro.getText().length());
                fulladdressPro.setCursorVisible(true);
                fulladdressPro.setFocusableInTouchMode(true);

            }
        });
        imageDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dobPro.requestFocus();
                dobPro.setSelection(dobPro.getText().length());
                dobPro.setCursorVisible(true);
                dobPro.setFocusableInTouchMode(true);

            }
        });
        imageMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mobilePro.requestFocus();
                mobilePro.setSelection(mobilePro.getText().length());
                mobilePro.setCursorVisible(true);
                mobilePro.setFocusableInTouchMode(true);
            }
        });
        imageEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailPro.requestFocus();
                emailPro.setSelection(emailPro.getText().length());
                emailPro.setCursorVisible(true);
                emailPro.setFocusableInTouchMode(true);

            }
        });
        imageProffession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                proffessionPro.requestFocus();
                proffessionPro.setSelection(proffessionPro.getText().length());
                proffessionPro.setCursorVisible(true);
                proffessionPro.setFocusableInTouchMode(true);
            }
        });

        personProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });


        State_array = new ArrayList<>();
        statearray_modal = new ArrayList<>();
        District_array = new ArrayList<>();
        districtarray_modal = new ArrayList<>();
        Block_array = new ArrayList<>();
        Block_ArrayModal = new ArrayList<>();
        showStateDataCurrentLocation();
        showStateDataHomeLocation();


//        profile_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //updateProfile();
//            }
//        });

        CoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCover();
            }
        });

    /* Get the real path from the URI */


        GenderAdapter customAdapter = new GenderAdapter(getApplicationContext(), gender);
        genderSpinner.setAdapter(customAdapter);
//
        GenderAdapter customAdapter1 = new GenderAdapter(getApplicationContext(), marital);
        maritalSpinner.setAdapter(customAdapter1);
//
        GenderAdapter customAdapter_countryHome = new GenderAdapter(getApplicationContext(), countryHome);
        countrySpinnerHome.setAdapter(customAdapter_countryHome);


        GenderAdapter customAdapter_countryCurrent = new GenderAdapter(getApplicationContext(), countryCurrent);
        countrySpinnerCurrentLocation.setAdapter(customAdapter_countryCurrent);

        profile_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((!checkBox.isChecked())&&(!checkboxDob.isChecked())&&(!checkboxMobile.isChecked())) {
                    saveProfileHome();
                }
                else if ((checkBox.isChecked())&& (checkboxDob.isChecked())&&(checkboxMobile.isChecked())) {
                    saveProfile();

                }
                else if((!checkBox.isChecked())&& (checkboxDob.isChecked())&&(checkboxMobile.isChecked())){
                    saveProfile();
                }
                else if((checkBox.isChecked())&& (!checkboxDob.isChecked())&&(!checkboxMobile.isChecked())){
                    saveProfile();
                }

            }
        });


    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        //  checkBox = (CheckBox) v;
        // checkBox.setChecked(true);
        if (!checkBox.isChecked()) {

            // layout_homelocation.setVisibility(View.VISIBLE);
            layout_homelocation.getLayoutParams().height = 500;
            //   ll.getLayoutParams().width = 200;
            layout_homelocation.requestLayout();
            current_location = "inactive";


        } else if (checkBox.isChecked()) {

            // layout_homelocation.setVisibility(View.INVISIBLE);
            layout_homelocation.getLayoutParams().height = 0;
            //   ll.getLayoutParams().width = 200;
            layout_homelocation.requestLayout();
            current_location = "active";

            //
            // showStateDataHomeLocation();

        }
    }

    public void itemClickedmobile(View v){

        if(!checkboxMobile.isChecked()){
            hiddenmobile_st="0";

        }
        else if(checkboxMobile.isChecked()){
            hiddenmobile_st="1";
        }


    }

    public void itemClickeddob(View v){
        if(!checkboxDob.isChecked()){
            hiddendob_set="0";

        }
        else if(checkboxDob.isChecked()){
            hiddendob_set="1";
        }

    }


    public void captureImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_PROFILE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_PICTURE_PROFILE:
                if (resultCode == Activity.RESULT_OK) {
                    // Get the url from data
                    selectedImageUri = data.getData();


                    if (null != selectedImageUri) {
                        // Get the path from the Uri
                        profilepath = getPathFromURI(selectedImageUri);
                        Log.i(TAG, "Image Path : " + profilepath);

                        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));

                        Crop.of(selectedImageUri, destination).asSquare().start(this);

                        // Set the image in ImageView
                        // profile_imagel.setImageURI(selectedImageUri);
//                        CropImage.activity(selectedImageUri)
//                                .setAspectRatio(1, 1)
//                                .setMinCropWindowSize(255, 150)
//                                .setMaxCropResultSize(255, 150)
//                                .start(this);

                        try {
                            //getting bitmap object from uri
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            changeProfileImageServer();


                            // profile_prefrence.edit().remove("guset_profileimage").apply();

                            //displaying selected image to imageview
                              personProfileImage.setImageBitmap(bitmap);

                            //calling the method uploadBitmap to upload image

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                break;
            case SELECT_PICTURE_COVER:
                if (resultCode == Activity.RESULT_OK) {

                    selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        // Get the path from the Uri
                        path = getPathFromURI(selectedImageUri);
                        Log.i(TAG, "Image Path : " + path);


                        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));

                        if(destination==null){

                        }
                        else {
                            Crop.of(selectedImageUri, destination).asSquare().start(this);
                        }
//                        CropImage.activity(selectedImageUri)
//                                .setAspectRatio(1, 1)
//                                .setMinCropWindowSize(1000, 1000)
//                                .setMaxZoom(1000)
//                                .setMaxCropResultSize(1000, 1000)
//                                .start(this);


                        // Set the image in ImageView
                        // profile_imagel.setImageURI(selectedImageUri);


                        try {
                            //getting bitmap object from uri
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            changeCoverImageServer();


                            // profile_prefrence.edit().remove("guset_profileimage").apply();

                            //displaying selected image to imageview
                              CoverImage.setImageBitmap(bitmap);

                            //calling the method uploadBitmap to upload image

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }


                }
                break;

            case CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {


                    if (profilepath == null && path != null) {
                        Uri mImageUri = result.getUri();

                        CoverImage.setImageURI(mImageUri);

                    }
                    if (profilepath != null && path == null) {
                        Uri mImageUri = result.getUri();
                        personProfileImage.setImageURI(mImageUri);
                    }


                }
        }
    }


    public void showStateDataCurrentLocation() {

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


                            JSONObject jsonObject = new JSONObject(response);
                            String sts = jsonObject.getString("success");
                            Log.d(TAG, "onResponse: " + sts);
                            String res = jsonObject.getString("data");
                            Log.d(TAG, "onResponse: " + res);

                            JSONArray jsonArray = new JSONArray(res);

                            for (int i = 0; i <= jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CountryDataStorage countryDataStorage = new CountryDataStorage();
                                ModalClassName modalClassName = new ModalClassName();

                                state_name = jsonObject1.getString("name");
                                Log.i("statename", state_name);
                                state_id = jsonObject1.getString("id");
                                Log.i("state_id", state_id);

                                countryDataStorage.setState_id(state_id);
                                modalClassName.setStatename(state_name);
                                State_array.add(modalClassName);
                                statearray_modal.add(countryDataStorage);

                            }


                        } catch (Exception e) {
                        }

                        setStateCurrentLocationSpinner();


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
                params.put("token", UserToken);


                return params;
            }

        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);


    }


    public void setStateCurrentLocationSpinner() {
        Log.i("setspinner", "setspinner");

        CustomAdapterSpinner customAdapter_state = new CustomAdapterSpinner(UserProfile.this, State_array);
        stateSpinnercurrentLocation.setAdapter(customAdapter_state);

        stateSpinnercurrentLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                state_id = statearray_modal.get(position).getState_id();
                state_name = statearray_modal.get(position).getState_name();
                //  Log.i("state_name", state_name);
                Log.i("state_idselected", state_id);
                District_array.clear();
                showDistrictDataCurrentLocation();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void showDistrictDataCurrentLocation() {
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


                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            String sts = jsonObject.getString("success");
                            Log.d(TAG, "onResponse: " + sts);
                            String res = jsonObject.getString("data");
                            Log.d(TAG, "onResponse: " + res);

                            JSONArray jsonArray = new JSONArray(res);

                            for (int i = 0; i <= jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CountryDataStorage countryDataStorage = new CountryDataStorage();
                                ModalClassName modalClassName = new ModalClassName();

                                district_name = jsonObject1.getString("name");
                                Log.i("districtname", district_name);
                                district_id = jsonObject1.getString("id");
                                Log.i("district_id", district_id);

                                countryDataStorage.setDistrict_id(district_id);
                                modalClassName.setDistrictname(district_name);
                                District_array.add(modalClassName);
                                districtarray_modal.add(countryDataStorage);


                            }


                        } catch (Exception e) {
                        }

                        setDistrictcurrentSpinner();


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
                params.put("id", state_id);
                params.put("token", UserToken);


                return params;
            }

        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);

    }

    public void setBlockcurrentSpinner() {

        CityAdapter customAdapter_state = new CityAdapter(UserProfile.this, Block_array);
        blockSpinnercurrentLocation.setAdapter(customAdapter_state);

        blockSpinnercurrentLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                city_id = Block_ArrayModal.get(position).getCity_id();
                //              city_name = Block_ArrayModal.get(position).getCity_name();
                //  Log.i("state_name", state_name);
                //  Log.i("ci", city_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void setBlockHomeSpinner() {

        CityAdapter customAdapter_state = new CityAdapter(UserProfile.this, Block_array);
        blockSpinnerHome.setAdapter(customAdapter_state);

        blockSpinnerHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                city_id = Block_ArrayModal.get(position).getCity_id();
                //              city_name = Block_ArrayModal.get(position).getCity_name();
                //  Log.i("state_name", state_name);
                //  Log.i("ci", city_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void setDistrictcurrentSpinner() {


        DistrictAdapter customAdapter_state = new DistrictAdapter(UserProfile.this, District_array);
        districtSpinnercurrentLocation.setAdapter(customAdapter_state);

        districtSpinnercurrentLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                district_id = districtarray_modal.get(position).getDistrict_id();
                district_name = districtarray_modal.get(position).getDistrict_name();
                //  Log.i("state_name", state_name);
                Log.i("district_idselecteddd", district_id + "" + district_name);
                Block_array.clear();
                showBlockDataCurrentLocation();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void showBlockDataCurrentLocation() {

        pDialog = new ProgressDialog(UserProfile.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);

        Log.i("instatedata", "instatedata");

        // pDialog.show();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/getcity",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG).show();

                        Log.i("responseBLOCK", response);


                        //   arrayList=new ArrayList<>();

                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        Log.e("response=", response);


                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            String sts = jsonObject.getString("success");
                            Log.d(TAG, "onResponse: " + sts);
                            String res = jsonObject.getString("data");
                            Log.d(TAG, "onResponse: " + res);

                            JSONArray jsonArray = new JSONArray(res);

                            for (int i = 0; i <= jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CountryDataStorage countryDataStorage = new CountryDataStorage();
                                ModalClassName modalClassName = new ModalClassName();

                                city_name = jsonObject1.getString("name");
                                Log.i("city", city_name);
                                city_id = jsonObject1.getString("id");
                                Log.i("city_id", city_id);

                                countryDataStorage.setCity_id(city_id);
                                modalClassName.setCityname(city_name);
                                Block_array.add(modalClassName);
                                Block_ArrayModal.add(countryDataStorage);


                            }


                        } catch (Exception e) {
                        }

                        setBlockcurrentSpinner();


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
                params.put("id", "707");
                params.put("token", UserToken);


                return params;
            }

        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);

    }


    public void showStateDataHomeLocation() {

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


                            JSONObject jsonObject = new JSONObject(response);
                            String sts = jsonObject.getString("success");
                            Log.d(TAG, "onResponse: " + sts);
                            String res = jsonObject.getString("data");
                            Log.d(TAG, "onResponse: " + res);

                            JSONArray jsonArray = new JSONArray(res);

                            for (int i = 0; i <= jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CountryDataStorage countryDataStorage = new CountryDataStorage();
                                ModalClassName modalClassName = new ModalClassName();

                                state_name = jsonObject1.getString("name");
                                Log.i("statename", state_name);
                                state_id = jsonObject1.getString("id");
                                Log.i("state_id", state_id);

                                countryDataStorage.setState_id(state_id);
                                modalClassName.setStatename(state_name);
                                State_array.add(modalClassName);
                                statearray_modal.add(countryDataStorage);

                            }


                        } catch (Exception e) {
                        }

                        setStateHomeLocationSpinner();


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
                params.put("token", UserToken);


                return params;
            }

        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);


    }

    public void setStateHomeLocationSpinner() {

        Log.i("setspinner", "setspinner");

        CustomAdapterSpinner customAdapter_state = new CustomAdapterSpinner(UserProfile.this, State_array);
        stateSpinnerHome.setAdapter(customAdapter_state);

        stateSpinnerHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                state_id = statearray_modal.get(position).getState_id();
                state_name = statearray_modal.get(position).getState_name();
                //  Log.i("state_name", state_name);
                Log.i("state_id", state_id);
                District_array.clear();
                showDistrictDataHomeLocation();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void showDistrictDataHomeLocation() {
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


                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            String sts = jsonObject.getString("success");
                            Log.d(TAG, "onResponse: " + sts);
                            String res = jsonObject.getString("data");
                            Log.d(TAG, "onResponse: " + res);

                            JSONArray jsonArray = new JSONArray(res);

                            for (int i = 0; i <= jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CountryDataStorage countryDataStorage = new CountryDataStorage();
                                ModalClassName modalClassName = new ModalClassName();

                                district_name = jsonObject1.getString("name");
                                Log.i("statename", district_name);
                                district_id = jsonObject1.getString("id");
                                Log.i("state_id", district_id);

                                countryDataStorage.setDistrict_id(district_id);
                                modalClassName.setDistrictname(district_name);
                                District_array.add(modalClassName);
                                districtarray_modal.add(countryDataStorage);


                            }


                        } catch (Exception e) {
                        }

                        setDistrictHomeSpinner();


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
                params.put("id", state_id);
                params.put("token", UserToken);


                return params;
            }

        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);

    }

    public void setDistrictHomeSpinner() {

        DistrictAdapter customAdapter_state = new DistrictAdapter(UserProfile.this, District_array);
        districtSpinnerHome.setAdapter(customAdapter_state);

        districtSpinnerHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                district_id = districtarray_modal.get(position).getDistrict_id();
                district_name = districtarray_modal.get(position).getDistrict_name();
                //  Log.i("state_name", state_name);
                Log.i("district_id", district_id);
                Block_array.clear();
                showBlockDataHomeLocation();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void showBlockDataHomeLocation() {

        pDialog = new ProgressDialog(UserProfile.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);

        Log.i("instatedata", "instatedata");

        // pDialog.show();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/getcity",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG).show();

                        Log.i("responseBLOCK", response);


                        //   arrayList=new ArrayList<>();

                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        Log.e("response=", response);


                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            String sts = jsonObject.getString("success");
                            Log.d(TAG, "onResponse: " + sts);
                            String res = jsonObject.getString("data");
                            Log.d(TAG, "onResponse: " + res);

                            JSONArray jsonArray = new JSONArray(res);

                            for (int i = 0; i <= jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CountryDataStorage countryDataStorage = new CountryDataStorage();
                                ModalClassName modalClassName = new ModalClassName();

                                city_name = jsonObject1.getString("name");
                                Log.i("city", city_name);
                                city_id = jsonObject1.getString("id");
                                Log.i("city_id", city_id);

                                countryDataStorage.setCity_id(city_id);
                                modalClassName.setCityname(city_name);
                                Block_array.add(modalClassName);
                                Block_ArrayModal.add(countryDataStorage);


                            }


                        } catch (Exception e) {
                        }

                        setBlockHomeSpinner();


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
                params.put("id", "707");
                params.put("token", UserToken);


                return params;
            }

        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);

    }

    public void getProfileData() {

        pDialog = new ProgressDialog(UserProfile.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
      //  pDialog.show();


        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/getprofile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG).show();


                        Log.i("responsegetProfile", response);


                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        Log.e("response=", response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            isSucessget = jsonObject.getBoolean("success");

                            location_checked_get = jsonObject.getString("current_location");
                            Log.i("locationchecked", location_checked_get);


                            currentlocationStatus = jsonObject.getString("current_location");


                            JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                            firstname_get = jsonObject1.getString("first_name");
                            Log.d(TAG, "onResponsefirsttt: " + firstname_get);
                            lastname_get = jsonObject1.getString("last_name");
                            mobile_get = jsonObject1.getString("mobile");
                            emailid_get = jsonObject1.getString("email");
                            fulladdress_get = jsonObject1.getString("address");
                            profession_get = jsonObject1.getString("profession");
                            gender_get = jsonObject1.getString("gender");
                            marital_status_get = jsonObject1.getString("marital_status");
                            dob_get = jsonObject1.getString("dob");
                            profileimage_get = jsonObject1.getString("image");
                            coverImageget = jsonObject1.getString("cover_image");
                            Log.d(TAG, "onResponsecover: " + coverImageget);
                            mobilehiddenget = jsonObject1.getString("mobile_hidden");
                            dobhiddenget = jsonObject1.getString("dob_hidden");
                            Log.d(TAG, "onResponsedobbb: "+dobhiddenget);
                            lastname_get=jsonObject1.getString("last_name");


                        } catch (Exception e) {
                        }
                        setData();
                        // saveUpdatedData();


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
                params.put("token", UserToken);


                return params;
            }

        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);


    }

    public void setData() {

        editText_name.setText(firstname_get);
        editText_last.setText(lastname_get);
        mobilePro.setText(mobile_get);
        emailPro.setText(emailid_get);
        fulladdressPro.setText(fulladdress_get);
        dobPro.setText(dob_get);
        proffessionPro.setText(profession_get);

        Glide.with(getApplicationContext()).load("http://emergingncr.com/mangalcity/public/images/user/" + profileimage_get)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(personProfileImage);

        Glide.with(getApplicationContext()).load("http://emergingncr.com/mangalcity/public/images/user/cover/" + coverImageget)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(CoverImage);

        if (location_checked_get.contains("active")) {

            checkBox.setChecked(true);
            // layout_homelocation.setVisibility(View.INVISIBLE);
            layout_homelocation.getLayoutParams().height = 0;
            //   ll.getLayoutParams().width = 200;
            layout_homelocation.requestLayout();
            Log.i("checkkkk", "checkkk");

        } else if (location_checked_get.contains("inactive")) {
            checkBox.setChecked(false);
            //  layout_homelocation.setVisibility(View.VISIBLE);
            layout_homelocation.getLayoutParams().height = 500;
            //   ll.getLayoutParams().width = 200;
            layout_homelocation.requestLayout();

            Log.i("checkkkkinactivee", "checkkkkinactivee");

        }
        //proffessionPro.setText(prof);
        if (mobilehiddenget.contains("0")) {
checkboxMobile.setChecked(false);

        }
        else if(mobilehiddenget.contains("1")){
            checkboxMobile.setChecked(true);
        }

        if (dobhiddenget.contains("0")) {

            checkboxDob.setChecked(false);
        }
        else if(dobhiddenget.contains("1")){
             checkboxDob.setChecked(true);
         }


    }

    public void changeCover() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_COVER);


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


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    public void changeProfileImageServer() {

        pDialog = new ProgressDialog(UserProfile.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        pDialog.show();

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/change_profile_image", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.i("responsereprofileimgeeeeeee", resultResponse);
                pDialog.cancel();
                try {

                    JSONObject jsonObject = new JSONObject(resultResponse);
                    isSucessprofile = jsonObject.getBoolean("success");
                    Log.i("imaggeee", isSucessprofile.toString());

//                    JSONArray jsonArray = new JSONArray(resultResponse);
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        isSucessprofile = jsonObject.getBoolean("success");
//                        Log.i("issucesssss", isSucessprofile.toString());
//                        //   String picture=jsonObject.getString("picture");
//                        // Log.i("picture",picture);

                    // }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if ((isSucessprofile == true)) {

                    Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                  //  getProfileData();


                } else if (isSucessprofile == false) {

                    Toast.makeText(getApplicationContext(), "please Try Again", Toast.LENGTH_SHORT).show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", UserToken);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                //params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                if (bitmap != null) {
                    params.put("image", new DataPart("profileImage.png", getFileDataFromDrawable(bitmap)));
                    Log.i("have", "have");
                } else if (bitmap == null) {
                    //  params.put("picture",new DataPart("profileImage.png",getFileDataFromDrawable(bitmap)));
                    Log.i("Don'thave", "Don'thave");
                }


                // params.put("old_picture",new DataPart("profileImage.png",getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(multipartRequest);


    }

    public void changeCoverImageServer() {

        pDialog = new ProgressDialog(UserProfile.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);

        pDialog.show();

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/change_cover_image", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.i("responsereprofileimgeeeeeee", resultResponse);
                pDialog.cancel();
                try {

                    JSONObject jsonObject = new JSONObject(resultResponse);
                    isSucesscover = jsonObject.getBoolean("success");
                    Log.i("imaggeee", isSucesscover.toString());

//                    JSONArray jsonArray = new JSONArray(resultResponse);
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        isSucessprofile = jsonObject.getBoolean("success");
//                        Log.i("issucesssss", isSucessprofile.toString());
//                        //   String picture=jsonObject.getString("picture");
//                        // Log.i("picture",picture);

                    // }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if ((isSucesscover == true)) {

                    Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();

                   // getProfileData();


//                    final AlertDialog alertDialog = new AlertDialog.Builder(
//                            UserProfile.this).create();
//
//                    // Setting Dialog Title
//                    alertDialog.setTitle("                 Alert!");
//
//                    // Setting Dialog Message
//                    alertDialog.setMessage("sucessfully updated");
//
//                    // Setting Icon to Dialog
//
//
//                    // Setting OK Button
//                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
////                                    Intent intent = new Intent(Intent.ACTION_MAIN);
////                                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
////                                    startActivity(intent);
//                            // Write your code here to execute after dialog closed
//                            // alertDialog.dismiss();
//                            // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_LONG).show();
//                            // saveUpdatedProfileData();
//
//                            alertDialog.dismiss();                        }
//                    });
//
//                    // Showing Alert Message
//                    alertDialog.show();

                } else if (isSucesscover == false) {

                    Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", UserToken);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                //params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                if (bitmap != null) {
                    params.put("cover_image", new DataPart("profileImage.png", getFileDataFromDrawable(bitmap)));
                    Log.i("have", "have");
                } else if (bitmap == null) {
                    //  params.put("picture",new DataPart("profileImage.png",getFileDataFromDrawable(bitmap)));
                    Log.i("Don'thave", "Don'thave");
                }


                // params.put("old_picture",new DataPart("profileImage.png",getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(multipartRequest);

    }

    public void saveProfile() {
        pDialog = new ProgressDialog(UserProfile.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);

        Log.i("instatedata", "instatedata");

        firstname_st = editText_name.getText().toString();
        lastname_st=editText_last.getText().toString();
        email_st = emailPro.getText().toString();
        fulladdress_st = fulladdressPro.getText().toString();
        proffession_st = proffessionPro.getText().toString();
        dob_st = dobPro.getText().toString();


        pDialog.show();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/userprofile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG).show();

                        Log.i("responprofileresponse", response);


                        //   arrayList=new ArrayList<>();

                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        Log.e("response=", response);


                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            isSucessProfileUpdate = jsonObject.getBoolean("success");
                            Log.d(TAG, "onResponse: " + isSucessProfileUpdate);
                            Toast.makeText(UserProfile.this, "updated sucessfully", Toast.LENGTH_LONG).show();
                            getProfileData();

//                            String res = jsonObject.getString("data");
//                            Log.d(TAG, "onResponse: " + res);
//
//                            JSONArray jsonArray = new JSONArray(res);
//
//                            for (int i = 0; i <= jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                CountryDataStorage countryDataStorage = new CountryDataStorage();
//                                ModalClassName modalClassName = new ModalClassName();
//
//                                district_name = jsonObject1.getString("name");
//                                Log.i("statename", district_name);
//                                district_id = jsonObject1.getString("id");
//                                Log.i("state_id", district_id);
//
//                                countryDataStorage.setDistrict_id(district_id);
//                                modalClassName.setDistrictname(district_name);
//                                District_array.add(modalClassName);
//                                districtarray_modal.add(countryDataStorage);
//
//
//                            }


                        } catch (Exception e) {
                        }

                        if(isSucessProfileUpdate==true){
                            if(checkBox.isChecked()){
                                saveProfileHomeLocationnotChecked();
                            }

                        }

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
                params.put("token", UserToken);
                params.put("first_name", firstname_st);
                params.put("last_name", lastname_st);
                params.put("email", email_st);
                params.put("country", "101");
                params.put("state", "10");
                params.put("district", "707");
                params.put("city", "1");
                params.put("gender", "female");
                params.put("marital_status", "yes");
                params.put("current_location", "active");
                params.put("home_city", "1");
                params.put("home_country", "101");
                params.put("home_district", "707");
                params.put("home_state", "10");
                params.put("address", fulladdress_st);
                params.put("profession", proffession_st);
                params.put("dob", dob_st);
                params.put("dob_hidden","1");
                params.put("mobile_hidden","1");


                return params;
            }

        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);


    }

    public void saveProfileHome() {
        pDialog = new ProgressDialog(UserProfile.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);

        Log.i("instatedata", "instatedata");

        firstname_st = editText_name.getText().toString();
        lastname_st=editText_last.getText().toString();
        email_st = emailPro.getText().toString();
        fulladdress_st = fulladdressPro.getText().toString();
        proffession_st = proffessionPro.getText().toString();
        dob_st = dobPro.getText().toString();


        pDialog.show();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/userprofile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.i("responprofileresponsehomeeee", response);




                        //   arrayList=new ArrayList<>();

                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        Log.e("response=", response);


                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            isSucessProfileUpdate = jsonObject.getBoolean("success");
                            Log.d(TAG, "onResponse: " + isSucessProfileUpdate);
                            Toast.makeText(UserProfile.this, "updated sucessfully", Toast.LENGTH_LONG).show();
                            getProfileData();
//                            String res = jsonObject.getString("data");
//                            Log.d(TAG, "onResponse: " + res);
//
//                            JSONArray jsonArray = new JSONArray(res);
//
//                            for (int i = 0; i <= jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                CountryDataStorage countryDataStorage = new CountryDataStorage();
//                                ModalClassName modalClassName = new ModalClassName();
//
//                                district_name = jsonObject1.getString("name");
//                                Log.i("statename", district_name);
//                                district_id = jsonObject1.getString("id");
//                                Log.i("state_id", district_id);
//
//                                countryDataStorage.setDistrict_id(district_id);
//                                modalClassName.setDistrictname(district_name);
//                                District_array.add(modalClassName);
//                                districtarray_modal.add(countryDataStorage);
//
//
//                            }


                        } catch (Exception e) {
                        }

                        if(isSucessProfileUpdate==true){
                            if(!checkBox.isChecked()){
                                saveProfileHomeLocationChecked();
                            }

                        }
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
                params.put("token", UserToken);
                params.put("first_name", firstname_st);
                params.put("last_name", lastname_st);
                params.put("email", email_st);
                params.put("country", "101");
                params.put("state", "10");
                params.put("district", "707");
                params.put("city", "1");
                params.put("gender", "female");
                params.put("marital_status", "yes");
                params.put("current_location", "inactive");
                params.put("home_city", "1");
                params.put("home_country", "101");
                params.put("home_district", "707");
                params.put("home_state", "10");
                params.put("address", fulladdress_st);
                params.put("profession", proffession_st);
                params.put("dob", dob_st);
                params.put("dob_hidden","0");
                params.put("mobile_hidden","0");


                return params;
            }

        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);


    }

public void saveProfileHomeLocationChecked(){
    SharedPreferences.Editor editor = profileprefrences.edit();
   editor.putString("home_location","1");

    editor.commit();

}
    public void saveProfileHomeLocationnotChecked(){
        SharedPreferences.Editor editor = profileprefrences.edit();
        editor.putString("home_location","0");

        editor.commit();

    }
}






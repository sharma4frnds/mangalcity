package net.clamour.mangalcity.profile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.clamour.mangalcity.Home.PostActivity;
import net.clamour.mangalcity.PostTabs.CommonPostActivity;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.LoginResponse;
import net.clamour.mangalcity.ResponseModal.LogoutResponse;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;



import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.user_mobile)
    EditText userMobile;
    @BindView(R.id.user_password)
    EditText userPassword;
    @BindView(R.id.login_button)
    Button loginButton;

    @BindView(R.id.face_book_button)
    Button faceBookButton;
    @BindView(R.id.gmail_button)
    Button gmailButton;
    @BindView(R.id.forget_password)
    TextView forget;

    ProgressDialog pDialog;
    android.support.v7.app.AlertDialog alertDialog;

    String userMobile_st, userPassword_st;
    ApiInterface apiInterface;
    Boolean isSucess;

    String userToken, id, first_name, last_name, mobile, email, country, state, district, city, verified, profile, user_id, profile_image,user_url;
    SharedPreferences LoginPrefrences;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.signup_text)
    TextView signupText;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 6969;
    String gmail_firstname,gmail_last_name,gmail_email,gmail_uid,gmailauth_token;
    private CallbackManager callbackManager ;
    String firstname_fb,lastname_fb,uid_fb,emailid_fb;


    //forgetpass
    EditText mobile_forget, otp_forget, newpassword_forget,confirmpasswordforget;
    Button reset_mob, reset_pass;
    String mobile_no_forget,otp_forget_st,newpassword_forget_st,confirm_password_forget_st,mobile_no_otpresponsedialog;

    ArrayList<String> permissions = new ArrayList<>();
    PermissionUtils permissionUtils;

    boolean isPermissionGranted;


    @Override
    protected void onCreate(Bundle savedInstanceState){
    FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        permissionUtils = new PermissionUtils(LoginActivity.this);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);


        permissionUtils.check_permission(permissions, "Need permission for Write External Storage permission allows us to do store documents", 1);


        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);

        if (LoginPrefrences.contains("userToken")) {

            Intent intent = new Intent(LoginActivity.this, PostActivity.class);
            startActivity(intent);
            finish();
        }

        callbackManager = CallbackManager.Factory.create();

        faceBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));


            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    String  facebook_id=profile.getId();
                    String  f_name=profile.getFirstName();
                    String  m_name=profile.getMiddleName();
                    String  l_name=profile.getLastName();
                    String   full_name=profile.getName();
                    String  profile_image=profile.getProfilePictureUri(400, 400).toString();
                }

                graphRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "net.clamour.mangalcity.profile",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLogin();
            }
        });

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForgotMobileDialog();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               // .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        gmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });



    }

    public void UserLogin() {

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        userMobile_st = userMobile.getText().toString();
        userPassword_st = userPassword.getText().toString();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<LoginResponse> call = apiInterface.userLogin(userMobile_st, userPassword_st);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                pDialog.cancel();

                LoginResponse loginResponse = response.body();
                isSucess = loginResponse.getSuccess();
                Log.d(TAG, "onResponse: " + isSucess);


                if (isSucess == true) {

                    userToken = loginResponse.data.token;
                    Log.d(TAG, "onResponse: " + userToken);

                    first_name = loginResponse.data.user.first_name;
                    // Log.d(TAG, "onResponse: " + first_name);
                    last_name = loginResponse.data.user.last_name;
                    mobile = loginResponse.data.user.mobile;
                    email = loginResponse.data.user.email;
                    user_id = loginResponse.data.user.id;
                    Log.i("userid", user_id);
                    profile_image = loginResponse.data.user.image;
                    user_url=loginResponse.data.user.url;



                    saveData();
                    Intent i = new Intent(LoginActivity.this, PostActivity.class);
                    i.putExtra("usertoken", userToken);
                    startActivity(i);
                    finish();

                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();

                }


                else if (isSucess == false) {

                    Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();


                               }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

    }

    public void saveData() {
        SharedPreferences.Editor editor = LoginPrefrences.edit();
        editor.putString("userToken", userToken);
        editor.putString("userFirstName", first_name);
        editor.putString("userLastName", last_name);
        editor.putString("userEmail", email);
        editor.putString("userMobile", mobile);
        editor.putString("user_id", user_id);
        editor.putString("profileImage", profile_image);
        editor.putString("user_url",user_url);
        editor.commit();
    }

    public void openForgotMobileDialog() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);


        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);


        View subView = inflater.inflate(R.layout.forgetpasswordmobile, null);
        builder.setView(subView);
        alertDialog = builder.create();

        mobile_forget = (EditText) subView.findViewById(R.id.mobile_mob);
        reset_mob = (Button) subView.findViewById(R.id.reset_button_mobile);

        reset_mob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mobile_forget.getText().toString().trim().equals("")){

                    mobile_forget.setError("please enter your mobile number");
                }
                else {
                sendOtpNumber();}
            }
        });



        alertDialog.show();
    }

    public void sendOtpNumber() {

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        mobile_no_forget = mobile_forget.getText().toString();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<LogoutResponse> call = apiInterface.SendOtp(mobile_no_forget);

        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                pDialog.cancel();

                LogoutResponse fogetResponse = response.body();
                isSucess = fogetResponse.getSuccess();
                Log.d(TAG, "onResponse: " + isSucess);


                if (isSucess == true) {

                    mobile_no_otpresponsedialog=fogetResponse.getMobile();
                    Log.d(TAG, "onResponse: "+mobile_no_otpresponsedialog);

                    openPasswordChangeDialog();

                } else if (isSucess == false) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(
                            LoginActivity.this).create();
                    // saveData();
                    // Setting Dialog Title
                    alertDialog.setTitle("                 Alert!");

                    // Setting Dialog Message
                    alertDialog.setMessage("    Please Check Your Mobile Number");

                    // Setting Icon to Dialog


                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //                                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            //                                    emailIntent.setType("text/plain");
                            //                                    startActivity(emailIntent);

                            // Write your code here to execute after dialog closed
                            alertDialog.dismiss();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {

            }
        });


    }

    public void openPasswordChangeDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);


        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);


        View subView = inflater.inflate(R.layout.forget_password_dialog, null);
        builder.setView(subView);
        alertDialog = builder.create();



        otp_forget=(EditText)subView.findViewById(R.id.otp) ;
        newpassword_forget=(EditText)subView.findViewById(R.id.new_password);
        confirmpasswordforget=(EditText)subView.findViewById(R.id.cnfirm_password);

        reset_pass=(Button)subView.findViewById(R.id.reset_button_mobile);



        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (otp_forget.getText().toString().equals("")){
                    otp_forget.setError("please enter otp");
                }

                else if(newpassword_forget.getText().toString().length()<6||newpassword_forget.getText().toString().length()>10){

                    newpassword_forget.setError("Please enter the password between 6-10 characters");
                }

                else if(newpassword_forget.getText().toString().equals("")){
                    newpassword_forget.setError("please enter your new Password");
                }
                else if(confirmpasswordforget.getText().toString().equals("")){

                    confirmpasswordforget.setError("please enter confirm password");
                }

                else if(!newpassword_forget.getText().toString().matches(confirmpasswordforget.getText().toString())){
                    confirmpasswordforget.setError("Password Not Matched");
                }

else {
                ChangePassword();}
            }
        });
        // set the custom dialog components - text, image and button

        // cross_dialog = (ImageView) subView.findViewById(R.id.cross_icon);
        //image.setImageResource(R.drawable.ic_launcher);

        // Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);


        // if button is clicked, close the custom dialog
//        cross_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });

        alertDialog.show();
    }

    public void ChangePassword() {

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();

       otp_forget_st=otp_forget.getText().toString();
       newpassword_forget_st=newpassword_forget.getText().toString();
       confirm_password_forget_st=confirmpasswordforget.getText().toString();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<LogoutResponse> call = apiInterface.forgetPassword(mobile_no_otpresponsedialog,otp_forget_st,newpassword_forget_st,confirm_password_forget_st);

        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                pDialog.cancel();

                LogoutResponse fogetResponse = response.body();
                isSucess = fogetResponse.getSuccess();
                Log.d(TAG, "onResponse: " + isSucess);


                if (isSucess == true) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(
                            LoginActivity.this).create();
                    // saveData();
                    // Setting Dialog Title
                    alertDialog.setTitle("                 Alert!");

                    // Setting Dialog Message
                    alertDialog.setMessage("    Password Successfully Updated");

                    // Setting Icon to Dialog


                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //                                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            //                                    emailIntent.setType("text/plain");
                            //                                    startActivity(emailIntent);

                            // Write your code here to execute after dialog closed
                            alertDialog.dismiss();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();


                } else if (isSucess == false) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(
                            LoginActivity.this).create();
                    // saveData();
                    // Setting Dialog Title
                    alertDialog.setTitle("                 Alert!");

                    // Setting Dialog Message
                    alertDialog.setMessage("    Please Check Credentials");

                    // Setting Icon to Dialog


                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //                                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            //                                    emailIntent.setType("text/plain");
                            //                                    startActivity(emailIntent);

                            // Write your code here to execute after dialog closed
                            alertDialog.dismiss();
                                                 }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        else {
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
          //  updateUI(account);
            // Signed in successfully, show authenticated UI.
         //   if (account != null) {
                String personName = account.getDisplayName();
                gmail_firstname = account.getGivenName();
                gmail_last_name = account.getFamilyName();
                gmail_email = account.getEmail();
                gmail_uid = account.getId();
                gmailauth_token=account.getIdToken();
                Log.d(TAG, "handleSignInResult: "+gmailauth_token+""+account.getServerAuthCode());

                Uri personPhoto = account.getPhotoUrl();
                String personIdToken = account.getIdToken();
                String personServerAuthCode =account.getServerAuthCode();
                String resp = personName+"\n"+gmail_firstname+"\n"+gmail_last_name+"\n"+gmail_email+"\n"+gmail_uid+"\n"+personPhoto;
                //  responceView.setText(resp);
                Log.i("gmail_response",resp);

                sendGmailDatatoServer();

          //  }

        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    public void sendGmailDatatoServer(){
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);



        pDialog.show();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST,"http://emergingncr.com/mangalcity/api/auth/social_login",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("responselogingmailserver", response);





                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        Log.e("response=",response);


                        try {




                            JSONObject jsonObject=new JSONObject(response);
                            isSucess=jsonObject.getBoolean("success");
                            Log.i("status",isSucess.toString());
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            userToken=jsonObject1.getString("token");
                            JSONObject jsonObject2=jsonObject1.getJSONObject("user");
                            first_name=jsonObject2.getString("first_name");
                            last_name=jsonObject2.getString("last_name");
                            mobile=jsonObject2.getString("mobile");
                            email=jsonObject2.getString("email");
                            user_id=jsonObject2.getString("id");
                            profile_image=jsonObject2.getString("image");


//                            firstname_get=jsonObject1.getString("first_name");
//                            Log.i("firstname",firstname_get);
//                            lastname_get=jsonObject1.getString("last_name");
//                            Log.i("lastname",lastname_get);
//                            oth_id=jsonObject1.getString("oauth_uid");
//                            Log.i("outh_id",oth_id);
//                            email_get=jsonObject1.getString("email");
//                            Log.i("email_get",email_get);
//                                user_mode=jsonObject.getString("usertype");
                            //error_code=jsonObject.getString("errorcode");
                            //  Log.i("errorcode",error_code);






                        }
                        catch (Exception e){
                        }
                        if((isSucess==true)) {


                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();

                            saveData();
                            Intent i = new Intent(LoginActivity.this,PostActivity.class);
                            i.putExtra("usertoken", userToken);

                            startActivity(i);
                            finish();


                        }

                        else if(isSucess==false) {

                            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();

                        }



                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(JobDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.i("errorr",error.toString());
                    }
                })

        {




            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("first_name",gmail_firstname);
                params.put("last_name",gmail_last_name);
                params.put("email",gmail_email);
                params.put("provider","google");
                params.put("provider_id",gmail_uid);
                params.put("social_token","session");

                return params;
            }

        };



        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);


    }

    public void sendFaceBookDatatoServer(){

        Log.i("apiiii","apiiiii");

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);






        pDialog.show();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST,"http://emergingncr.com/mangalcity/api/auth/social_login",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG).show();

                        Log.i("responseloginfacebook", response);



                        //   arrayList=new ArrayList<>();

                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        Log.e("response=",response);
//                        Intent intent=new Intent(GuestRegistration.this,OtpScreen.class);
//                        intent.putExtra("mobile_no",mobile_no_st);
//                        startActivity(intent);


                        try {



                            JSONObject jsonObject=new JSONObject(response);
                            isSucess=jsonObject.getBoolean("success");
                            Log.i("status",isSucess.toString());
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            userToken=jsonObject1.getString("token");
                            JSONObject jsonObject2=jsonObject1.getJSONObject("user");
                            first_name=jsonObject2.getString("first_name");
                            last_name=jsonObject2.getString("last_name");
                            mobile=jsonObject2.getString("mobile");
                            email=jsonObject2.getString("email");
                            user_id=jsonObject2.getString("id");
                            profile_image=jsonObject2.getString("image");




                        }
                        catch (Exception e){
                        }
                        if((isSucess==true)) {

                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();


                            saveData();
                            Intent i = new Intent(LoginActivity.this,PostActivity.class);
                            i.putExtra("usertoken", userToken);

                            startActivity(i);
                            finish();



                        }
                        else if(isSucess==false) {
                            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();

                        }


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(JobDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.i("errorr",error.toString());
                    }
                })

        {




            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("first_name",firstname_fb);
                params.put("last_name",lastname_fb);
                params.put("email",emailid_fb);
                params.put("provider","facebook");
                params.put("provider_id",uid_fb);
                params.put("social_token","session_fb");



                return params;
            }

        };



        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);
    }
    public void graphRequest(AccessToken token){
        GraphRequest request = GraphRequest.newMeRequest(token,new GraphRequest.GraphJSONObjectCallback(){

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {



                Log.i("Responsefb",object.toString());

                String jsondata=object.toString()+"";



                try {
                    object=new JSONObject(jsondata);
                    firstname_fb=object.getString("first_name");
                    Log.i("first_name",firstname_fb);
                    lastname_fb=object.getString("last_name");
                    Log.i("last_name",lastname_fb);

                    uid_fb=object.getString("id");
                    Log.i("user_id",uid_fb);

                    if(object.has("email")) {
                        Log.i("getinsideemail","getinsideemail");

                        emailid_fb = object.getString("email");
                        Log.i("user_email", emailid_fb);
                        sendFaceBookDatatoServer();
                    }
                    else{

                        Toast.makeText(getApplicationContext(),"please verify your email in facebook account", Toast.LENGTH_SHORT).show();
                    }

                } catch(Exception e){
                    e.printStackTrace();
                }






            }
        });

        Bundle b = new Bundle();
        b.putString("fields", "last_name,first_name,email,picture.type(large)");
        request.setParameters(b);
        request.executeAsync();



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    @Override
    public void PermissionGranted(int request_code) {

        isPermissionGranted = true;
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {

        isPermissionGranted = false;
    }

    @Override
    public void PermissionDenied(int request_code) {

        isPermissionGranted = false;
    }

    @Override
    public void NeverAskAgain(int request_code) {

        isPermissionGranted = false;
    }
}

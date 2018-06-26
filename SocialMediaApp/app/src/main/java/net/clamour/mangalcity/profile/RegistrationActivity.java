package net.clamour.mangalcity.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.clamour.mangalcity.Home.PostActivity;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.LoginResponse;
import net.clamour.mangalcity.ResponseModal.RegisterResponse;
import net.clamour.mangalcity.countrypost.CountryPost;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";
    @BindView(R.id.user_mobile)
    EditText userMobile;
    @BindView(R.id.getotp_button)
    Button getotpButton;
    @BindView(R.id.signIn_text)
    TextView signInText;
    @BindView(R.id.user_email)
    EditText userEmail;
    @BindView(R.id.user_password)
    EditText userPassword;
    @BindView(R.id.user_cnfrm_password)
    EditText userCnfrmPassword;

    ProgressDialog pDialog;
    @BindView(R.id.first_name)
    EditText firstName;
    @BindView(R.id.last_name)
    EditText lastName;

    String firstname_st,lastname_st, mobile_st, email_st, password_st, confirmpass_st;
    ApiInterface apiInterface;
    Boolean isSucess;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        getotpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(RegistrationActivity.this, GetOtp.class);
//                startActivity(intent);
                if(firstName.getText().toString().trim().equals("")){
                    firstName.setError("please enter first name");
                }
                else if(lastName.getText().toString().trim().equals("")){
                    lastName.setError("please enter last name");
                }
                else if(userMobile.getText().toString().trim().equals("")){

                    userMobile.setError("please enter mobile no");
                }
                else if(userEmail.getText().toString().trim().equals("")){

                    userEmail.setError("Please check your Email Id");
                }
                else if (!userEmail.getText().toString().matches(emailPattern)){
                    userEmail.setError("Please enter valid Email Id ");
                }

                else if(userPassword.getText().toString().trim().equals("")){
                    userPassword.setError("Please enter password");
                }
                else if(userPassword.getText().toString().length()<6||userPassword.getText().toString().length()>10){

                    userPassword.setError("Please enter the password between 6-10 characters");
                }
                else if(userCnfrmPassword.getText().toString().trim().equals("")){

                    userCnfrmPassword.setError("Please ReType password");
                }
                else if(!userPassword.getText().toString().matches(userCnfrmPassword.getText().toString())){
                    userCnfrmPassword.setError("Password Not Matched");
                }

                else {
                    Registration();
                }


            }
        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

public void Registration(){



    firstname_st=firstName.getText().toString();
    Log.d(TAG, "registerUser: "+firstname_st);
    lastname_st=lastName.getText().toString();
    email_st=userEmail.getText().toString();
    mobile_st=userMobile.getText().toString();
    password_st=userPassword.getText().toString();
    confirmpass_st=userCnfrmPassword.getText().toString();

    pDialog = new ProgressDialog(RegistrationActivity.this);
    pDialog.setMessage("Please wait...");
    pDialog.setCancelable(true);
    pDialog.show();




    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    Call<RegisterResponse> call = apiInterface.User_Registration(firstname_st,lastname_st,mobile_st,email_st,password_st,confirmpass_st);

    call.enqueue(new Callback<RegisterResponse>() {
        @Override
        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
             pDialog.cancel();

            RegisterResponse loginResponse = response.body();
            isSucess = loginResponse.getSuccess();
            Log.d(TAG, "onResponse: " + isSucess);


            if (isSucess == true) {

                Intent i = new Intent(RegistrationActivity.this, GetOtp.class);
                i.putExtra("mobile_number",mobile_st);
                startActivity(i);
                finish();


            } else if (isSucess == false) {

                Toast.makeText(getApplicationContext(),"Please Check Your Details",Toast.LENGTH_LONG).show();

            }
        }
        @Override
        public void onFailure(Call<RegisterResponse> call, Throwable t) {

        }
    });

                 }}

//    public void saveData(){
//        SharedPreferences.Editor editor=RegistrationPrefrence.edit();
//        editor.putString("")
//        editor.putString("UserEmail",SessionEmailString);
//        editor.putString("UserName",name_response);
//        editor.putString("UserMobile",mobile_response);
//        editor.commit();
//
//
//    }



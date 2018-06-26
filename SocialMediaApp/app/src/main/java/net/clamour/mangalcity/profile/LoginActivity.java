package net.clamour.mangalcity.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.clamour.mangalcity.Home.PostActivity;
import net.clamour.mangalcity.PostTabs.CommonPostActivity;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.LoginResponse;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

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

    String userToken, id, first_name, last_name, mobile, email, country, state, district, city, verified, profile,user_id,profile_image;
    SharedPreferences LoginPrefrences;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.signup_text)
    TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);

        if (LoginPrefrences.contains("userToken")) {

            Intent intent = new Intent(LoginActivity.this, PostActivity.class);
            startActivity(intent);
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
        openForgotEmailDialog();
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
                        user_id=loginResponse.data.user.id;
                        Log.i("userid",user_id);
                        profile_image=loginResponse.data.user.image;

                        saveData();
                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                LoginActivity.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("                 Alert!");

                        // Setting Dialog Message
                        alertDialog.setMessage("            Login Successfull");

                        // Setting Icon to Dialog


                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
    //                                    Intent intent = new Intent(Intent.ACTION_MAIN);
    //                                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
    //                                    startActivity(intent);
                                // Write your code here to execute after dialog closed
                                // alertDialog.dismiss();
                                // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_LONG).show();

                                // verifyEmail();
                                // saveData();
                                Intent i = new Intent(LoginActivity.this, PostActivity.class);
                                i.putExtra("usertoken",userToken);
                                startActivity(i);
                                finish();
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
                        alertDialog.setMessage("    Invalid Credentials");

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
        editor.putString("user_id",user_id);
        editor.putString("profileImage",profile_image);
        editor.commit();
    }

    public void openForgotEmailDialog(){

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);



        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);


        View subView = inflater.inflate(R.layout.forget_password_dialog, null);
        builder.setView(subView);
        alertDialog = builder.create();

      //  forgot_email=(EditText)subView.findViewById(R.id.current_password);
//        confirm_password=(EditText)subView.findViewById(R.id.confirm_password);
//        pass_matched=(TextView)subView.findViewById(R.id.pass_matched);
      //  submit_email=(Button)subView.findViewById(R.id.reset_button_email);


//        submit_email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ForgotPassword();
//            }
//        });
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

        alertDialog.show();}


}

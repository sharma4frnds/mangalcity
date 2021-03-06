package net.clamour.mangalcity.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.OtpVerificationResponse;
import net.clamour.mangalcity.ResponseModal.ResendOtpResponse;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetOtp extends AppCompatActivity {
    private static final String TAG = "GetOtp";

    @BindView(R.id.otp_code)
    EditText otpCode;
    @BindView(R.id.submit_otp)
    Button submitOtp;


    String otp_st, mobile_st;
    ProgressDialog pDialog;
    ApiInterface apiInterface;
    Boolean isSucess;
    @BindView(R.id.resendOtp)
    TextView resendOtp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_otp);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mobile_st = intent.getStringExtra("mobile_number");
        Log.i("mobileeeee", mobile_st);

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resendOTP();
            }
        });


        submitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (otpCode.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter the Code", Toast.LENGTH_SHORT).show();

                } else {

                    otpVerification();
                }


            }
        });


    }

    public void otpVerification() {

        pDialog = new ProgressDialog(GetOtp.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        otp_st = otpCode.getText().toString();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<OtpVerificationResponse> call = apiInterface.User_Verification(otp_st, mobile_st);

        call.enqueue(new Callback<OtpVerificationResponse>() {
            @Override
            public void onResponse(Call<OtpVerificationResponse> call, Response<OtpVerificationResponse> response) {
                pDialog.cancel();

                OtpVerificationResponse otpVerificationResponse = response.body();
                isSucess = otpVerificationResponse.success;
                Log.d(TAG, "onResponse: " + isSucess);


                if (isSucess == true) {

                    Toast.makeText(getApplicationContext(),"Successfully Verified",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(GetOtp.this, LoginActivity.class);
                    startActivity(i);
                    finish();



                } else if (isSucess == false) {

                    Toast.makeText(getApplicationContext(),"Invalid Otp",Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<OtpVerificationResponse> call, Throwable t) {

            }
        });

    }

public void resendOTP(){
    pDialog = new ProgressDialog(GetOtp.this);
    pDialog.setMessage("Please wait...");
    pDialog.setCancelable(true);
    pDialog.show();

    otp_st = otpCode.getText().toString();

    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    Call<ResendOtpResponse> call = apiInterface.resendOtp(mobile_st);

    call.enqueue(new Callback<ResendOtpResponse>() {
        @Override
        public void onResponse(Call<ResendOtpResponse> call, Response<ResendOtpResponse> response) {
            pDialog.cancel();

            ResendOtpResponse otpVerificationResponse = response.body();
            isSucess = otpVerificationResponse.getSuccess();
            Log.d(TAG, "onResponse: " + isSucess);


            if (isSucess == true) {

                resendOtp.setEnabled(false);
                final AlertDialog alertDialog = new AlertDialog.Builder(
                        GetOtp.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("                 Alert!");

                // Setting Dialog Message
                alertDialog.setMessage("    Successfully Sent To Your Mobile No ");

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
//                        Intent i = new Intent(GetOtp.this, LoginActivity.class);
//                        startActivity(i);
//                        finish();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

            } else if (isSucess == false) {

                final AlertDialog alertDialog = new AlertDialog.Builder(
                        GetOtp.this).create();
                // saveData();
                // Setting Dialog Title
                alertDialog.setTitle("                 Alert!");

                // Setting Dialog Message
                alertDialog.setMessage("    Invalid Mobile No.");

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
        public void onFailure(Call<ResendOtpResponse> call, Throwable t) {

        }
    });


}
}

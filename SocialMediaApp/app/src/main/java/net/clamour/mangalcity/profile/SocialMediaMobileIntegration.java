package net.clamour.mangalcity.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.OtpVerificationResponse;
import net.clamour.mangalcity.SocialMedia.SocialMediaOtpResponse;
import net.clamour.mangalcity.SocialMedia.SocialMobileResponse;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SocialMediaMobileIntegration extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.mobile_number)
    EditText mobileNumber;
    @BindView(R.id.submit_mobile)
    Button submitMobile;
    @BindView(R.id.relative_button)
    RelativeLayout relativeButton;

    String mobile_st,userToken;
    ProgressDialog pDialog;
    ApiInterface apiInterface;
    Boolean isSucess;
    private static final String TAG = "SocialMediaMobileIntegr";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media_mobile_integration);
        ButterKnife.bind(this);

        Intent intent=getIntent();
        userToken=intent.getStringExtra("usertoken");


        pDialog = new ProgressDialog(SocialMediaMobileIntegration.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        mobile_st = mobileNumber.getText().toString();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<SocialMobileResponse> call = apiInterface.socialmediaMobile(userToken, mobile_st);

        call.enqueue(new Callback<SocialMobileResponse>() {
            @Override
            public void onResponse(Call<SocialMobileResponse> call, Response<SocialMobileResponse> response) {
                pDialog.cancel();
try {
    SocialMobileResponse socialMobileResponse = response.body();
    isSucess = socialMobileResponse.success;
    Log.d(TAG, "onResponse: " + isSucess);
}
catch (Exception e){

}



                if (isSucess == true) {

                    Toast.makeText(getApplicationContext(),"Successfully Verified",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SocialMediaMobileIntegration.this, SocialLoginOtp.class);
                    i.putExtra("userToken",userToken);
                    i.putExtra("mobile_number",mobile_st);
                    startActivity(i);
                    finish();



                } else if (isSucess == false) {

                    Toast.makeText(getApplicationContext(),"Invalid Otp",Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<SocialMobileResponse> call, Throwable t) {

            }
        });

    }

    }


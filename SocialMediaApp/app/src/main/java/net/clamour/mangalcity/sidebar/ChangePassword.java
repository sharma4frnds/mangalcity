package net.clamour.mangalcity.sidebar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import net.clamour.mangalcity.Home.CommonBaseActivity;
import net.clamour.mangalcity.Home.DrawerBaseActivity;
import net.clamour.mangalcity.Home.PostActivity;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.changePasswordResponse;
import net.clamour.mangalcity.profile.LoginActivity;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends DrawerBaseActivity {

    private static final String TAG = "ChangePassword";


    @BindView(R.id.new_password)
    EditText newPassword;

    @BindView(R.id.saveChanges)
    Button saveChanges;


    ApiInterface apiInterface;

    String newPassword_st,oldpassword_st,confirmpassword_st,UserToken;
    ProgressDialog pDialog;
    Boolean isSucess;
    @BindView(R.id.old_password)
    EditText oldPassword;
    @BindView(R.id.confirm_password)
    EditText confirmPassword;

    SharedPreferences LoginPrefrences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar1.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar1.setTitle("Security");
        setSupportActionBar(toolbar1);
        setDrawer();

        oldPassword.setSelection(0);

        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        UserToken=LoginPrefrences.getString("userToken","");
        Log.i("token",UserToken);


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oldPassword.getText().toString().trim().equals("")) {
                    oldPassword.setError("please enter old Password");
                }
                else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError("Please enter new password");
                }
                else if(confirmPassword.getText().toString().trim().equals("")) {
                    confirmPassword.setError("Please enter confirm password");
                }
                else {
                    changePassword();
                }


            }
        });
    }

    public void changePassword() {

        oldpassword_st = oldPassword.getText().toString();
        Log.d(TAG, "registerUser: " + oldpassword_st);
        newPassword_st = newPassword.getText().toString();
        confirmpassword_st=confirmPassword.getText().toString();

        pDialog = new ProgressDialog(ChangePassword.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<changePasswordResponse> call = apiInterface.changePassword(UserToken,oldpassword_st,newPassword_st,confirmpassword_st);

        call.enqueue(new Callback<changePasswordResponse>() {
            @Override
            public void onResponse(Call<changePasswordResponse> call, Response<changePasswordResponse> response) {
                pDialog.cancel();

                try {

                    changePasswordResponse loginResponse = response.body();
                    isSucess = loginResponse.getSuccess();
                    Log.d(TAG, "onResponse: " + isSucess);
                }
                catch (Exception e){


                }

                if (isSucess == true) {

                    Toast.makeText(getApplicationContext(),"password changed sucessfully",Toast.LENGTH_SHORT).show();
                    confirmPassword.getText().clear();
                    newPassword.getText().clear();
                    oldPassword.getText().clear();

                    Intent intent=new Intent(ChangePassword.this, CommonBaseActivity.class);
                    startActivity(intent);
                }


                 else if (isSucess == false) {

                    Toast.makeText(getApplicationContext(),"Invalid Password",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<changePasswordResponse> call, Throwable t) {

            }
        });

    }
}




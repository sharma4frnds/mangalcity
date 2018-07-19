package net.clamour.mangalcity.PostTabs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import net.clamour.mangalcity.Home.PostActivity;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.LoginResponse;
import net.clamour.mangalcity.ResponseModal.PostResponse;
import net.clamour.mangalcity.profile.LoginActivity;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonPostActivity extends AppCompatActivity {

//    ArrayList<PostModalClass> post_array;
//    PostAdapter postAdapter;
//    private RecyclerView recyclerView;
//
//    private RecyclerView recyclerView_horizental;
//
//    ViewPager viewPager;
//    // int images[] = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4};
//    TrendingPostAdapter trendingPostAdapter;
//    ImageView gallery_upload,setImage;
//
//    private static final int SELECT_PICTURE = 100;
//    private static final String TAG = "UserProfile";
//
//    Uri selectedImageUri;
//    Bitmap bitmap;
//    String path;
//    EditText PostText_et;
//    String posttext_st;
//    Button post_button;
//    ProgressDialog pDialog;
//    ApiInterface apiInterface;
//    SharedPreferences LoginPrefrences;
//    Boolean isSucess;
//    String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjU0LCJpc3MiOiJodHRwOi8vZW1lcmdpbmduY3IuY29tL21hbmdhbGNpdHkvYXBpL2F1dGgvbG9naW4iLCJpYXQiOjE1MjkzMTg2NDEsImV4cCI6MTUyOTMyMjI0MSwibmJmIjoxNTI5MzE4NjQxLCJqdGkiOiJ0UlRNUUd6UnFCUEp5NjFTIn0.nox7Piw5BgKOU-c7BEUc6y4srSD3Hd_V5JCSuqM5hgM";
//String getToken;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_common_post);
//
////        Intent intent=getIntent();
////        getToken=intent.getStringExtra("usertoken");
////        Log.i("tokennnnnn",getToken);
//
//
//        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
//        getToken=LoginPrefrences.getString("userToken","");
//        Log.d(TAG, "onCreate: "+getToken);
//
//
//
//        post_array=new ArrayList<>();
//        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
//        gallery_upload=(ImageView)findViewById(R.id.gallery_upload);
//        setImage=(ImageView)findViewById(R.id.setImage);
//        PostText_et=(EditText)findViewById(R.id.editText_textpost) ;
//        post_button=(Button)findViewById(R.id.post_button);
//
//        gallery_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //captureImage();
//            }
//        });
//
//
//
//      //  postAdapter=new PostAdapter (CommonPostActivity.this,post_array);
//
//        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CommonPostActivity.this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(postAdapter);
//       // postAdapter.setClickListener(this);
//
//        post_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//             //   getPostData();
//            }
//        });
//
//
//        setData();
//
//    }
//
//    public void setData(){
//
//        PostModalClass postModalClass=new PostModalClass("anuska","10 min","Well Done Dude",R.drawable.image,0,0,0,0,R.drawable.anu);
//        post_array.add(postModalClass);
//        PostModalClass postModalClass1=new PostModalClass("anita","40 min","Well Done Dude",R.drawable.image2,0,0,0,0,R.drawable.anu);
//        post_array.add(postModalClass1);
//        PostModalClass postModalClass2=new PostModalClass("anuska","10 min","Well Done Dude",R.drawable.image2,0,0,0,0,R.drawable.anu);
//        post_array.add(postModalClass2);
//    }
//
////    public void getPostData(){
////
////        final ProgressDialog progressDialog;
////        progressDialog = new ProgressDialog(CommonPostActivity.this);
////        progressDialog.setMessage("please wait");
////        progressDialog.show();
////
////        //Create Upload Server Client
////        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
////
////        //File creating from selected URL
////        File file = new File("/storage/emulated/0/Download/Corrections 6.jpg");
////
////        // create RequestBody instance from file
////        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
////
////        MultipartBody.Part body =
////                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);
////        RequestBody fullName =
////                RequestBody.create(MediaType.parse("multipart/form-data"), "ghgdhsgfhgdzfh");
////        RequestBody fullName1 =
////                RequestBody.create(MediaType.parse("multipart/form-data"), "ugidfgdhgfjhjdghj");
////
////        RequestBody fullName2 =
////                RequestBody.create(MediaType.parse("multipart/form-data"), getToken);
////
////
////        Call<PostResponse> call = apiInterface.postData(fullName2,fullName,body);
////
////        call.enqueue(new Callback<PostResponse>() {
////            @Override
////            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
////
////               // progressDialog.dismiss();
////                PostResponse postResponse=response.body();
////                 isSucess=postResponse.getSuccess();
////                Log.d(TAG, "onResponse: "+isSucess);
////
////
////                // Response Success or Fail
//////                if (response.isSuccessful()) {
//////                    if (response.body().getResult().equals("success"))
//////                        Snackbar.make(parentView, R.string.string_upload_success, Snackbar.LENGTH_LONG).show();
//////                    else
//////                        Snackbar.make(parentView, R.string.string_upload_fail, Snackbar.LENGTH_LONG).show();
//////
//////                } else {
//////                    Snackbar.make(parentView, R.string.string_upload_fail, Snackbar.LENGTH_LONG).show();
//////                }
////
////                /**
////                 * Update Views
////                 */
////              //  imagePath = "";
////               // textView.setVisibility(View.VISIBLE);
////               // imageView.setVisibility(View.INVISIBLE);
////            }
////
////            @Override
////            public void onFailure(Call<PostResponse> call, Throwable t) {
////
////                progressDialog.dismiss();
////            }
////        });
////    }



}



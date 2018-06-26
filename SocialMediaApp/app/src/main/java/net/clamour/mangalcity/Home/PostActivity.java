package net.clamour.mangalcity.Home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.clamour.mangalcity.BuildConfig;
import net.clamour.mangalcity.PostTabs.CommonPostActivity;
import net.clamour.mangalcity.PostTabs.PostAdapter;
import net.clamour.mangalcity.PostTabs.SharePostActivity;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.CountryPostResponse;
import net.clamour.mangalcity.ResponseModal.FeedsResponse;
import net.clamour.mangalcity.ResponseModal.OwnLikeResponse;
import net.clamour.mangalcity.ResponseModal.PostResponse;
import net.clamour.mangalcity.countrypost.CountryPost;
import net.clamour.mangalcity.districtpost.DistrictPost;
import net.clamour.mangalcity.statepost.StatePost;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends DrawerBaseActivity implements EasyPermissions.PermissionCallbacks {


    @BindView(R.id.button_country)
    Button buttonCountry;
    @BindView(R.id.button_state)
    Button buttonState;
    @BindView(R.id.button_district)
    Button buttonDistrict;
    @BindView(R.id.editText_textpost)
    EditText editTextTextpost;
    @BindView(R.id.person_profile_image)
    ImageView personProfileImage;
    @BindView(R.id.gallery_upload)
    ImageView galleryUpload;
    @BindView(R.id.video_upload)
    ImageView video_upload;
    @BindView(R.id.imagepreview)
    ImageView imagepreview;
    @BindView(R.id.post_buttonnn)
    Button post_button;

    ProgressDialog pDialog;
    ApiInterface apiInterface;
    Boolean isSucess,isSucessPost;;
    private static final String TAG = "PostActivity";
    List<CountryPostResponse> countryPostResponses_array;
    PostAdapter postAdapter;
    private RecyclerView recyclerView;

    private static final int SELECT_PICTURE = 100;

    Uri selectedImageUri;
    Bitmap bitmap;
    String path;
    String UserToken,profile_image;
    SharedPreferences LoginPrefrences;



    private static final int REQUEST_IMAGE_CODE = 200;
    private static final int REQUEST_VIDEO_CODE = 210;
    private static final int READ_REQUEST_CODE = 300;
    private static final String SERVER_PATH = "http://emergingncr.com/mangalcity/api/";
    private Uri uri;
    private String imagepath ="";
    private String videopath ="";
    private String message ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        ButterKnife.bind(this);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar1.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar1.setTitle("Post");
        setSupportActionBar(toolbar1);
        setDrawer();

        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        UserToken=LoginPrefrences.getString("userToken","");
        Log.i("UserToken",UserToken);
        profile_image=LoginPrefrences.getString("profileImage","");

        Glide.with(PostActivity.this).load("http://emergingncr.com/mangalcity/public/images/user/"+profile_image)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(personProfileImage);





        buttonCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(PostActivity.this, CountryPost.class);
                startActivity(intent);

            }
        });

        buttonDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PostActivity.this, DistrictPost.class);
                startActivity(intent);

            }
        });

        buttonState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PostActivity.this, StatePost.class);
                startActivity(intent);

            }
        });
        countryPostResponses_array=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);

        getCityPost();

        galleryUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   captureImage();

                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("image/*");
                startActivityForResult(openGalleryIntent, REQUEST_IMAGE_CODE);

            }
        });

        video_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("video/*");
                startActivityForResult(openGalleryIntent, REQUEST_VIDEO_CODE);

            }
        });




        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getPostData();
                message=editTextTextpost.getText().toString();
                Log.d(TAG, "input : "+ imagepath +"\n"+ videopath +"\n"+ message);
                pjUploadMultiFile(message, imagepath, videopath);



            }
        });

    }

    public void getCityPost()
    {

        pDialog = new ProgressDialog(PostActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<FeedsResponse> call = apiInterface.getFeeds(UserToken);

        call.enqueue(new Callback<FeedsResponse>() {
            @Override
            public void onResponse(Call<FeedsResponse> call, Response<FeedsResponse> response) {
                pDialog.cancel();

                FeedsResponse feedsResponse = response.body();
                isSucess = feedsResponse.getSuccess();
                Log.d(TAG, "onResponse: " + isSucess);

                countryPostResponses_array=feedsResponse.getCity_posts();
                Log.d(TAG, "onResponse: " + countryPostResponses_array);




                for ( CountryPostResponse countryPostResponse:countryPostResponses_array){

                    String hfdh=countryPostResponse.getCreated_at();
                    Log.d(TAG, "onResponse: "+hfdh);

                    String iddd=countryPostResponse.getId();
                    Log.d(TAG, "onResponse: "+iddd);

                    String fjdsfj=countryPostResponse.user.first_name;
                    Log.d(TAG, "onResponse: "+fjdsfj+""+iddd);


                    String fhghsdkj=countryPostResponse.like+"";
                    Log.d(TAG, "onResponse: "+fhghsdkj+""+iddd);
                    if(fhghsdkj==null){

                        Log.i("nullll",fhghsdkj);
                    }

                    else{

                        Log.i("nottttttnullll",fhghsdkj);

                   //    String hfhgjhjfg=countryPostResponse.like.getType();

                    }




                }
                postAdapter=new PostAdapter(PostActivity.this,countryPostResponses_array);

                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(PostActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(postAdapter);


//                first_name = loginResponse.data.user.first_name;
//                Log.d(TAG, "onResponse: " + first_name);
//                last_name = loginResponse.data.user.last_name;
//                mobile = loginResponse.data.user.mobile;
//                email = loginResponse.data.user.email;



            }

            @Override
            public void onFailure(Call<FeedsResponse> call, Throwable t) {

            }
        });


    }
//    public void captureImage() {
//
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
//    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, PostActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String imagepathSelected = getRealPathFromURIPath(uri, PostActivity.this);
                File imageFile = new File(imagepathSelected);
                double maxFileSize = 2.00;
                double selectedImageFileSize = getFileSizeMegaBytes(imageFile);
                Log.d(TAG, "selectedImageFileSize " + selectedImageFileSize);
                Log.d(TAG, "maxFileSize " + maxFileSize);
                if (selectedImageFileSize < maxFileSize){
                    imagepath =  imagepathSelected;
                }else{
                    Toast.makeText(PostActivity.this, "File Size is greater than 2MB " , Toast.LENGTH_LONG).show();
                }

                Log.d(TAG, "Filename " + imagepath);

            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        if (requestCode == REQUEST_VIDEO_CODE && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String videopathSelected = getPath(uri, PostActivity.this);
                File videoFile = new File(videopathSelected);
                double maxFileSize = 2.00;
                double selectedVideoFileSize = getFileSizeMegaBytes(videoFile);
                if (selectedVideoFileSize < maxFileSize){
                    videopath =  videopathSelected;
                }else{
                    Toast.makeText(PostActivity.this, "File Size is greater than 2MB " , Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, "Filename " + videopath);

            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private String getRealPathFromURIPath(Uri contentURI, Context activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

            return cursor.getString(idx);
        }
    }
    public String getPath(Uri uri, Context activity) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private static double getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024) ;
    }



    private void pjUploadMultiFile(String message,String imageFilePath,String videoFilePath) {


        File videoFile = new File(videoFilePath);
        File imageFile = new File(imageFilePath);


        MediaType mediaType = MediaType.parse("multipart/form-data");

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Log.d(TAG, "pjUploadMultiFile: "+imageFile.getName() + "  "+ videoFile.getName());
        builder.addFormDataPart("token", UserToken);
        builder.addFormDataPart("message", message);
        if(!imageFilePath.equalsIgnoreCase("")) {
            builder.addFormDataPart("image", imageFile.getName(), RequestBody.create(mediaType, imageFile));
        }

        if(!videoFilePath.equalsIgnoreCase("")) {
            builder.addFormDataPart("video", videoFile.getName(), RequestBody.create(mediaType, videoFile));
        }
        MultipartBody requestBody = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .client(provideOkhttpClient())
                .build();
        ApiInterface uploadImage = retrofit.create(ApiInterface.class);
        Call<PostResponse> fileUpload = uploadImage.postData("multipart/form-data; boundary=" + requestBody.boundary(),requestBody);
        fileUpload.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                PostResponse postResponse = response.body();
                isSucessPost = postResponse.getSuccess();
                Log.i("sucesspost", isSucessPost.toString());


            //    Toast.makeText(PostActivity.this, "Success " + response.message(), Toast.LENGTH_LONG).show();

              //  Toast.makeText(PostActivity.this, "Success " + response.message(), Toast.LENGTH_LONG).show();
              //  Toast.makeText(PostActivity.this, "Success " + response.body().toString(), Toast.LENGTH_LONG).show();
//                if(response != null){
//                    Log.d(TAG, "Succcess " + response.body().toString());
//                    Log.d(TAG, "Succcess " + response.message());
//                }


                //   postAdapter.notifyDataSetChanged();
                editTextTextpost.getText().clear();


                if (isSucessPost == true) {

                    getCityPost();
                    final AlertDialog alertDialog = new AlertDialog.Builder(
                            PostActivity.this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("                 Alert!");

                    // Setting Dialog Message
                    alertDialog.setMessage("            Successfully Posted ");

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
                            alertDialog.dismiss();


                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();


                } else if (isSucessPost == false) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(
                            PostActivity.this).create();
                    // saveData();
                    // Setting Dialog Title
                    alertDialog.setTitle("                 Alert!");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please enter some text,image or video");

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
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d(TAG, "Succcess " + t.getMessage());
            }
        });

    }

    public static OkHttpClient provideOkhttpClient() {

        HttpLoggingInterceptor interceptor = null;
        if (BuildConfig.DEBUG) {
            interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        return new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_IMAGE_CODE){
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
            openGalleryIntent.setType("image/*");
            startActivityForResult(openGalleryIntent, REQUEST_IMAGE_CODE);
        }else  if (requestCode == REQUEST_VIDEO_CODE){
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
            openGalleryIntent.setType("video/*");
            startActivityForResult(openGalleryIntent, REQUEST_VIDEO_CODE);
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }
}





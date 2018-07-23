package net.clamour.mangalcity.statepost;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.clamour.mangalcity.Home.DrawerBaseActivity;
import net.clamour.mangalcity.Home.FilePath;
import net.clamour.mangalcity.Home.OtherUserProfile;
import net.clamour.mangalcity.Home.PaginationScrollListener;
import net.clamour.mangalcity.Home.SearchAdapter;
import net.clamour.mangalcity.MainActivity;
import net.clamour.mangalcity.PostTabs.CommonAdapterPost;
import net.clamour.mangalcity.PostTabs.FeedBackActivity;
import net.clamour.mangalcity.PostTabs.PostAdapter;
import net.clamour.mangalcity.PostTabs.SharePostActivity;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.CityPostResponse;
import net.clamour.mangalcity.ResponseModal.Country_Posts;
import net.clamour.mangalcity.ResponseModal.FeedsResponse;
import net.clamour.mangalcity.ResponseModal.LikeResponse;
import net.clamour.mangalcity.ResponseModal.PostDeleteResponse;
import net.clamour.mangalcity.ResponseModal.PostResponse;
import net.clamour.mangalcity.ResponseModal.State_Posts;
import net.clamour.mangalcity.countrypost.CountryPost;

import net.clamour.mangalcity.feed.CityPosts;
import net.clamour.mangalcity.feed.FeedPostData;
import net.clamour.mangalcity.feed.PostFeedResponse;
import net.clamour.mangalcity.profile.UserProfile;

import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class StatePost extends DrawerBaseActivity  {




    ProgressDialog pDialog;
    ApiInterface apiInterface;
    Boolean isSucess, isSucessPost;
    String fullPath;

    private static final String TAG = "CountryPost";


    List<FeedPostData> postDataList;
    CommonAdapterPost postAdapter;
    private RecyclerView recyclerView;

    String UserToken, profile_image;
    SharedPreferences LoginPrefrences;

    String name,city,image;
    AlertDialog.Builder builder;
    AlertDialog alertDialog_search;
    Button btn_cancel,reportspam,delete_sheet,download;

    private AsyncTask mMyTask;



//    @BindView(R.id.swipe_refresh_layout)
//    SwipeRefreshLayout swipeRefreshLayout;

    private static final int REQUEST_IMAGE_CODE = 200;
    private static final int REQUEST_VIDEO_CODE = 210;
    private static final int REQUEST_AUDIO_CODE = 220;

    private static final int READ_REQUEST_CODE = 300;
    private static final String SERVER_PATH = "http://emergingncr.com/mangalcity/api/";
    private Uri uri;
    private String imagepath = "";
    private String videopath = "";
    private String message = "";
    private String audiopath = "";
    JSONArray jsonArray, filtered_array;
    BottomSheetDialog dialog;

    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private static int PAGE_SIZE = 10;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int currentPage = 1;
    private android.support.v7.app.AlertDialog alertDialog;
    private Button feedback;
    private Button delete;
    private State_Posts state_posts;
    private String user_id;
    List<FeedPostData> nextList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_post);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar1.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar1.setTitle("State Posts");


        setSupportActionBar(toolbar1);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        UserToken = LoginPrefrences.getString("userToken", "");
        Log.i("UserToken", UserToken);
        user_id = LoginPrefrences.getString("user_id", "");
        profile_image = LoginPrefrences.getString("profileImage", "");






        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);

        postDataList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        postAdapter = new CommonAdapterPost(StatePost.this);
        recyclerView.setAdapter(postAdapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void showForm() {

            }

            @Override
            protected void hideForm() {

            }

            @Override
            protected void onScrolledToEnd() {
                Log.e("Position", "Last item reached");
            }

            @Override
            protected void loadMoreItems() {
                Log.d(TAG, "loadMoreItems: Loading");
                isLoading = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        loadFirstPage();
//        swipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeRefreshLayout.setRefreshing(true);
//                loadFirstPage();
//            }
//        });


        postAdapter.setOnItemClickListner(new CommonAdapterPost.OnItemClickListner() {
            @Override
            public void onLikeClick(final int position) {
                Log.d(TAG, "onLikeClick: " + position);
                final FeedPostData feedPostData = postDataList.get(position);
                try {
                    pDialog = new ProgressDialog(StatePost.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(true);
                    pDialog.show();
                    Call<LikeResponse> call = apiInterface.like_post(UserToken, feedPostData.getId() + "");
                    call.enqueue(new Callback<LikeResponse>() {
                        @Override
                        public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {

                            LikeResponse likeResponse = response.body();
                            Log.d(TAG, "onResponse: " + response.code() + " " +response.errorBody()+ " " +response.message() +" "+ response.headers());

                            if (likeResponse.getSuccess() == true) {
                                pDialog.cancel();
                                Log.d(TAG, "pre: " + feedPostData.toString());
                                feedPostData.setLikes(Long.valueOf(likeResponse.getLcount()));
                                feedPostData.setDislikes(Long.valueOf(likeResponse.getDcount()));

                                postDataList.set(position, feedPostData);
                                Log.d(TAG, "post: " + feedPostData.toString());
                                postAdapter.replace(position, feedPostData);
                                postAdapter.notifyItemChanged(position);


                            } else if (likeResponse.getSuccess() == false) {

                                final AlertDialog alertDialog = new AlertDialog.Builder(
                                        StatePost.this).create();

                                alertDialog.setTitle("                 Alert!");


                                alertDialog.setMessage("    Try Again");

                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        alertDialog.dismiss();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LikeResponse> call, Throwable t) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onDislikeClick(final int position) {
                Log.d(TAG, "onDislikeClick: " + position);
                pDialog = new ProgressDialog(StatePost.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(true);
                pDialog.show();
                try {
                    final FeedPostData feedPostData = postDataList.get(position);
                    Call<LikeResponse> call = apiInterface.dislike_post(UserToken, feedPostData.getId() + "");
                    call.enqueue(new Callback<LikeResponse>() {
                        @Override
                        public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {

                            LikeResponse likeResponse = response.body();
                            if (likeResponse.getSuccess() == true) {
                                pDialog.cancel();
                                Log.d(TAG, "pre: " + postDataList.toString());
                                feedPostData.setLikes(Long.valueOf(likeResponse.getLcount()));
                                feedPostData.setDislikes(Long.valueOf(likeResponse.getDcount()));
                                postDataList.set(position, postDataList.get(position));
                                Log.d(TAG, "post: " + postDataList.toString());
                                postAdapter.replace(position, postDataList.get(position));
                                postAdapter.notifyItemChanged(position);


                            } else if (likeResponse.getSuccess() == false) {

                                final AlertDialog alertDialog = new AlertDialog.Builder(
                                        StatePost.this).create();

                                alertDialog.setTitle("                 Alert!");


                                alertDialog.setMessage("    Try Again");

                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        alertDialog.dismiss();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LikeResponse> call, Throwable t) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onSharePostClick(int position) {
                Log.d(TAG, "onSharePostClick: " + position);

                Intent intent = new Intent(StatePost.this, SharePostActivity.class);

                if (postDataList.get(position).getType().contains("image")) {

                    intent.putExtra("image", postDataList.get(position).getValue());
                    intent.putExtra("token", UserToken);
                    intent.putExtra("post_id", postDataList.get(position).getId());
                    intent.putExtra("profileimage", postDataList.get(position).getUser().getImage());

                } else if (postDataList.get(position).getType().contains("video")) {
                    intent.putExtra("video", postDataList.get(position).getValue());
                    intent.putExtra("token", UserToken);
                    intent.putExtra("post_id", postDataList.get(position).getId());
                    intent.putExtra("profileimage", postDataList.get(position).getUser().getImage());


                } else if (postDataList.get(position).getType().contains("audio")) {
                    intent.putExtra("audio", postDataList.get(position).getValue());
                    intent.putExtra("token", UserToken);
                    intent.putExtra("post_id", postDataList.get(position).getId());
                    intent.putExtra("profileimage", postDataList.get(position).getUser().getImage());


                } else if (postDataList.get(position).getType().contains("")) {

                    intent.putExtra("text", postDataList.get(position).getMessage());
                    intent.putExtra("token", UserToken);
                    intent.putExtra("post_id", postDataList.get(position).getId());
                    intent.putExtra("profileimage", postDataList.get(position).getUser().getImage());

                }
                startActivity(intent);
            }

            @Override
            public void onShareImageClick(int position) {
                Log.d(TAG, "onShareImageClick: " + position);

                Intent intent = new Intent(StatePost.this, SharePostActivity.class);

                if (postDataList.get(position).getType().contains("image")) {

                    intent.putExtra("image", postDataList.get(position).getValue());
                    intent.putExtra("token", UserToken);
                    intent.putExtra("post_id", String.valueOf(postDataList.get(position).getId()));
                    intent.putExtra("profileimage", postDataList.get(position).getUser().getImage());
                    intent.putExtra("text", postDataList.get(position).getMessage());

                } else if (postDataList.get(position).getType().contains("video")) {
                    intent.putExtra("video", postDataList.get(position).getValue());
                    intent.putExtra("token", UserToken);
                    intent.putExtra("post_id", String.valueOf(postDataList.get(position).getId()));
                    intent.putExtra("profileimage", postDataList.get(position).getUser().getImage());
                    intent.putExtra("text", postDataList.get(position).getMessage());


                } else if (postDataList.get(position).getType().contains("audio")) {
                    intent.putExtra("audio", postDataList.get(position).getValue());
                    intent.putExtra("token", UserToken);
                    intent.putExtra("post_id", String.valueOf(postDataList.get(position).getId()));
                    intent.putExtra("profileimage", postDataList.get(position).getUser().getImage());
                    intent.putExtra("text", postDataList.get(position).getMessage());


                } else if (postDataList.get(position).getType().contains("")) {

                    intent.putExtra("text", postDataList.get(position).getMessage());
                    intent.putExtra("token", UserToken);
                    intent.putExtra("post_id", String.valueOf(postDataList.get(position).getId()));
                    intent.putExtra("profileimage", postDataList.get(position).getUser().getImage());

                }
                startActivity(intent);

            }

            @Override
            public void onDotClick(int position) {
                Log.d(TAG, "onDotClick: " + position);

                init_modal_bottomsheet(position);
                // openDialog(position);

            }

            @Override
            public void onUserImageClick(int position) {


                Intent intent = new Intent(StatePost.this, OtherUserProfile.class);
                intent.putExtra("user_url", postDataList.get(position).getUser().getUrl());
                intent.putExtra("user_image", postDataList.get(position).getUser().getImage());
                intent.putExtra("user_cover_image", postDataList.get(position).getUser().getCoverImage());
                intent.putExtra("first_name", postDataList.get(position).getUser().getFirstName());
                intent.putExtra("last_name", postDataList.get(position).getUser().getLastName());
                intent.putExtra("email", postDataList.get(position).getUser().getEmail());
                intent.putExtra("dob", postDataList.get(position).getUser().getDob());
                intent.putExtra("profession", postDataList.get(position).getUser().getProfession());
                intent.putExtra("gender", postDataList.get(position).getUser().getGender());
                intent.putExtra("mobile", postDataList.get(position).getUser().getMobile());
                intent.putExtra("marital", postDataList.get(position).getUser().getMaritalStatus());
                intent.putExtra("address", postDataList.get(position).getUser().getAddress());
                intent.putExtra("bypost","postsend");

                startActivity(intent);



            }
        });


    }







    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, StatePost.this);
    }

    public void loadFirstPage() {
        //swipeRefreshLayout.setRefreshing(true);
        Log.d(TAG, "load 1 Page: " + currentPage);
        initFeedApi().enqueue(new Callback<PostFeedResponse>() {
            @Override
            public void onResponse(Call<PostFeedResponse> call, Response<PostFeedResponse> response) {
                progressBar.setVisibility(View.GONE);
                //swipeRefreshLayout.setRefreshing(false);
                state_posts = new State_Posts();
                state_posts = fetchCityPost(response);
                //cityPosts.
                currentPage = Integer.parseInt(String.valueOf(state_posts.getCurrentPage()));
                TOTAL_PAGES = Integer.parseInt(String.valueOf(state_posts.getLastPage()));

                nextList = state_posts.getData();
                if (postDataList.size() > 0) {

                    postDataList.clear();
                }
                postDataList.addAll(nextList);
                postAdapter.refreshAdapter(postDataList);


                if (currentPage <= TOTAL_PAGES) postAdapter.addLoadingFooter();
                else isLastPage = true;


            }

            @Override
            public void onFailure(Call<PostFeedResponse> call, Throwable t) {
                //swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    private void loadNextPage() {

        Log.d(TAG, "loadNextPage: " + currentPage);


        callFeedApi().enqueue(new Callback<PostFeedResponse>() {
            @Override
            public void onResponse(Call<PostFeedResponse> call, Response<PostFeedResponse> response) {


                postAdapter.removeLoadingFooter();
                isLoading = false;
                state_posts = fetchCityPost(response);
                currentPage = Integer.parseInt(String.valueOf(state_posts.getCurrentPage()));
                TOTAL_PAGES = Integer.parseInt(String.valueOf(state_posts.getLastPage()));
                nextList = state_posts.getData();
                postDataList.addAll(nextList);
                postAdapter.addAll(nextList);


                if (currentPage != TOTAL_PAGES) postAdapter.addLoadingFooter();
                else {

                    isLastPage = true;
                    currentPage = 1;
                }


            }

            @Override
            public void onFailure(Call<PostFeedResponse> call, Throwable t) {

            }
        });


    }

    private State_Posts fetchCityPost(Response<PostFeedResponse> response) {
        PostFeedResponse body = response.body();
        return body.getState_posts();
    }

    private Call<PostFeedResponse> callFeedApi() {
        return apiInterface.getStateFeeds(UserToken, currentPage + "");
    }

    private Call<PostFeedResponse> initFeedApi() {
        return apiInterface.getStateFeeds(UserToken, "1");
    }

//    @Override
//    public void onRefresh() {
//        loadFirstPage();
//    }






    public void init_modal_bottomsheet(final int position) {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.modal_bottomsheet, null);


        dialog = new BottomSheetDialog(this);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();


        btn_cancel = (Button) modalbottomsheet.findViewById(R.id.btn_cancel);
        reportspam = (Button) modalbottomsheet.findViewById(R.id.ReportSpam);
        delete_sheet = (Button) modalbottomsheet.findViewById(R.id.Delete);
        download = (Button) modalbottomsheet.findViewById(R.id.DownLoad);

//    if(!nextList.get(position).user.getId().equals(user_id)){
//
//       delete.setEnabled(false);
//       delete.setTextColor(Color.parseColor("#808080"));
//
//    }


        reportspam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+position+""+nextList.get(position).getId());
                Intent intent = new Intent(StatePost.this, FeedBackActivity.class);
                intent.putExtra("postid",String.valueOf(postDataList.get(position).getId()));
                startActivity(intent);
                dialog.hide();


            }
        });

        delete_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                        StatePost.this);

                // set title
                alertDialogBuilder.setTitle("                    Alert!");

                // set dialog message
                alertDialogBuilder
                        .setMessage("      Are you surely want to delete")
                        .setCancelable(false)
                        .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {

                                pDialog = new ProgressDialog(StatePost.this);
                                pDialog.setMessage("Please wait...");
                                pDialog.setCancelable(true);
                                pDialog.show();

                                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                                Call<PostDeleteResponse> call = apiInterface.deltePost(UserToken, String.valueOf(postDataList.get(position).getId()));

                                call.enqueue(new Callback<PostDeleteResponse>() {
                                    @Override
                                    public void onResponse(Call<PostDeleteResponse> call, Response<PostDeleteResponse> response) {
                                        pDialog.cancel();

                                        PostDeleteResponse postDeleteResponse = response.body();
                                        isSucess = postDeleteResponse.getSuccess();
                                        Log.d(TAG, "onResponse: " + isSucess);
                                        //  notifyItemRangeChanged(position, event_list.size());


                                        if (isSucess == true) {

                                            nextList.remove(position);
                                            postAdapter.notifyItemRemoved(position);
                                            dialog.dismiss();
                                            //   alertDialog.dismiss();
                                            //dialog.dismiss();

                                            Toast.makeText(StatePost.this,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                                        } else if (isSucess == false) {

                                            Toast.makeText(StatePost.this,"Please Try Again",Toast.LENGTH_SHORT).show();
                                            // dialog.dismiss();

                                        }


                                    }

                                    @Override
                                    public void onFailure(Call<PostDeleteResponse> call, Throwable t) {


                                    }
                                });






                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();

                            }
                        });

                // create alert dialog
                android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();



            }
        });

        // alertDialog.show();


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PostActivity.this, "DownLoad Complete", Toast.LENGTH_SHORT).show();

                if (EasyPermissions.hasPermissions(StatePost.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Glide.with(StatePost.this)
                            .load("http://emergingncr.com/mangalcity/public/images/post/post_image/"+postDataList.get(position).getValue())
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>(100,100) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {
                                    saveImage(resource);
                                }
                            });
                }
                else {
                    EasyPermissions.requestPermissions(StatePost.this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                }


            }
        });




        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });
    }

    private String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "JPEG_" + "FILE_NAME" + ".jpg";
        File storageDir = new File(            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "Mangal");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            Toast.makeText(StatePost.this, "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                // Respond to the action bar's Up/Home button
                // adapter.notifyDataSetChanged();

                finish();
                // NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
package net.clamour.mangalcity.citypost;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.clamour.mangalcity.Home.FilePath;
import net.clamour.mangalcity.Home.OtherUserProfile;
import net.clamour.mangalcity.Home.PaginationScrollListener;
import net.clamour.mangalcity.Home.SearchAdapter;
import net.clamour.mangalcity.MainActivity;
import net.clamour.mangalcity.PostTabs.CommentAdapter;
import net.clamour.mangalcity.PostTabs.FeedBackActivity;
import net.clamour.mangalcity.PostTabs.PostAdapter;
import net.clamour.mangalcity.PostTabs.SharePostActivity;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.CityPostResponse;
import net.clamour.mangalcity.ResponseModal.FeedsResponse;
import net.clamour.mangalcity.ResponseModal.LikeResponse;
import net.clamour.mangalcity.ResponseModal.PostDeleteResponse;
import net.clamour.mangalcity.ResponseModal.PostResponse;
import net.clamour.mangalcity.countrypost.CountryPost;
import net.clamour.mangalcity.districtpost.DistrictPost;
import net.clamour.mangalcity.feed.CityPosts;
import net.clamour.mangalcity.feed.CommentShowData;
import net.clamour.mangalcity.feed.FeedPostData;
import net.clamour.mangalcity.feed.MediaImageResponse;
import net.clamour.mangalcity.feed.PostCommentResponse;
import net.clamour.mangalcity.feed.PostFeedResponse;
import net.clamour.mangalcity.profile.UserProfile;
import net.clamour.mangalcity.statepost.StatePost;
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
import static android.content.Context.MODE_PRIVATE;

public class CityTab extends android.support.v4.app.Fragment implements EasyPermissions.PermissionCallbacks {


//    @BindView(R.id.button_country)
//    Button buttonCountry;
//    @BindView(R.id.button_state)
//    Button buttonState;
//    @BindView(R.id.button_district)
//    Button buttonDistrict;
//    @BindView(R.id.search_icon)
//    ImageView search_icon;


    ProgressDialog pDialog;
    ApiInterface apiInterface;
    Boolean isSucess, isSucessPost;
    String fullPath;
    private List<String> imagePathList;

    private static final String TAG = "PostActivity";
    //   List<String>imageFilePath=new ArrayList<>();


    List<FeedPostData> postDataList;
    PostAdapter postAdapter;
    private RecyclerView recyclerView;

    String UserToken, profile_image;
    SharedPreferences LoginPrefrences,profileprefrences;
    ListView searchList;
    EditText searchDialogEdit;
    SearchAdapter searchAdapter;
    String name,city,image;
    AlertDialog.Builder builder;
    AlertDialog alertDialog_search;
    Button btn_cancel,reportspam,delete_sheet,download;

    private AsyncTask mMyTask;
    PopupWindow popWindow;
    EditText postedit;
    String postcommet_st;
    CommentAdapter commentAdapter;



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
    public static BottomSheetDialog dialog;
    BottomSheetBehavior bottomSheetBehavior;

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
    private CityPosts cityPosts;
    private String user_id;
    List<FeedPostData> nextList;
    List<CommentShowData>showComment_array;
    String comment_message,comment_id,user_imagecomment,comment_post_id;

    String character, character_dialog;
    Boolean isSucessSearch;
    Boolean isEXist=false;
    View view;

    String url = "http://ichef.bbci.co.uk/onesport/cps/480/cpsprodpb/11136/production/_95324996_defoe_rex.jpg";
    File file;
    String dirPath, fileName;
    private String imagePath1;
    String imageEncoded;
    List<String> imagesEncodedList;
    List<CommentShowData>comment_array;
    String homeLocationchecked,homelocationnotchecked;
    List<String>imageFilePath;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final   View v = inflater.inflate(R.layout.activity_post, container, false);



        profileprefrences = getActivity().getSharedPreferences("net.clamour.mangalcity.profile.UserProfile", MODE_PRIVATE);

        LoginPrefrences = getActivity().getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        UserToken = LoginPrefrences.getString("userToken", "");
        Log.i("UserToken", UserToken);
        user_id = LoginPrefrences.getString("user_id", "");
        profile_image = LoginPrefrences.getString("profileImage", "");
//
//        if(profileprefrences.contains("")){
//
//
//        }

        progressBar = (ProgressBar)v.findViewById(R.id.main_progress);
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerview);
//        swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);

        postDataList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        postAdapter = new PostAdapter(getActivity());
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
                Log.d(TAG, "loadMoreItems: Loading");
                isLoading = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            protected void loadMoreItems() {
                Log.e("Position", "Last item reached");
//                Log.d(TAG, "loadMoreItems: Loading");
//                isLoading = true;
//                currentPage += 1;
//                loadNextPage();

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
        postAdapter.setOnPostClickListner(new PostAdapter.OnPostClickListner() {
            @Override
            public void onPostButtonClick(String msz) {
                //message = editTextTextpost.getText().toString();
                Log.d(TAG, "input : " + imagepath + "\n" + videopath + "\n" + message + "\n" + audiopath);
//        if(msz.isEmpty()&&imagepath.isEmpty()&&videopath.isEmpty()&&audiopath.isEmpty()){
//            postAdapter.desableButton();
//        }
//        else {
                // postAdapter.enableButton();
                 uploadImage();
                pjUploadMultiFile(msz, imagesEncodedList, videopath, audiopath);}
            //}

            @Override
            public void onImageButttonClick() {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("image/*");
                openGalleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(openGalleryIntent, REQUEST_IMAGE_CODE);
            }

            @Override
            public void onVideoButttonClick() {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("video/*");
                startActivityForResult(openGalleryIntent, REQUEST_VIDEO_CODE);
            }

            @Override
            public void onAudioButtonClick() {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Audio"), REQUEST_AUDIO_CODE);
            }

            @Override
            public void OnCrossImageClick() {
                imagepath="";
                videopath="";
                audiopath="";
            }
        });



        postAdapter.setOnItemClickListner(new PostAdapter.OnItemClickListner() {
            @Override
            public void onLikeClick(final int position) {
                Log.d(TAG, "onLikeClick: " + position);
                final FeedPostData feedPostData = postDataList.get(position);
                try {
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(true);
                    pDialog.show();
                    Call<LikeResponse> call = apiInterface.like_post(UserToken, feedPostData.getId() + "");
                    call.enqueue(new Callback<LikeResponse>() {
                        @Override
                        public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {

                            LikeResponse likeResponse = response.body();
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
                                        getActivity()).create();

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
            public void OnCommentTextClick(int position) {





                showCommentPopup(v,position);
            }

            @Override
            public void onDislikeClick(final int position) {
                Log.d(TAG, "onDislikeClick: " + position);
                pDialog = new ProgressDialog(getActivity());
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
                                        getActivity()).create();

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

                Intent intent = new Intent(getActivity(), SharePostActivity.class);

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

                Intent intent = new Intent(getActivity(), SharePostActivity.class);

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
            public void onShareTextClick(int position) {
                Log.d(TAG, "onShareImageClick: " + position);

                Intent intent = new Intent(getActivity(), SharePostActivity.class);

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
            public void onCommentImageClick(int position) {

                List<CommentShowData> commentList = postDataList.get(position).getComment();
                comment_array= new ArrayList<>();

                if(commentList!=null){
                    for(CommentShowData commentShowData :commentList){
                        comment_message=commentShowData.getMessage();
                        Log.d(TAG, "commentListtttt: "+comment_message);
                        comment_id=commentShowData.getId();
                        comment_post_id=commentShowData.getPost_id();
                        comment_array.add(commentShowData);
                    }
                }
                showCommentPopup(v,position);
            }

            @Override
            public void onUserImageClick(int position) {

//                if(user_id.equals(nextList.get(position).getUser().getId())){
//
//                    userImage.setEnabled(false);
//                }
//                else {

                Intent intent = new Intent(getActivity(), OtherUserProfile.class);
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
                //    }


            }

        });




        return v;
    }

    public void openDialog(final int position) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());


        LayoutInflater inflater = LayoutInflater.from(getActivity());


        View subView = inflater.inflate(R.layout.feedbackdialog, null);
        builder.setView(subView);
        alertDialog = builder.create();

        feedback = subView.findViewById(R.id.feedback);
        delete = subView.findViewById(R.id.delete);


        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), FeedBackActivity.class);
                startActivity(intent);


            }
        });
        if (user_id.equalsIgnoreCase(String.valueOf(postDataList.get(position).getUserId()))) {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();

                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(true);
                    pDialog.show();


                    Call<PostDeleteResponse> call = apiInterface.deltePost(UserToken, String.valueOf(postDataList.get(position).getId()));

                    call.enqueue(new Callback<PostDeleteResponse>() {
                        @Override
                        public void onResponse(Call<PostDeleteResponse> call, Response<PostDeleteResponse> response) {
                            pDialog.cancel();

                            PostDeleteResponse postDeleteResponse = response.body();
                            if (response.body() != null)
                                isSucess = postDeleteResponse.getSuccess();
                            Log.d(TAG, "onResponse: " + isSucess);


                            if (isSucess == true) {

                                postAdapter.remove(postDataList.get(position));
                                postDataList.remove(position);
                                dialog.hide();
                                //postAdapter.notifyDataSetChanged();

                                final AlertDialog alertDialog = new AlertDialog.Builder(
                                        getActivity()).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("                 Alert!");

                                // Setting Dialog Message
                                alertDialog.setMessage("            Post Deleted Successfully");

                                // Setting Icon to Dialog


                                // Setting OK Button
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        alertDialog.dismiss();

                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();

                            } else if (isSucess == false) {

                                final AlertDialog alertDialog = new AlertDialog.Builder(
                                        getActivity()).create();

                                alertDialog.setTitle("                 Alert!");

                                // Setting Dialog Message
                                alertDialog.setMessage("    Please Try Again");

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
                        public void onFailure(Call<PostDeleteResponse> call, Throwable t) {

                        }
                    });


                }
            });
        } else {
            delete.setVisibility(View.INVISIBLE);
        }


        alertDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
                uri = data.getData();

                if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    imagesEncodedList = new ArrayList<String>();
                    if (data.getData() != null) {

                        Uri mImageUri = data.getData();

                        // Get the cursor
                        Cursor cursor = getActivity().getContentResolver().query(mImageUri,
                                filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded = cursor.getString(columnIndex);
                        cursor.close();

                    } else {
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                mArrayUri.add(uri);
                                // Get the cursor
                                Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                                // Move to first row
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                imageEncoded = cursor.getString(columnIndex);
                                imagesEncodedList.add(imageEncoded);
                                cursor.close();

//                            File file = new File(uri.getPath());
//                            String[] filePath = file.getPath().split(":");
//                            String image_id = filePath[filePath.length - 1];

                            }
                            Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                            Log.v(TAG, "getImageFilePath: "+imagesEncodedList.get(0)+" "+imagesEncodedList.get(1)+"   "+mArrayUri.size()+"    "+mArrayUri.get(0));
                            Toast.makeText(getActivity(),imagesEncodedList.get(0)+"   "+mArrayUri.size()+"    "+mArrayUri.get(0),Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }}
        }
        catch(Exception e){
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
//        super.onActivityResult(requestCode, resultCode, data);



//                final String imagepathSelected = getRealPathFromURIPath(uri, getActivity());
//                File imageFile = new File(imagepathSelected);
//                double maxFileSize = 5.00;
//                double selectedImageFileSize = getFileSizeMegaBytes(imageFile);
//                Log.d(TAG, "selectedImageFileSize " + selectedImageFileSize);
//                Log.d(TAG, "maxFileSize " + maxFileSize);
//                if (selectedImageFileSize < maxFileSize) {
//                    imagepath = imagepathSelected;
//                    postAdapter.setImageUri(uri);
//                    postAdapter.setSelectionData();
//
//                //    postAdapter.cleanData(imagepath);


//                else {
//                    Toast.makeText(getActivity(), "File Size is greater than 5MB ", Toast.LENGTH_LONG).show();
//                }

        Log.d(TAG, "Filename " + imagepath);

//             else {
//                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
//            }

        if (requestCode == REQUEST_VIDEO_CODE && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String videopathSelected = getPath(uri, getActivity());
                File videoFile = new File(videopathSelected);
                double maxFileSize = 25.00;
                double selectedVideoFileSize = getFileSizeMegaBytes(videoFile);
                if (selectedVideoFileSize < maxFileSize) {
                    videopath = videopathSelected;
                    Toast.makeText(getActivity(),videopath,Toast.LENGTH_SHORT).show();
                    postAdapter.setSelectionDataVideo();
                    //  selected_file.setVisibility(View.VISIBLE);
                    //  selected_file.setText("Video Selected");
                    // imagepreview.setImageResource(0);
                    // cross_image.setVisibility(View.VISIBLE);
//                    cross_image.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            videopath="";
//                            selected_file.setVisibility(View.INVISIBLE);
//                            imagepreview.setImageResource(0);
//                            cross_image.setVisibility(View.INVISIBLE);
//                        }
//                    });


                } else {
                    Toast.makeText(getActivity(), "File Size is greater than 25MB ", Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, "Filename " + videopath);

            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        if (requestCode == REQUEST_AUDIO_CODE && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {


                Uri selectedFileUri = data.getData();
                String selectedFilePath = FilePath.getPath(getActivity(), selectedFileUri);
                Log.i(TAG, "Selected File Path:" + selectedFilePath);


                audiopath = selectedFilePath;

                Toast.makeText(getActivity(), selectedFilePath, Toast.LENGTH_SHORT).show();
                postAdapter.setSelectionDataAudio();


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
        String[] projection = {MediaStore.Video.Media.DATA};
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
        return (double) file.length() / (1024 * 1024);
    }


    private void pjUploadMultiFile(String message,List<String> imageFilePath, String videoFilePath, String audioFilePath) {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();
     //   imageFilePath=new ArrayList<>();
        File videoFile = new File(videoFilePath);
        File audioFile = new File(audioFilePath);

        // Log.d(TAG, "pjUploadMultiFilenameeeeimagepathhh: "+imageFilePath.size());




        MediaType mediaType = MediaType.parse("multipart/form-data");

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Log.d(TAG, "pjUploadMultiFile: "  + "  " + videoFile.getName() + " " + audioFile.getName());
        builder.addFormDataPart("token", UserToken);
        builder.addFormDataPart("message", message);
        if (!imageFilePath.isEmpty()) {

            for (int i = 0; i <imageFilePath.size() ; i++) {
                File imageFile = new File(imageFilePath.get(i));
                Log.d(TAG, "pjUploadMultiFilenameeee: "+imageFile.getName());
                // RequestBody requestImage = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                builder.addFormDataPart("image[]", imageFile.getName(), RequestBody.create(mediaType, imageFile));
                // builder.addFormDataPart("event_images[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                //  }
            }}
        Log.d(TAG, "pjUploadMultiFilenameeeeimagepathhhafterloop: "+imageFilePath.size());

        if (!videoFilePath.equalsIgnoreCase("")) {
            builder.addFormDataPart("video", videoFile.getName(), RequestBody.create(mediaType, videoFile));
        }
        if (!audioFilePath.equalsIgnoreCase("")) {
            builder.addFormDataPart("audio", audioFile.getName(), RequestBody.create(mediaType, audioFile));
        }
        MultipartBody requestBody = builder.build();
        Call<PostResponse> fileUpload = apiInterface.postData("multipart/form-data; boundary=" + requestBody.boundary(), requestBody);
        fileUpload.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                pDialog.cancel();
                // imagepreview.setImageResource(0);
                //  postAdapter.restForm();
                try {
                    PostResponse postResponse = response.body();
                    isSucessPost = postResponse.getSuccess();
                    Log.i("sucesspost", isSucessPost.toString());

                }
                catch (Exception e){


                }


                if (isSucessPost == true) {
                    pDialog.dismiss();
                    imagepath = "";
                    //imageFilePath.clear();
                    //  editTextTextpost.getText().clear();
                    audiopath = "";
                    videopath = "";
                    postAdapter.restForm();
                    //    postAdapter.clear();

                    loadFirstPage();
                    //  selected_file.setVisibility(View.INVISIBLE);

                    //   postAdapter.notifyDataSetChanged();
                    //  loadFirstPage();
                    //  loadNextPage();

                    Toast.makeText(getActivity(), "Successfully Posted", Toast.LENGTH_SHORT).show();
                } else if (isSucessPost == false) {

                    Toast.makeText(getActivity(), "Please enter some  Text,video,Image or Audio", Toast.LENGTH_SHORT).show();


                }

            }


            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d(TAG, "Succcess " + t.getMessage());
            }
        });

    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_IMAGE_CODE) {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
            openGalleryIntent.setType("image/*");
            startActivityForResult(openGalleryIntent, REQUEST_IMAGE_CODE);
        } else if (requestCode == REQUEST_VIDEO_CODE) {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
            openGalleryIntent.setType("video/*");
            startActivityForResult(openGalleryIntent, REQUEST_VIDEO_CODE);
        } else if (requestCode == REQUEST_AUDIO_CODE) {
            Intent intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Audio"), REQUEST_AUDIO_CODE);
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }


    public void loadFirstPage() {
        //swipeRefreshLayout.setRefreshing(true);
        Log.d(TAG, "load 1 Page: " + currentPage);
        initFeedApi().enqueue(new Callback<PostFeedResponse>() {
            @Override
            public void onResponse(Call<PostFeedResponse> call, Response<PostFeedResponse> response) {



                progressBar.setVisibility(View.GONE);
                //swipeRefreshLayout.setRefreshing(false);
                cityPosts = new CityPosts();
                cityPosts = fetchCityPost(response);
                //cityPosts.
                currentPage = Integer.parseInt(String.valueOf(cityPosts.getCurrentPage()));
                TOTAL_PAGES = Integer.parseInt(String.valueOf(cityPosts.getLastPage()));

                nextList = cityPosts.getData();
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
                cityPosts = fetchCityPost(response);
                currentPage = Integer.parseInt(String.valueOf(cityPosts.getCurrentPage()));
                TOTAL_PAGES = Integer.parseInt(String.valueOf(cityPosts.getLastPage()));
                nextList = cityPosts.getData();
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

    private CityPosts fetchCityPost(Response<PostFeedResponse> response) {
        PostFeedResponse body = response.body();


        return body.getCityPosts();
    }

    private Call<PostFeedResponse> callFeedApi() {
        return apiInterface.getFeeds(UserToken, currentPage + "");
    }

    private Call<PostFeedResponse> initFeedApi() {
        return apiInterface.getFeeds(UserToken, "1");
    }

//    @Override
//    public void onRefresh() {
//        loadFirstPage();
//    }


//    public void openSearchPopup() {
//
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View subView = inflater.inflate(R.layout.search_pop, null);
//        searchList = (ListView) subView.findViewById(R.id.list_search);
//        searchDialogEdit = (EditText) subView.findViewById(R.id.looking_for_dialog);
//
//        searchDialogEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//
//
//                character_dialog = searchDialogEdit.getText().toString();
//                if (start == 0 && count == 1) {
//                    changeText();
//
//                } else if (charSequence.toString().length() > 0) {
//                    character_dialog = charSequence.toString();
//
//                    //filtered_array=new JSONArray();
//                    int count1 = filtered_array.length();
//                    for (int i = 0; i < count1; i++) {
//
//                        Log.i("beforefiltered", filtered_array.length() + "");
//                        filtered_array.remove(0);
//
//                    }
//                    Log.i("afteredfiltered", filtered_array.length() + "");
//                    Log.i("", "filtered_array ");
//                    for(int i=0;i<jsonArray.length();i++) {
//                        try {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//
//                            name = jsonObject.getString("name");
//                            Log.i("namesearchhh",name);
//                            city=jsonObject.getString("city");
//                            image=jsonObject.getString("image");
//                            Log.d(TAG, "getView: "+image);
//
//
//                            if(name.toLowerCase().contains(character_dialog.toLowerCase())){
//
//                                isEXist=true;
//
//
//
//
//
//
//                            }
//
//
//
//
//                            if(isEXist==true)
//                            {
//                                filtered_array.put(jsonObject);
//                                isEXist =false;
//                            }
//
//                        }
//                        catch (Exception e){
//
//                        }
//
//                    }
//                    Log.i("responsearraynotify",jsonArray.length()+"");
//                    Log.i("fiterednotify",filtered_array.length()+"");
//
//                    searchAdapter.notifyDataSetChanged();
//                    Log.i("responsearraynotify1",jsonArray.length()+"");
//                    Log.i("fiterednotify1",filtered_array.length()+"");
//
//
//                    // changeText();
//                }
//                else
//                {
//                    //When all char deleted
////                    search_details.clear();
////                    filtered.clear();
////                    looking.getText().clear();
//                    filtered_array.remove(start);
//
//                }
//
//
//            }
//
//
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//
//
//
//        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
//
//
//        builder = new AlertDialog.Builder(getActivity());
//
//        builder.setView(subView);
//        alertDialog_search = builder.create();
//
////        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////               // textInfo.setText(subEditText.getText().toString());
////            }
////        });
////
////        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();
////            }
////        });
//
//        builder.show();
//    }


    public void changeText() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);


        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/search",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG).show();

                        Log.i("responsegetProfile", response);


                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        Log.e("response=", response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("data");
                            //for (int i=0;i<jsonArray.length();i++){


                            // Log.i("pppppppppp",search_details.toString());
                            // Log.i("oooooooooo",color.toString());
                            JSONArray ff = new JSONArray();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ff.put(jsonArray.get(i));
                                // Log.i("filtereeeddloop",filtered_array.length()+"");


                            }
                            filtered_array = ff;

                            setList(filtered_array);

                            Log.i("filtereeedd", ff.length() + "");

                            // }


                        } catch (Exception e) {

                            Log.i("filtereeedd55555", "exxeption" + e);

                        }

                        if (filtered_array.length() == 0) {

                            Toast.makeText(getActivity(),"No Result Found",Toast.LENGTH_SHORT).show();
                            // final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                            //         PostActivity.this).create();

                            // Setting Dialog Title
//                            alertDialog.setTitle("                 Alert!");
//
//                            // Setting Dialog Message
//                            alertDialog.setMessage("            No Result Found");
//
//                            // Setting Icon to Dialog
//
//
//                            // Setting OK Button
//                            alertDialog.setButton("OK", new android.content.DialogInterface.OnClickListener() {
//                                public void onClick(android.content.DialogInterface dialog, int which) {
//                                    alertDialog.dismiss();
//                                    pDialog.dismiss();
//                                    alertDialog_search.dismiss();
//
//                                }
//                            });
//
//                            // Showing Alert Message
//                            alertDialog.show();
                        }


                    }
                },
                new com.android.volley.Response.ErrorListener() {
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
                params.put("q", character_dialog);


                return params;
            }

        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(getActivity());
        requestQueue1.add(stringRequest1);

    }

    public void setList(final JSONArray arr) {


        // Log.i("list search count",search_details.size()+"");


//    if (adapter == null)
//    {
        searchAdapter = new SearchAdapter(getActivity(), R.layout.list_search, filtered_array);
        searchList.setAdapter(searchAdapter);


        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    JSONObject object = arr.getJSONObject(position);
                    Intent intent=new Intent(getActivity(),OtherUserProfile.class);
                    intent.putExtra("user_url",object.getString("url"));
                    intent.putExtra("bypost","searchsend");
                    startActivity(intent);
                    alertDialog_search.dismiss();
                    //alertDialog_search.cancel();
                    // alertDialog.dismiss();
                }
                catch (Exception e){


                }
                alertDialog_search.dismiss();
                //  alertDialog_search.cancel();
                // alertDialog.dismiss();







            }
        });


    }

    public void init_modal_bottomsheet(final int position) {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.modal_bottomsheet, null);


        dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
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
                Intent intent = new Intent(getActivity(), FeedBackActivity.class);
                intent.putExtra("postid",String.valueOf(postDataList.get(position).getId()));
                startActivity(intent);
                dialog.hide();


            }
        });
        if (user_id.equalsIgnoreCase(String.valueOf(postDataList.get(position).getUserId()))) {
            delete_sheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                            getActivity());

                    // set title
                    alertDialogBuilder.setTitle("                    Alert!");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("      Are you surely want to delete")
                            .setCancelable(true)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int id) {

                                    pDialog = new ProgressDialog(getActivity());
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

                                                postAdapter.remove(postDataList.get(position));
                                                postDataList.remove(position);
                                                ;
                                                //   alertDialog.dismiss();
                                                //dialog.dismiss();

                                                Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            } else if (isSucess == false) {

                                                Toast.makeText(getActivity(), "Please Try Again", Toast.LENGTH_SHORT).show();
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
        }
        else {

            delete_sheet.setVisibility(View.GONE);
        }



        // alertDialog.show();


        Log.d(TAG, "init_modal_bottomsheet: "+"insideeeeeeeeeeee");
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PostActivity.this, "DownLoad Complete", Toast.LENGTH_SHORT).show();


                if (postDataList.get(position).getType().equalsIgnoreCase("image") && EasyPermissions.hasPermissions(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Glide.with(getActivity())
                            .load("http://emergingncr.com/mangalcity/public/images/post/post_image/" + postDataList.get(position).getValue())
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>(100, 100) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                    saveImage(resource);
                                }
                            });
                }
//             else if (postDataList.get(position).getType().equalsIgnoreCase("video") && EasyPermissions.hasPermissions(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                 // ProgressBack PB = new ProgressBack();
//                 //   PB.execute("");
//
//
//             }

//                else if {
//                    EasyPermissions.requestPermissions(getActivity(), getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//                }


            }
        });

        //}

// else {
//     //download.setVisibility(View.GONE);
// }


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });
    }

    private String saveImage(Bitmap image) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        String savedImagePath = null;

        String imageFileName = "JPEG_" + System.currentTimeMillis()+ ".jpg";
        File storageDir = new File(            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "Mangal");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            pDialog.cancel();
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
            dialog.hide();
            Toast.makeText(getActivity(), "IMAGE SAVED TO GALLERY", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public void showCommentPopup(View v,final int position){


        List<CommentShowData> commentList = postDataList.get(position).getComment();
        comment_array= new ArrayList<>();

        if(commentList!=null){
            for(CommentShowData commentShowData :commentList){
                comment_message=commentShowData.getMessage();
                Log.d(TAG, "commentListtttt: "+comment_message);

                comment_id=commentShowData.getId();
                comment_post_id=commentShowData.getPost_id();
                comment_array.add(commentShowData);
            }
        }

        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View subView = layoutInflater.inflate(R.layout.commentpopup, null);
        // inflate the custom popup layout
        // find the ListView in the popup layout
        final ListView listView = (ListView)subView.findViewById(R.id.commentsListView);
        LinearLayout headerView = (LinearLayout)subView.findViewById(R.id.headerLayout);
        postedit=(EditText)subView.findViewById(R.id.writeComment);
        ImageView postButton=(ImageView) subView.findViewById(R.id.post_button);
        commentAdapter=new CommentAdapter(getActivity(),comment_array);
        listView.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(true);
                pDialog.show();
                postcommet_st=postedit.getText().toString();
                Log.d(TAG, "postComment: "+postcommet_st);
                //Log.d(TAG, "postComment: "+comment_array.get(position).getPost_id());


                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<PostCommentResponse> call = apiInterface.postComment(UserToken,String.valueOf(postDataList.get(position).getId()),postcommet_st,"0");

                call.enqueue(new Callback<PostCommentResponse>() {
                    @Override
                    public void onResponse(Call<PostCommentResponse> call, Response<PostCommentResponse> response) {
                        pDialog.cancel();
                        try {
                            PostCommentResponse   postCommentResponse = response.body();
                            isSucess = postCommentResponse.getSuccess();
                            Log.d(TAG, "comment_array onResponse: " + postCommentResponse.toString());
                            CommentShowData csd = new CommentShowData();
                            if (postCommentResponse !=null) {
                                postedit.setText("");
                                csd.setImage(postCommentResponse.getUser_image());
                                csd.setMessage(postCommentResponse.getComment());
                                csd.setPost_id(postCommentResponse.getComment_id());
                                csd.setFirst_name(postCommentResponse.getName());
                                csd.setParent_id(postCommentResponse.getPost_id());
                                comment_array.add(csd);
                                commentAdapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e){
                        }

                    }

                    @Override
                    public void onFailure(Call<PostCommentResponse> call, Throwable t) {

                    }
                });
            }
        });


        // get device size
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
//        mDeviceHeight = size.y;
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;


        // set height depends on the device size
        popWindow = new PopupWindow(subView, width,height-50, true );
        // set a background drawable with rounders corners
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.screen_background));

        popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popWindow.setAnimationStyle(R.style.PopupAnimation);

        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0,100);

    }





    private void downloadFile(String fileURL, String fileName) {
        try {
            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "Video";
            File rootFile = new File(rootDir);
            rootFile.mkdir();
            URL url = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    fileName));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
        } catch (IOException e) {
            Log.d("Error....", e.toString());
        }}
    public void setCommentList(ListView listView){


    }

    public void getImageFilePath(Uri uri) {

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        Cursor cursor = getActivity().getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor != null && cursor.moveToFirst()) {
            // cursor.moveToFirst();
            imagePath1 = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            imagePathList.add(imagePath1);
            cursor.close();


        }
    }

    public void uploadImage(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();
     //   imageFilePath=new ArrayList<>();

      //  File videoFile = new File(videoFilePath);
        //File imageFile = new File(imageFilePath);
       // File audioFile = new File(audioFilePath);

//        for (int i = 0; i <imageFilePath.size() ; i++) {
//            File imageFile = new File(imageFilePath.get(i));
//           // RequestBody requestImage = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//           // builder.addFormDataPart("event_images[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
//        }


        MediaType mediaType = MediaType.parse("multipart/form-data");

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        // Log.d(TAG, "pjUploadMultiFile: " + imageFile.getName() + "  " + videoFile.getName() + " " + audioFile.getName());
        builder.addFormDataPart("token", UserToken);
        builder.addFormDataPart("message", "abc");
        // if (!imageFilePath.isEmpty()) {
        //  builder.addFormDataPart("image", imageFile.getName(), RequestBody.create(mediaType, imageFile));

        for (int i = 0; i <imagesEncodedList.size() ; i++) {
            File imageFile = new File(imagesEncodedList.get(i));
            Log.d(TAG, "pjUploadMultiFilenameeee: "+imageFile.getName());
            // RequestBody requestImage = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("image[]", imageFile.getName(), RequestBody.create(mediaType, imageFile));
            // builder.addFormDataPart("event_images[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            //  }
        }

//        if (!videoFilePath.equalsIgnoreCase("")) {
//            builder.addFormDataPart("video", videoFile.getName(), RequestBody.create(mediaType, videoFile));
//        }
//        if (!audioFilePath.equalsIgnoreCase("")) {
//            builder.addFormDataPart("audio", audioFile.getName(), RequestBody.create(mediaType, audioFile));
//        }
        MultipartBody requestBody = builder.build();
        Call<PostResponse> fileUpload = apiInterface.postData("multipart/form-data; boundary=" + requestBody.boundary(), requestBody);
        fileUpload.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                pDialog.cancel();
                // imagepreview.setImageResource(0);
                //  postAdapter.restForm();
                try {
                    PostResponse postResponse = response.body();
                    isSucessPost = postResponse.getSuccess();
                    Log.i("sucesspostupload", isSucessPost.toString());

                }
                catch (Exception e){


                }


                if (isSucessPost == true) {

                    imagepath = "";
                    //  editTextTextpost.getText().clear();
                    audiopath = "";
                    videopath = "";
                    postAdapter.restForm();
                    //    postAdapter.clear();

                    loadFirstPage();
                    //  selected_file.setVisibility(View.INVISIBLE);

                    //   postAdapter.notifyDataSetChanged();
                    //  loadFirstPage();
                    //  loadNextPage();

                    Toast.makeText(getActivity(), "Successfully Posted", Toast.LENGTH_SHORT).show();
                } else if (isSucessPost == false) {

                    Toast.makeText(getActivity(), "Please enter some  Text,video,Image or Audio", Toast.LENGTH_SHORT).show();


                }

            }


            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d(TAG, "Succcess " + t.getMessage());
            }
        });

    }



}







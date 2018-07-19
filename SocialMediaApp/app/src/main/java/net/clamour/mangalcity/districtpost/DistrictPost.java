package net.clamour.mangalcity.districtpost;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.clamour.mangalcity.Home.PaginationScrollListener;
import net.clamour.mangalcity.PostTabs.FeedBackActivity;
import net.clamour.mangalcity.PostTabs.PostAdapter;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.CityPostResponse;
import net.clamour.mangalcity.ResponseModal.CountryMainResponse;
import net.clamour.mangalcity.ResponseModal.CountryPostResponse;
import net.clamour.mangalcity.ResponseModal.DistrictMainResponse;
import net.clamour.mangalcity.ResponseModal.FeedsResponse;
import net.clamour.mangalcity.ResponseModal.PostDeleteResponse;
import net.clamour.mangalcity.countrypost.CountryPost;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DistrictPost extends AppCompatActivity {

    ProgressDialog pDialog;
    ApiInterface apiInterface;
    Boolean isSucess;
    private static final String TAG = "CountryPost";
    List<CityPostResponse>DistrictPostResponses_array;
    //  ArrayList<PostModalClass> post_array;
    PostAdapter postAdapter;
    private RecyclerView recyclerView;
    SharedPreferences LoginPrefrences;
    String UserToken;

    LinearLayoutManager linearLayoutManager;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 15;
    private int currentPage = PAGE_START;
    List<CityPostResponse> nextList;
    String post_id;
    Button btn_cancel,reportspam,delete,download;
    BottomSheetDialog dialog;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_post);

//        Toolbar toolbar1 = (Toolbar) findViewById(R.id.main_page_toolbar);
//        toolbar1.setTitleTextColor(Color.parseColor("#ffffff"));
//        toolbar1.setTitle("District Post");
//
//
//        setSupportActionBar(toolbar1);
//
//        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
//        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
//
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
//        UserToken=LoginPrefrences.getString("userToken","");
//
//
//
//
//        progressBar = (ProgressBar) findViewById(R.id.main_progress);
//        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
//
//        DistrictPostResponses_array=new ArrayList<>();
//        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        postAdapter=new PostAdapter(DistrictPost.this);
//        recyclerView.setAdapter(postAdapter);
//
//        postAdapter.setOnItemClicklistner(new PostAdapter.setOnItemClickListener() {
//            @Override
//            public void onViewMoreClickListner(int position) {
//                init_modal_bottomsheet(position);
//
//            }
//        });
//
//        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
//            @Override
//            protected void showForm() {
//                // formLayout.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            protected void hideForm() {
//
//                //formLayout.setVisibility(View.GONE);
//            }
//
//            @Override
//            protected void onScrolledToEnd() {
//                Log.e("Position", "Last item reached");
//                isLoading = true;
//                currentPage += 1;
//                loadNextPage();
//            }
//
//            @Override
//            protected void loadMoreItems() {
//                Log.d(TAG, "loadMoreItems: Loading");
//
//
//            }
//
//            @Override
//            public int getTotalPageCount() {
//                return TOTAL_PAGES;
//            }
//
//            @Override
//            public boolean isLastPage() {
//
//                return isLastPage;
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//        });
//        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//        loadFirstPage();
//
//
//        //  getCountryPost();
//
//    }
//
//    public void loadFirstPage(){
//        Log.d(TAG, "load 1 Page: " + currentPage);
//        callFeedApi().enqueue(new Callback<DistrictMainResponse>() {
//            @Override
//            public void onResponse(Call<DistrictMainResponse> call, Response<DistrictMainResponse> response) {
//
//                progressBar.setVisibility(View.GONE);
//                nextList =fetchResults(response);
//
//                // postAdapter.clear();
//                for (CityPostResponse cityPostResponse:nextList){
//
//                    post_id=cityPostResponse.getId();
//                    Log.d(TAG, "onResponseidddactivity: "+post_id+cityPostResponse.getCreated_at());
//                }
//
//                postAdapter.addAll(nextList);
//
//
//                if (currentPage <= TOTAL_PAGES) postAdapter.addLoadingFooter();
//                else isLastPage = true;
//
//
//
//
//            }
//
//            @Override
//            public void onFailure(Call<DistrictMainResponse> call, Throwable t) {
//
//            }
//        });
//
//
//
//    }
//
//    private void loadNextPage(){
//
//        Log.d(TAG, "loadNextPage: " + currentPage);
//
//
//
//        callFeedApi().enqueue(new Callback<DistrictMainResponse>() {
//            @Override
//            public void onResponse(Call<DistrictMainResponse> call, Response<DistrictMainResponse> response) {
//
//                postAdapter.removeLoadingFooter();
//                isLoading = false;
//                List<CityPostResponse> nextList =fetchResults(response);
//                for (CityPostResponse cityPostResponse:nextList){
//
//                    post_id=cityPostResponse.getId();
//                    Log.d(TAG, "onResponseidddactivity: "+post_id+cityPostResponse.getCreated_at());
//                }
//
//                postAdapter.addAll(nextList);
//
//                if (currentPage != TOTAL_PAGES) postAdapter.addLoadingFooter();
//                else isLastPage = true;
//
//
//            }
//
//            @Override
//            public void onFailure(Call<DistrictMainResponse> call, Throwable t) {
//
//            }
//        });
//
//
//    }
//
//    private List<CityPostResponse> fetchResults(Response<DistrictMainResponse> response) {
//        DistrictMainResponse body = response.body();
//        return body.getDistrict_posts().getData();
//    }
//    private Call<DistrictMainResponse> callFeedApi() {
//        return apiInterface.getDistrictFeeds(UserToken,currentPage+"");
//    }
//
//
////    public void getCountryPost(){
////
////        pDialog = new ProgressDialog(DistrictPost.this);
////        pDialog.setMessage("Please wait...");
////        pDialog.setCancelable(true);
////        pDialog.show();
////
////
////        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
////
////        Call<DistrictMainResponse> call = apiInterface.getDistrictFeeds(UserToken);
////
////        call.enqueue(new Callback<DistrictMainResponse>() {
////            @Override
////            public void onResponse(Call<DistrictMainResponse> call, Response<DistrictMainResponse> response) {
////                pDialog.cancel();
////
////                DistrictMainResponse feedsResponse = response.body();
////                isSucess = feedsResponse.getSuccess();
////                Log.d(TAG, "onResponse: " + isSucess);
////
////                countryPostResponses_array=feedsResponse.getDistrict_posts().getData();
////                Log.d(TAG, "onResponse: " + countryPostResponses_array);
////
////
//////                for ( CountryMainResponse countryPostResponse:countryPostResponses_array){
//////
//////                    String hfdh=countryPostResponse.getCreated_at();
//////                    Log.d(TAG, "onResponse: "+hfdh);
//////
//////                    String fjdsfj=countryPostResponse.user.getFirst_name();
//////                    Log.d(TAG, "onResponse: "+fjdsfj);
//////
//////                }
////                postAdapter=new PostAdapter(DistrictPost.this,countryPostResponses_array);
////
////                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(DistrictPost.this);
////                recyclerView.setLayoutManager(layoutManager);
////                recyclerView.setItemAnimator(new DefaultItemAnimator());
////                recyclerView.setAdapter(postAdapter);
////
////
////
////
////
////            }
////
////            @Override
////            public void onFailure(Call<DistrictMainResponse> call, Throwable t) {
////
////            }
////        });
////
////
////    }
////
//    @Override
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case android.R.id.home:
//                // Respond to the action bar's Up/Home button
//                // adapter.notifyDataSetChanged();
//
//                finish();
//                // NavUtils.navigateUpFromSameTask(this);
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }
//    public void init_modal_bottomsheet(final int position) {
//        View modalbottomsheet = getLayoutInflater().inflate(R.layout.modal_bottomsheet, null);
//
//
//        dialog = new BottomSheetDialog(this);
//        dialog.setContentView(modalbottomsheet);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        dialog.show();
//
//
//        btn_cancel = (Button) modalbottomsheet.findViewById(R.id.btn_cancel);
//        reportspam = (Button) modalbottomsheet.findViewById(R.id.ReportSpam);
//        delete = (Button) modalbottomsheet.findViewById(R.id.Delete);
//        download = (Button) modalbottomsheet.findViewById(R.id.DownLoad);
//
//        reportspam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: "+position+""+nextList.get(position).getId());
//                Intent intent = new Intent(DistrictPost.this, FeedBackActivity.class);
//                intent.putExtra("postid",nextList.get(position).getId());
//                startActivity(intent);
//                dialog.hide();
//
//
//            }
//        });
//
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final AlertDialog alertDialog = new AlertDialog.Builder(
//                        DistrictPost.this).create();
//
//                // Setting Dialog Title
//                alertDialog.setTitle("                 Alert!");
//
//                // Setting Dialog Message
//                alertDialog.setMessage("Are You Surely Want to Delete");
//
//                // Setting Icon to Dialog
//
//
//                // Setting OK Button
//                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, int which) {
//                        //                                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                        //                                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
//                        //                                    startActivity(intent);
//                        // Write your code here to execute after dialog closed
//                        // alertDialog.dismiss();
//                        // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_LONG).show();
//
//                        // verifyEmail();
//                        // saveData();
//                        //  notifyDataSetChanged();
//                        alertDialog.dismiss();
//
//
//                        pDialog = new ProgressDialog(DistrictPost.this);
//                        pDialog.setMessage("Please wait...");
//                        pDialog.setCancelable(true);
//                        pDialog.show();
//
//                        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//
//                        Call<PostDeleteResponse> call = apiInterface.deltePost(UserToken, nextList.get(position).getId());
//
//                        call.enqueue(new Callback<PostDeleteResponse>() {
//                            @Override
//                            public void onResponse(Call<PostDeleteResponse> call, Response<PostDeleteResponse> response) {
//                                pDialog.cancel();
//
//                                PostDeleteResponse postDeleteResponse = response.body();
//                                isSucess = postDeleteResponse.getSuccess();
//                                Log.d(TAG, "onResponse: " + isSucess);
//                                //  notifyItemRangeChanged(position, event_list.size());
//
//
//                                if (isSucess == true) {
//
//                                    nextList.remove(position);
//                                    postAdapter.notifyItemRemoved(position);
//                                    alertDialog.dismiss();
//                                    dialog.dismiss();
//
//                                    Toast.makeText(DistrictPost.this,"Deleted Successfully",Toast.LENGTH_SHORT).show();
//                                } else if (isSucess == false) {
//
//                                    Toast.makeText(DistrictPost.this,"Please Try Again",Toast.LENGTH_SHORT).show();
//                                    dialog.dismiss();
//
//                                }
//
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<PostDeleteResponse> call, Throwable t) {
//
//                            }
//                        });
//
//
//                    }
//                });
//
//                // Showing Alert Message
//                alertDialog.show();
//
//            }
//        });
//
//
//        // alertDialog.show();
//
//
//        download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.hide();
//            }
//        });
    }

}


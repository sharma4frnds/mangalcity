package net.clamour.mangalcity.PostTabs;

        import android.app.ProgressDialog;
        import android.content.SharedPreferences;
        import android.support.design.widget.BottomSheetDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ProgressBar;

        import net.clamour.mangalcity.Activity.ActivityPost;
        import net.clamour.mangalcity.Activity.ActivityResponse;
        import net.clamour.mangalcity.R;
        import net.clamour.mangalcity.ResponseModal.CityPostResponse;
        import net.clamour.mangalcity.webservice.ApiInterface;

        import java.util.List;


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
        import net.clamour.mangalcity.Home.PostActivity;
        import net.clamour.mangalcity.PostTabs.FeedBackActivity;
        import net.clamour.mangalcity.PostTabs.ItemClickListener;
        import net.clamour.mangalcity.PostTabs.PostAdapter;
        import net.clamour.mangalcity.R;
        import net.clamour.mangalcity.ResponseModal.CityPostResponse;
        import net.clamour.mangalcity.ResponseModal.CountryMainResponse;
        import net.clamour.mangalcity.ResponseModal.CountryPostResponse;
        import net.clamour.mangalcity.ResponseModal.FeedsResponse;
        import net.clamour.mangalcity.ResponseModal.PostDeleteResponse;
        import net.clamour.mangalcity.webservice.ApiClient;
        import net.clamour.mangalcity.webservice.ApiInterface;

        import java.util.ArrayList;
        import java.util.List;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;


public class ActivityLog extends AppCompatActivity {


    ProgressDialog pDialog;
    ApiInterface apiInterface;
    Boolean isSucess;
    private static final String TAG = "CountryPost";
    List<ActivityPost> actvitylog_array;

    ActivityAdapter activityAdapter;
    private RecyclerView recyclerView;
    SharedPreferences LoginPrefrences;
    String UserToken;
    ProgressBar progressBar;
    LinearLayoutManager linearLayoutManager;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 15;
    private int currentPage = PAGE_START;
    List<ActivityPost> nextList;
    String post_id;
    Button btn_cancel,reportspam,delete,download;
    BottomSheetDialog dialog;
    int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

                Toolbar toolbar1 = (Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar1.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar1.setTitle("AcctivityLog");


        setSupportActionBar(toolbar1);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        UserToken=LoginPrefrences.getString("userToken","");




        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
nextList=new ArrayList<>();
        actvitylog_array=new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        activityAdapter=new ActivityAdapter(ActivityLog.this);
        recyclerView.setAdapter(activityAdapter);



        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void showForm() {
               // formLayout.setVisibility(View.VISIBLE);
            }

            @Override
            protected void hideForm() {

                //formLayout.setVisibility(View.GONE);
            }

            @Override
            protected void onScrolledToEnd() {
                Log.e("Position", "Last item reached");
                isLoading = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            protected void loadMoreItems() {
                Log.d(TAG, "loadMoreItems: Loading");


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


        //getCountryPost();

    }

    public void loadFirstPage(){
        Log.d(TAG, "load 1 Page: " + currentPage);
        callFeedApi().enqueue(new Callback<ActivityResponse>() {
            @Override
            public void onResponse(Call<ActivityResponse> call, Response<ActivityResponse> response) {

                progressBar.setVisibility(View.GONE);
                nextList =fetchResults(response);

                // postAdapter.clear();
//                for (ActivityPost activityPost:nextList){
//
//                    post_id=activityPost.getId();
//                    Log.d(TAG, "onResponseidddactivity: "+post_id+activityPost.getUpdated_at());
//                }

                activityAdapter.addAll(nextList);


                if (currentPage <= TOTAL_PAGES) activityAdapter.addLoadingFooter();
                else isLastPage = true;




            }

            @Override
            public void onFailure(Call<ActivityResponse> call, Throwable t) {

            }
        });



    }

    private void loadNextPage(){

        Log.d(TAG, "loadNextPage: " + currentPage);



        callFeedApi().enqueue(new Callback<ActivityResponse>() {
            @Override
            public void onResponse(Call<ActivityResponse> call, Response<ActivityResponse> response) {

                activityAdapter.removeLoadingFooter();
                isLoading = false;
                nextList =fetchResults(response);
                for (ActivityPost activityPost:nextList){

                    post_id=activityPost.getId();
                   // Log.d(TAG, "onResponseidddactivity: "+post_id+cityPostResponse.getCreated_at());
                }

                activityAdapter.addAll(nextList);

                if (currentPage != TOTAL_PAGES) activityAdapter.addLoadingFooter();
                else isLastPage = true;


            }

            @Override
            public void onFailure(Call<ActivityResponse> call, Throwable t) {

            }
        });


    }

    private List<ActivityPost> fetchResults(Response<ActivityResponse> response) {
        ActivityResponse body = response.body();
        String isSucess=body.getSuccess().toString();
        Log.d(TAG, "fetchResults: "+isSucess);
        return nextList;
    }
    private Call<ActivityResponse> callFeedApi() {
        return apiInterface.activityLogs(UserToken,currentPage+"");
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

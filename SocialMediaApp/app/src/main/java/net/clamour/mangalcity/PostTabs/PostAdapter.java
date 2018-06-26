package net.clamour.mangalcity.PostTabs;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.clamour.mangalcity.Home.PostActivity;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.CountryPostResponse;
import net.clamour.mangalcity.ResponseModal.DislikeResponse;
import net.clamour.mangalcity.ResponseModal.LikeResponse;
import net.clamour.mangalcity.ResponseModal.LoginResponse;
import net.clamour.mangalcity.ResponseModal.PostDeleteResponse;
import net.clamour.mangalcity.profile.LoginActivity;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private static final String TAG = "PostAdapter";
    Context context;
    List<CountryPostResponse> event_list;
    ItemClickListener clickListener;
    ImageView postImage;
    ImageView imageLike;
    ImageView imageDislike;
    ImageView imageComment;
    ImageView imageShare;
    ImageView userImage;
    TextView userName;
    TextView postTiming;
    TextView postText;
    TextView no_of_likes;
    TextView no_of_dislikes,share_text;
    VideoView post_video;
    RelativeLayout relativeImage,relativeCard;

    ImageView dots;
    android.support.v7.app.AlertDialog alertDialog;
    Button feedback,delete;
    String post_id,userToken,user_id,mylikeId,liketype,likerespose;
    SharedPreferences LoginPrefrences;
    ApiInterface apiInterface;
    Boolean isSucess;
    ProgressDialog pDialog;







    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        public MyViewHolder(View view) {
            super(view);

         userName=(TextView)view.findViewById(R.id.user_name);
         postText=(TextView)view.findViewById(R.id.post_text);
         postTiming=(TextView)view.findViewById(R.id.post_timing);
         userImage=(ImageView)view.findViewById(R.id.user_image);
         imageShare=(ImageView)view.findViewById(R.id.image_share);
         imageComment=(ImageView)view.findViewById(R.id.image_comment);
         imageDislike=(ImageView)view.findViewById(R.id.image_dislike);
         imageLike=(ImageView)view.findViewById(R.id.image_like);
         postImage=(ImageView)view.findViewById(R.id.post_image);
         no_of_likes=(TextView) view.findViewById(R.id.no_of_likes);
         no_of_dislikes=(TextView)view.findViewById(R.id.no_of_dislikes);
         post_video=(VideoView)view.findViewById(R.id.post_video);
         dots=(ImageView)view.findViewById(R.id.dots);
         relativeImage=(RelativeLayout) view.findViewById(R.id.relativ1);
         relativeCard=(RelativeLayout)view.findViewById(R.id.relative_image) ;
         share_text=(TextView)view.findViewById(R.id.share_text);

            view.setTag(view);

            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    public PostAdapter(Context context, List<CountryPostResponse> event_list) {
        this.context = context;
        this.event_list = event_list;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        LoginPrefrences = context.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        userToken=LoginPrefrences.getString("userToken","");
        Log.i("UserTokenAdapter",userToken);

        user_id=LoginPrefrences.getString("user_id","");
        Log.i("userid",user_id);


        final CountryPostResponse countryPostResponse = event_list.get(position);

//        if(event_list.get(position).like.toString()==null){
//
//            imageLike.setImageResource(R.drawable.like_gray);
//            imageDislike.setImageResource(R.drawable.dislike_grey);
//        }
//        else if(event_list.get(position).like.toString()!=null){
//
//            mylikeId=event_list.get(position).like.getUser_id();
//            Log.d(TAG, "onBindViewHolder: "+mylikeId);
//
//            imageLike.setImageResource(R.drawable.like_gray);
//            imageDislike.setImageResource(R.drawable.dislike_grey);
//        }

//        mylikeId=countryPostResponse.getLike().getUser_id();
//        Log.d(TAG, "onBindViewHolder: "+mylikeId);
//
//        liketype=countryPostResponse.getLike().getType();
//        Log.i("liketype",liketype);
//
//        if(user_id.equals(mylikeId)&&liketype.equals("1")){
//
//            imageLike.setImageResource(R.drawable.like);
//            imageDislike.setImageResource(R.drawable.dislike_grey);
//        }
//        else if(user_id.equals(mylikeId)&&liketype.equals("0")) {
//
//            imageLike.setImageResource(R.drawable.like_gray);
//            imageDislike.setImageResource(R.drawable.dislike);
//
//
//        }



        imageLike.setTag(position);

        imageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(true);
                pDialog.show();

                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                Call<LikeResponse> call = apiInterface.like_post(userToken, event_list.get(position).getId());

                call.enqueue(new Callback<LikeResponse>() {
                    @Override
                    public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                        pDialog.cancel();

                        LikeResponse likeResponse = response.body();
                        isSucess = likeResponse.getSuccess();
                        Log.d(TAG, "onResponse: " + isSucess);
                        String lcount=likeResponse.getLcount();
                        Log.i("lcount",lcount);
                      String dcount=likeResponse.getDcount();
                      Log.i("dcount",dcount);

                      no_of_likes.setText(lcount);
                        no_of_likes.setText(dcount);


                        if (isSucess == true) {
                            no_of_likes.setText(lcount);
                            no_of_likes.setText(dcount);

//                            final AlertDialog alertDialog = new AlertDialog.Builder(
//                                    context).create();
//
//                            // Setting Dialog Title
//                            alertDialog.setTitle("                 Alert!");
//
//                            // Setting Dialog Message
//                            alertDialog.setMessage("            Post Deleted Sucessfully");
//
//                            // Setting Icon to Dialog
//
//
//                            // Setting OK Button
//                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //                                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                                    //                                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
//                                    //                                    startActivity(intent);
//                                    // Write your code here to execute after dialog closed
//                                    // alertDialog.dismiss();
//                                    // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_LONG).show();
//
//                                    // verifyEmail();
//                                    // saveData();
//                                //    notifyDataSetChanged();
//                                    alertDialog.dismiss();
//
//                                }
//                            });
//
//                            // Showing Alert Message
//                            alertDialog.show();

                        } else if (isSucess == false) {

                            final AlertDialog alertDialog = new AlertDialog.Builder(
                                    context).create();
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
                    public void onFailure(Call<LikeResponse> call, Throwable t) {

                    }
                });
imageDislike.setTag(position);
imageDislike.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<DislikeResponse> call = apiInterface.dislike_post(userToken, event_list.get(position).getId());

        call.enqueue(new Callback<DislikeResponse>() {
            @Override
            public void onResponse(Call<DislikeResponse> call, Response<DislikeResponse> response) {
                pDialog.cancel();

                DislikeResponse likeResponse = response.body();
                isSucess = likeResponse.getSuccess();
                Log.d(TAG, "onResponse: " + isSucess);
                String lcount=likeResponse.getLcount();
                Log.i("lcount",lcount);
                String dcount=likeResponse.getDcount();
                Log.i("dcount",dcount);

                no_of_likes.setText(lcount);
                no_of_likes.setText(dcount);


                if (isSucess == true) {
                    no_of_likes.setText(lcount);
                    no_of_likes.setText(dcount);

//                            final AlertDialog alertDialog = new AlertDialog.Builder(
//                                    context).create();
//
//                            // Setting Dialog Title
//                            alertDialog.setTitle("                 Alert!");
//
//                            // Setting Dialog Message
//                            alertDialog.setMessage("            Post Deleted Sucessfully");
//
//                            // Setting Icon to Dialog
//
//
//                            // Setting OK Button
//                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //                                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                                    //                                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
//                                    //                                    startActivity(intent);
//                                    // Write your code here to execute after dialog closed
//                                    // alertDialog.dismiss();
//                                    // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_LONG).show();
//
//                                    // verifyEmail();
//                                    // saveData();
//                                //    notifyDataSetChanged();
//                                    alertDialog.dismiss();
//
//                                }
//                            });
//
//                            // Showing Alert Message
//                            alertDialog.show();

                } else if (isSucess == false) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(
                            context).create();
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
            public void onFailure(Call<DislikeResponse> call, Throwable t) {

            }
        });


    }
});

            }
        });


        imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,SharePostActivity.class);

                if(countryPostResponse.getType().contains("image")){

                    intent.putExtra("image",event_list.get(position).getValue());
                    intent.putExtra("token",userToken);
                    intent.putExtra("post_id",event_list.get(position).getId());
                    intent.putExtra("profileimage",event_list.get(position).user.getImage());

                }
                else if(countryPostResponse.getType().contains("video")){
                    intent.putExtra("video",event_list.get(position).getValue());
                    intent.putExtra("token",userToken);
                    intent.putExtra("post_id",event_list.get(position).getId());
                    intent.putExtra("profileimage",event_list.get(position).user.getImage());


                }
                else if(countryPostResponse.getType().contains("")){

                    intent.putExtra("text",event_list.get(position).getMessage());
                    intent.putExtra("token",userToken);
                    intent.putExtra("post_id",event_list.get(position).getId());
                    intent.putExtra("profileimage",event_list.get(position).user.getImage());

                }
                context.startActivity(intent);



            }
        });

        share_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,SharePostActivity.class);

                if(countryPostResponse.getType().contains("image")){

                    intent.putExtra("image",event_list.get(position).getValue());
                    intent.putExtra("token",userToken);
                    intent.putExtra("post_id",event_list.get(position).getId());
                    intent.putExtra("profileimage",event_list.get(position).user.getImage());

                }
                else if(countryPostResponse.getType().contains("video")){
                    intent.putExtra("video",event_list.get(position).getValue());
                    intent.putExtra("token",userToken);
                    intent.putExtra("post_id",event_list.get(position).getId());
                    intent.putExtra("profileimage",event_list.get(position).user.getImage());


                }
                else if(countryPostResponse.getType().contains("")){

                    intent.putExtra("text",event_list.get(position).getMessage());
                    intent.putExtra("token",userToken);
                    intent.putExtra("post_id",event_list.get(position).getId());
                    intent.putExtra("profileimage",event_list.get(position).user.getImage());

                }
                context.startActivity(intent);

            }
        });


        userName.setText(countryPostResponse.user.getFirst_name()+" "+countryPostResponse.user.getLast_name());

       // post_image_string=countryPostResponse.getValue();
        if(countryPostResponse.getType().isEmpty()){

            postImage.setVisibility(View.INVISIBLE);
            post_video.setVisibility(View.INVISIBLE);

            ViewGroup.LayoutParams params = relativeImage.getLayoutParams();
            params.height = 0;
            params.width = 0;

            ViewGroup.LayoutParams params1 = relativeCard.getLayoutParams();

            params1.height = 380;
            params1.width = 700;


            // MediaController mediaController = new MediaController(context);
         //   post_video.setMediaController(mediaController);

//            Uri uri=Uri.parse("http://emergingncr.com/mangalcity/public/images/post/post_video/1528895061.mp4");
//            post_video.setVideoURI(uri);
//            //post_video.start();
//            post_video.pause();

        }
        else if(countryPostResponse.getType().contains("video")) {

            Log.i("video","video");

            postImage.setVisibility(View.INVISIBLE);
            post_video.setVisibility(View.VISIBLE);

            Uri uri = Uri.parse("http://emergingncr.com/mangalcity/public/images/post/post_video/" + countryPostResponse.getValue());
            post_video.setVideoURI(uri);

            MediaController mediaController = new
                    MediaController(context);
           // mediaController.setAnchorView(post_video);
            post_video.setMediaController(mediaController);
            post_video.pause();
            post_video.seekTo(100);

        }

        else if(countryPostResponse.getType().contains("image")){

            Log.i("image","image");

            postImage.setVisibility(View.VISIBLE);
            post_video.setVisibility(View.INVISIBLE);



            Glide.with(context).load("http://emergingncr.com/mangalcity/public/images/post/post_image/"+countryPostResponse.getValue())
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(0)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(postImage);


        }




        Glide.with(context).load("http://emergingncr.com/mangalcity/public/images/user/"+countryPostResponse.user.getImage())
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userImage);

              no_of_likes.setText(countryPostResponse.getLikes());
        no_of_dislikes.setText(countryPostResponse.getDislikes());

        post_id=countryPostResponse.getId();
        Log.i("post_id",post_id);


        // userImage.setImageResource(countryPostResponse.user.getI);
      //  postImage.setImageResource(countryPostResponse.getPost_image());
      //  imageLike.setImageResource(countryPostResponse.getLike_image());
      //  imageDislike.setImageResource(countryPostResponse.getDislike_image());
      //  imageShare.setImageResource(countryPostResponse.getShare_image());
      //  imageComment.setImageResource(countryPostResponse.getComment_image());
        postText.setText(countryPostResponse.getMessage());
        postTiming.setText(countryPostResponse.getCreated_at());

        dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDialog();


            }
        });




    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.postlistitems, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public int getItemCount() {
        Log.i("sizeeeee", event_list.size() + "");
        return event_list.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;


    }

    public void openDialog(){

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);



        LayoutInflater inflater = LayoutInflater.from(context);


        View subView = inflater.inflate(R.layout.feedbackdialog, null);
        builder.setView(subView);
        alertDialog = builder.create();

        feedback=(Button)subView.findViewById(R.id.feedback);
        delete=(Button)subView.findViewById(R.id.delete);


        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






            }
        });
        final int position=0;
        delete.setTag(position);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(true);
                pDialog.show();

                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                Call<PostDeleteResponse> call = apiInterface.deltePost(userToken, event_list.get(position).getId());

                call.enqueue(new Callback<PostDeleteResponse>() {
                    @Override
                    public void onResponse(Call<PostDeleteResponse> call, Response<PostDeleteResponse> response) {
                        pDialog.cancel();

                        PostDeleteResponse postDeleteResponse = response.body();
                        isSucess = postDeleteResponse.getSuccess();
                        Log.d(TAG, "onResponse: " + isSucess);
                        event_list.remove(event_list.get(position).getId());
                        notifyDataSetChanged();


                        if (isSucess == true) {


                            final AlertDialog alertDialog = new AlertDialog.Builder(
                                    context).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("                 Alert!");

                            // Setting Dialog Message
                            alertDialog.setMessage("            Post Deleted Successfully");

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
                                   // notifyDataSetChanged();
                                    alertDialog.dismiss();

                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();

                        } else if (isSucess == false) {

                            final AlertDialog alertDialog = new AlertDialog.Builder(
                                    context).create();
                            // saveData();
                            // Setting Dialog Title
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



        // if button is clicked, close the custom dialog
//        cross_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });

        alertDialog.show();}

}

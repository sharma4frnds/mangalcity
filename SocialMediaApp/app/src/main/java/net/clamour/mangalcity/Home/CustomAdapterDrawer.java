package net.clamour.mangalcity.Home;

/**
 * Created by clamour_5 on 5/14/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.theartofdev.edmodo.cropper.CropImage;

import net.clamour.mangalcity.R;
import net.clamour.mangalcity.profile.UserProfile;
import net.clamour.mangalcity.profile.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;


public class CustomAdapterDrawer extends ArrayAdapter<String> {

    String web[];
    Integer img[];
    Activity context;
    ProgressDialog pDialog;


    SharedPreferences LoginPrefrences;
    SharedPreferences Registration_preferences;
    String UserToken,profile_image,first_nameuser,last_nameuser;
    Boolean isSucessprofile;
    private static final int SELECT_PICTURE_PROFILE = 100;
    private static final int SELECT_PICTURE_COVER=101;
    String profilepath;
    Bitmap bitmap;
    Uri selectedImageUri;
    ImageView profile;
    TextView name;
    String firstname_get,lastname_get,profileimage_get;

    String auth_id,first_name,last_name,complete_name,email;
    public CustomAdapterDrawer(Activity context, String[] web,
                               Integer[] img) {
        super(context, android.R.layout.simple_list_item_1, web);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.img=img;
        this.web=web;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.draweritems,null,true);
//        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, 150);
//        rowView.setLayoutParams(params);
        LoginPrefrences = context.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        UserToken=LoginPrefrences.getString("userToken","");
        Log.i("UserToken",UserToken);
        profile_image=LoginPrefrences.getString("profileImage","");
        first_nameuser=LoginPrefrences.getString("userFirstName","");
        last_nameuser=LoginPrefrences.getString("userLastName","");



        TextView txtTitle = (TextView) rowView.findViewById(R.id.compalint_row_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.complaint_row_img);



        txtTitle.setText(web[position]);
        imageView.setImageResource(img[position]);

       // Registration_preferences = context.getSharedPreferences("net.clamour.detlef.Profile.RegistrationScreen", MODE_PRIVATE);
        //UserName = Registration_preferences.getString("UserName", "");


        if(position==0){
            rowView=inflater.inflate(R.layout.profile_row,null,true);

             name=(TextView)rowView.findViewById(R.id.name);
            profile=(ImageView)rowView.findViewById(R.id.drawer_profile_image);

            getProfileData();

            //name.setText(first_nameuser+" "+last_nameuser);
//            Glide.with(context).load("http://emergingncr.com/mangalcity/public/images/user/"+profile_image)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .placeholder(0)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(profile);

//            profile.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    captureImage();
//                }
//            });







        }
        if(position==9){
            rowView=inflater.inflate(R.layout.switchlocation,null,true);



        }











        return rowView;
    }

//    public void changeProfile(){
//
//        pDialog = new ProgressDialog(context);
//        pDialog.setMessage("Please wait...");
//        pDialog.setCancelable(true);
//
//        pDialog.show();
//
//        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/change_profile_image", new Response.Listener<NetworkResponse>() {
//            @Override
//            public void onResponse(NetworkResponse response) {
//                String resultResponse = new String(response.data);
//                Log.i("responsereprofileimgeeeeeee", resultResponse);
//                pDialog.cancel();
//                try {
//
//                    JSONObject jsonObject=new JSONObject(resultResponse);
//                    isSucessprofile=jsonObject.getBoolean("success");
//                    Log.i("imaggeee", isSucessprofile.toString());
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if ((isSucessprofile == true)) {
//
//                    Toast.makeText(context,"Successfully Updated",Toast.LENGTH_SHORT).show();
//
//
//
//                }
//
//                else if(isSucessprofile == false){
//
//                    Toast.makeText(context,"please Try Again",Toast.LENGTH_SHORT).show();
//                }
//            }
//
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                NetworkResponse networkResponse = error.networkResponse;
//                String errorMessage = "Unknown error";
//                if (networkResponse == null) {
//                    if (error.getClass().equals(TimeoutError.class)) {
//                        errorMessage = "Request timeout";
//                    } else if (error.getClass().equals(NoConnectionError.class)) {
//                        errorMessage = "Failed to connect server";
//                    }
//                } else {
//                    String result = new String(networkResponse.data);
//                    try {
//                        JSONObject response = new JSONObject(result);
//                        String status = response.getString("status");
//                        String message = response.getString("message");
//
//                        Log.e("Error Status", status);
//                        Log.e("Error Message", message);
//
//                        if (networkResponse.statusCode == 404) {
//                            errorMessage = "Resource not found";
//                        } else if (networkResponse.statusCode == 401) {
//                            errorMessage = message + " Please login again";
//                        } else if (networkResponse.statusCode == 400) {
//                            errorMessage = message + " Check your inputs";
//                        } else if (networkResponse.statusCode == 500) {
//                            errorMessage = message + " Something is getting wrong";
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Log.i("Error", errorMessage);
//                error.printStackTrace();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("token",UserToken);
//
//                return params;
//            }
//
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                long imagename = System.currentTimeMillis();
//                //params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
//                if (bitmap != null) {
//                    params.put("image", new DataPart("profileImage.png", getFileDataFromDrawable(bitmap)));
//                    Log.i("have", "have");
//                } else if (bitmap == null) {
//                    //  params.put("picture",new DataPart("profileImage.png",getFileDataFromDrawable(bitmap)));
//                    Log.i("Don'thave", "Don'thave");
//                }
//
//
//                // params.put("old_picture",new DataPart("profileImage.png",getFileDataFromDrawable(bitmap)));
//                return params;
//            }
//        };
//
//        Volley.newRequestQueue(context).add(multipartRequest);
//
//    }
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode){
//            case SELECT_PICTURE_PROFILE:
//                if(resultCode== Activity.RESULT_OK){
//                    // Get the url from data
//                    selectedImageUri = data.getData();
//
//
//
//                    if (null != selectedImageUri) {
//                        // Get the path from the Uri
//                        profilepath = getPathFromURI(selectedImageUri);
//                        Log.i(TAG, "Image Path : " + profilepath);
//                        // Set the image in ImageView
//                        // profile_imagel.setImageURI(selectedImageUri);
//                        CropImage.activity(selectedImageUri)
//                                .setAspectRatio(1, 1)
//                                .setMinCropWindowSize(500, 500)
//                                .start(context);
//
//                        try {
//                            //getting bitmap object from uri
//                            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImageUri);
//                            changeProfile();
//
//
//                            // profile_prefrence.edit().remove("guset_profileimage").apply();
//
//                            //displaying selected image to imageview
//                            //  personProfileImage.setImageBitmap(bitmap);
//
//                            //calling the method uploadBitmap to upload image
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//                break;
//
//            case CROP_IMAGE_ACTIVITY_REQUEST_CODE:
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                if(resultCode==Activity.RESULT_OK){
//
//
//
//
//                        Uri mImageUri = result.getUri();
//
//                        profile.setImageURI(mImageUri);
//
//                    }
//
//
//
//
//
//                }
//        }


//    public void captureImage() {
//
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        context.startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_PROFILE);
//    }
//    public String getPathFromURI(Uri contentUri) {
//        String res = null;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//        }
//        cursor.close();
//        return res;
//    }
//
//
//    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }
public void getProfileData() {

    pDialog = new ProgressDialog(context);
    pDialog.setMessage("Please wait...");
    pDialog.setCancelable(true);


    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/getprofile",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG).show();

                    Log.i("responsegetProfile", response);


                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    Log.e("response=", response);


                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        Boolean isSucessget = jsonObject.getBoolean("success");

                        getProfileData();




                        JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                       firstname_get = jsonObject1.getString("first_name");
                        Log.d(TAG, "onResponsefirsttt: " + firstname_get);
                      lastname_get = jsonObject1.getString("last_name");

                        profileimage_get = jsonObject1.getString("image");


                    } catch (Exception e) {
                    }
                    setData();
                    // saveUpdatedData();


                }
            },
            new Response.ErrorListener() {
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


            return params;
        }

    };

    RequestQueue requestQueue1 = Volley.newRequestQueue(context);
    requestQueue1.add(stringRequest1);


}
public void setData(){
    Glide.with(context).load("http://emergingncr.com/mangalcity/public/images/user/" + profileimage_get)
            .thumbnail(0.5f)
            .crossFade()
            .placeholder(0)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(profile);

    name.setText(firstname_get+" "+lastname_get);

}
}

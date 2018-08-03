package net.clamour.mangalcity.Home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.clamour.mangalcity.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {


    @BindView(R.id.editText_serach)
    EditText editTextSerach;
    ListView searchList;
    String character, character_dialog;
    JSONArray jsonArray, filtered_array;
    ProgressDialog pDialog;
    Boolean isEXist=false;
    SearchAdapter searchAdapter;
    String name,city,image;
    SharedPreferences LoginPrefrences;
    String UserToken;
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        android.support.v7.widget.Toolbar toolbar1 = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);



        setSupportActionBar(toolbar1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        UserToken = LoginPrefrences.getString("userToken", "");
        Log.i("token", UserToken);




        searchList = (ListView) findViewById(R.id.listview);


        editTextSerach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


                character_dialog = editTextSerach.getText().toString();
                if (start == 0 && count == 1) {
                    changeText();
                    editTextSerach.setEnabled(false);

                } else if (charSequence.toString().length() > 0) {
                    character_dialog = charSequence.toString();
                    Log.d(TAG, "onTextChanged: "+character_dialog);

                    //filtered_array=new JSONArray();
                    int count1 = filtered_array.length();
                    Log.d(TAG, "onTextChanged: "+count1);
                    for (int i = 0; i < count1; i++) {

                        Log.i("beforefiltered", filtered_array.length() + "");
                        filtered_array.remove(0);

                    }
                    Log.i("afteredfiltered", filtered_array.length() + "");
                    Log.i("", "filtered_array ");
                    for(int i=0;i<jsonArray.length();i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            name = jsonObject.getString("name");
                            Log.i("namesearchhh",name);
                            city=jsonObject.getString("city");
                            image=jsonObject.getString("image");
                            Log.d(TAG, "getView: "+image);


                            if(name.toLowerCase().contains(character_dialog.toLowerCase())){

                                isEXist=true;






                            }




                            if(isEXist==true)
                            {
                                filtered_array.put(jsonObject);
                                isEXist =false;
                            }

                        }
                        catch (Exception e){

                        }

                    }
                    Log.i("responsearraynotify",jsonArray.length()+"");
                    Log.i("fiterednotify",filtered_array.length()+"");

                    searchAdapter.notifyDataSetChanged();
                    Log.i("responsearraynotify1",jsonArray.length()+"");
                    Log.i("fiterednotify1",filtered_array.length()+"");


                    // changeText();
                }
                else
                {
                    //When all char deleted
//                    search_details.clear();
//                    filtered.clear();
//                    looking.getText().clear();
                    filtered_array.remove(start);

                }


            }



            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




      

//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//               // textInfo.setText(subEditText.getText().toString());
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();
//            }
//        });

     
    }


    public void changeText() {

        pDialog = new ProgressDialog(SearchActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/search",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG).show();

                        Log.i("responsegetProfile", response);

                        editTextSerach.setEnabled(true);
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

                            Toast.makeText(getApplicationContext(),"No Result Found",Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);

    }

    public void setList(final JSONArray arr) {


        // Log.i("list search count",search_details.size()+"");


//    if (adapter == null)
//    {
        searchAdapter = new SearchAdapter(SearchActivity.this, R.layout.list_search, filtered_array);
        searchList.setAdapter(searchAdapter);


        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    JSONObject object = arr.getJSONObject(position);
                    Intent intent=new Intent(SearchActivity.this,OtherUserProfile.class);
                    intent.putExtra("user_url",object.getString("url"));
                    intent.putExtra("bypost","searchsend");
                    startActivity(intent);
                   
                    //alertDialog_search.cancel();
                    // alertDialog.dismiss();
                }
                catch (Exception e){


                }
              
                //  alertDialog_search.cancel();
                // alertDialog.dismiss();








            }
        });


    }

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


    }}


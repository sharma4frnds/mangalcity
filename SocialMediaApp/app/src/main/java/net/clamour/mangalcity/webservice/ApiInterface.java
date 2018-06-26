package net.clamour.mangalcity.webservice;


import net.clamour.mangalcity.ResponseModal.DislikeResponse;
import net.clamour.mangalcity.ResponseModal.FeedsResponse;
import net.clamour.mangalcity.ResponseModal.LikeResponse;
import net.clamour.mangalcity.ResponseModal.LoginResponse;
import net.clamour.mangalcity.ResponseModal.LogoutResponse;
import net.clamour.mangalcity.ResponseModal.OtpVerificationResponse;
import net.clamour.mangalcity.ResponseModal.PostDeleteResponse;
import net.clamour.mangalcity.ResponseModal.PostResponse;
import net.clamour.mangalcity.ResponseModal.PostShareresponse;
import net.clamour.mangalcity.ResponseModal.RegisterResponse;
import net.clamour.mangalcity.ResponseModal.changePasswordResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Clamour on 11/29/2017.
 */

public interface ApiInterface {

    @POST("auth/register")
    @FormUrlEncoded
    Call<RegisterResponse>User_Registration(@Field("first_name") String first_name,@Field("last_name") String last_name,@Field("mobile") String mobile,@Field("email") String email,@Field("password") String password,@Field("password_confirmation") String password_confirmation);

    @POST("http://emergingncr.com/mangalcity/api/auth/verifyUser")
    @FormUrlEncoded
    Call<OtpVerificationResponse>User_Verification(@Field("otp") String otp, @Field("mobile") String mobile);

    @POST("http://emergingncr.com/mangalcity/api/auth/login")
    @FormUrlEncoded
    Call<LoginResponse>userLogin(@Field("mobile") String mobile, @Field("password") String password);

    @POST("auth/logout")
    @FormUrlEncoded
    Call<LogoutResponse>userLogout(@Field("token") String token);

    @POST("change_password")
    @FormUrlEncoded
    Call<changePasswordResponse>changePassword(@Field("token") String token, @Field("old_password") String old_password, @Field("password") String password, @Field("password_confirmation") String password_confirmation);

    @POST("post")
    Call<PostResponse> postData (@Header("Content-Type") String contentType, @Body MultipartBody body);

    @POST("feeds")
    @FormUrlEncoded
    Call<FeedsResponse>getFeeds(@Field("token") String token);

    @POST("delete_post")
    @FormUrlEncoded
    Call<PostDeleteResponse>deltePost(@Field("token") String token, @Field("post_id") String post_id);

    @POST("share_post")
    @FormUrlEncoded
    Call<PostShareresponse>share_post(@Field("token") String token, @Field("post_id") String post_id);

    @POST("dolike")
    @FormUrlEncoded
    Call<LikeResponse>like_post(@Field("token") String token, @Field("post_id") String post_id);

    @POST("dodislikes")
    @FormUrlEncoded
    Call<DislikeResponse>dislike_post(@Field("token") String token, @Field("post_id") String post_id);





}
package net.clamour.mangalcity.webservice;


import net.clamour.mangalcity.Activity.ActivityResponse;
import net.clamour.mangalcity.ResponseModal.CountryMainResponse;
import net.clamour.mangalcity.ResponseModal.DislikeResponse;
import net.clamour.mangalcity.ResponseModal.DistrictMainResponse;
import net.clamour.mangalcity.ResponseModal.FeedsResponse;
import net.clamour.mangalcity.ResponseModal.GetProfileResponse;
import net.clamour.mangalcity.ResponseModal.LikeResponse;
import net.clamour.mangalcity.ResponseModal.LoginResponse;
import net.clamour.mangalcity.ResponseModal.LogoutResponse;
import net.clamour.mangalcity.ResponseModal.OtpVerificationResponse;
import net.clamour.mangalcity.ResponseModal.PostDeleteResponse;
import net.clamour.mangalcity.ResponseModal.PostResponse;
import net.clamour.mangalcity.ResponseModal.PostShareresponse;
import net.clamour.mangalcity.ResponseModal.RegisterResponse;
import net.clamour.mangalcity.ResponseModal.ResendOtpResponse;
import net.clamour.mangalcity.ResponseModal.SpamTagsResponse;
import net.clamour.mangalcity.ResponseModal.StateMainResponse;
import net.clamour.mangalcity.ResponseModal.UpdateProfileResponse;
import net.clamour.mangalcity.ResponseModal.changePasswordResponse;
import net.clamour.mangalcity.feed.PostFeedResponse;

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
    Call<PostFeedResponse>getFeeds(@Field("token") String token, @Field("page") String page);

    @POST("delete_post")
    @FormUrlEncoded
    Call<PostDeleteResponse>deltePost(@Field("token") String token, @Field("post_id") String post_id);

    @POST("share_post")
    @FormUrlEncoded
    Call<PostShareresponse>share_post(@Field("token") String token, @Field("post_id") String post_id,@Field("share_message") String share_message);

    @POST("dolike")
    @FormUrlEncoded
    Call<LikeResponse>like_post(@Field("token") String token, @Field("post_id") String post_id);

    @POST("dodislikes")
    @FormUrlEncoded
    Call<LikeResponse>dislike_post(@Field("token") String token, @Field("post_id") String post_id);

    @POST("auth/resend_otp")
    @FormUrlEncoded
    Call<ResendOtpResponse>resendOtp(@Field("mobile") String mobile);

    @POST("spam_tags")
    @FormUrlEncoded
    Call<SpamTagsResponse>getSpamTags(@Field("token") String token);

    @POST("report_feedback")
    @FormUrlEncoded
    Call<LogoutResponse>getReportFeedback(@Field("token") String token,@Field("post_id") String post_id,@Field("spam_tags") String spam_tags);



    @POST("userprofile")
    @FormUrlEncoded
    Call<UpdateProfileResponse>updateProfile(@Field("token") String token,@Field("first_name") String first_name,@Field("last_name") String last_name,@Field("email") String email,@Field("country") String country,@Field("state") String state,@Field("district") String district,@Field("city") String city,@Field("gender") String gender,@Field("marital_status") String marital_status,@Field("current_location") String current_location,@Field("home_city") String home_city,@Field("home_district") String home_district,@Field("home_state") String home_state);

    @POST("getprofile")
    @FormUrlEncoded
    Call<GetProfileResponse>getProfile(@Field("token") String token);

    @POST("forgot_password_otp")
    @FormUrlEncoded
    Call<LogoutResponse>SendOtp(@Field("mobile") String token);

    @POST("forgot_change_password")
    @FormUrlEncoded
    Call<LogoutResponse>forgetPassword(@Field("otp") String otp ,@Field("mobile")String mobile,@Field("password")String password,@Field("password_confirmation")String password_confirmation);

    @POST("district_feeds")
    @FormUrlEncoded
    Call<DistrictMainResponse>getDistrictFeeds(@Field("token") String token ,@Field("page") String page);
    @POST("country_feeds")
    @FormUrlEncoded
    Call<CountryMainResponse>getCountryFeeds(@Field("token") String token,@Field("page") String page);

    @POST("state_feeds")
    @FormUrlEncoded
    Call<StateMainResponse>getStateFeeds(@Field("token") String token,@Field("page") String page);

    @POST("profile")
    @FormUrlEncoded
    Call<PostFeedResponse>getOtherProfile(@Field("token") String token ,@Field("url")String url);

    @POST("activity")
    @FormUrlEncoded
    Call<ActivityResponse>activityLogs(@Field("token") String token ,@Field("page") String page);


}
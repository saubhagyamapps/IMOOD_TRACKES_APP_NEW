package app.food.patient_app.retrofit;

import app.food.patient_app.model.AddressTimeModel;
import app.food.patient_app.model.AllCallsDataModel;
import app.food.patient_app.model.CallInsertModel;
import app.food.patient_app.model.CaloriesDataModel;
import app.food.patient_app.model.ChangeWorkLocationModel;
import app.food.patient_app.model.DashBoardAverageModel;
import app.food.patient_app.model.EditProfileModel;
import app.food.patient_app.model.ForgotPasswordModel;
import app.food.patient_app.model.GetCaloriesModel;
import app.food.patient_app.model.GetGooGleFitActivityModel;
import app.food.patient_app.model.GetHomeLocationModel;
import app.food.patient_app.model.GetMoodNotesModel;
import app.food.patient_app.model.GetSMSCountModel;
import app.food.patient_app.model.GetSocialUsageListModel;
import app.food.patient_app.model.GetWorkAddressModel;
import app.food.patient_app.model.HomeLocationStoreModel;
import app.food.patient_app.model.ImageCountModel;
import app.food.patient_app.model.InsertSocialTimeModel;
import app.food.patient_app.model.InsertStepCountModel;
import app.food.patient_app.model.InsertWorkLocationModel;
import app.food.patient_app.model.InsetCaloriesDataModel;
import app.food.patient_app.model.LocationChgangeModel;
import app.food.patient_app.model.LockCountModel;
import app.food.patient_app.model.LockCountShowModel;
import app.food.patient_app.model.LoginModel;
import app.food.patient_app.model.LoginWithFbModel;
import app.food.patient_app.model.LoginWithGmailModel;
import app.food.patient_app.model.MoodInsertModel;
import app.food.patient_app.model.NewAddedCountactCountModel;
import app.food.patient_app.model.NewContackGetModel;
import app.food.patient_app.model.PercentageModel;
import app.food.patient_app.model.RegisterModel;
import app.food.patient_app.model.RemainingCallModel;
import app.food.patient_app.model.ResetPasswordModel;
import app.food.patient_app.model.SendSMSCountModel;
import app.food.patient_app.model.SocialItegrationModel;
import app.food.patient_app.model.StepCountPerModel;
import app.food.patient_app.model.StepDisplayModel;
import app.food.patient_app.model.StoreCurrentHomeAddressModel;
import app.food.patient_app.model.WorkTimeDiffrentModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("signup")
    Call<RegisterModel> getRegisterUser(@Field("username") String username,
                                        @Field("email") String email,
                                        @Field("mobile") String mobile,
                                        @Field("password") String password,
                                        @Field("firebase_id") String firebase_id,
                                        @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("signin")
    Call<LoginModel> getUserLogin(@Field("email") String email,
                                  @Field("password") String password,
                                  @Field("firebase_id") String firebase_id,
                                  @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<ForgotPasswordModel> getResetLink(@Field("email") String email);

    @FormUrlEncoded
    @POST("reset_password")
    Call<ResetPasswordModel> getResetPasswrod(@Field("id") String id,
                                              @Field("oldpass") String oldpass,
                                              @Field("newpass") String newpass);

    @FormUrlEncoded
    @POST("InsertNotes")
    Call<MoodInsertModel> SetMoodDetails(@Field("user_id") String user_id,
                                         @Field("date") String date,
                                         @Field("time") String time,
                                         @Field("mode") String mode,
                                         @Field("activities") String activities,
                                         @Field("notes") String notes);

    @FormUrlEncoded
    @POST("GetNotes")
    Call<GetMoodNotesModel> getMoodDetails(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getcall_duration")
    Call<AllCallsDataModel> getAllCallData(@Field("user_id") String user_id,
                                           @Field("date") String date);

    @FormUrlEncoded
    @POST("social_timespent_test")
    Call<InsertSocialTimeModel> InsertSocialData(@Field("user_id") String user_id,
                                                 @Field("date") String date,
                                                 @Field("gsonObject") String gsonObject);

    @FormUrlEncoded
    @POST("insertcall_duration")
    Call<CallInsertModel> insertCallData(@Field("gsonObject") String gsonObject,
                                         @Field("user_id") String user_id,
                                         @Field("date") String date);

    @FormUrlEncoded
    @POST("remaining_call")
    Call<RemainingCallModel> remainingCall(@Field("user_id") String user_id,
                                           @Field("date") String date);

    @FormUrlEncoded
    @POST("CountingAppTime")
    Call<GetSocialUsageListModel> CallSocialList(@Field("user_id") String user_id,
                                                 @Field("date") String date,
                                                 @Field("day") String day);

    @FormUrlEncoded
    @POST("InsertContact")
    Call<NewContackGetModel> getNewContact(@Field("user_id") String user_id,
                                           @Field("date") String date,
                                           @Field("contact_name") String contact_name,
                                           @Field("contact_number") String contact_number);

    @FormUrlEncoded
    @POST("LoginWithGmail")
    Call<LoginWithGmailModel> loginWithGmail(@Field("email") String email,
                                             @Field("username") String username,
                                             @Field("firebase_id") String firebase_id,
                                             @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("ImageCountData")
    Call<ImageCountModel> getImageCount(@Field("user_id") String user_id,
                                        @Field("date") String date,
                                        @Field("total") String total);

    @FormUrlEncoded
    @POST("CountUserdata")
    Call<NewAddedCountactCountModel> getNewAddedCountactCount(@Field("user_id") String user_id,
                                                              @Field("date") String date);

    @FormUrlEncoded
    @POST("getsms_count")
    Call<GetSMSCountModel> getSMSCount(@Field("user_id") String user_id,
                                       @Field("date") String date);

    @FormUrlEncoded
    @POST("insert_sms")
    Call<SendSMSCountModel> InsertSMS(@Field("user_id") String user_id,
                                      @Field("date") String date,
                                      @Field("sendor_number") String sendor_number);

    @FormUrlEncoded
    @POST("getcalories_data")
    Call<GetCaloriesModel> getCaloriesData(@Field("user_id") String user_id,
                                           @Field("date") String date);

    @FormUrlEncoded
    @POST("insert_activity")
    Call<InsetCaloriesDataModel> InsertGoogleFitActivity(@Field("user_id") String user_id,
                                                         @Field("date") String date,
                                                         @Field("gsonObject") String gsonObject);

    @FormUrlEncoded
    @POST("fetch_activity")
    Call<GetGooGleFitActivityModel> getActivityData(@Field("user_id") String user_id,
                                                    @Field("date") String date);

    @FormUrlEncoded
    @POST("insert_calories")
    Call<CaloriesDataModel> InsertCalories(@Field("user_id") String user_id,
                                           @Field("date") String date,
                                           @Field("calories") String calories);

    @FormUrlEncoded
    @POST("fetch_location")
    Call<GetHomeLocationModel> getHomeLocation(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("store_location")
    Call<StoreCurrentHomeAddressModel> storeLocation(@Field("user_id") String user_id,
                                                     @Field("address") String address,
                                                     @Field("latitude") String latitude,
                                                     @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("home_location")
    Call<HomeLocationStoreModel> storeHomeLocation(@Field("user_id") String user_id,
                                                   @Field("date") String date,
                                                   @Field("address") String address,
                                                   @Field("latitude") String latitude,
                                                   @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("time_diffrent")
    Call<AddressTimeModel> getAddressTime(@Field("user_id") String user_id,
                                          @Field("date") String date);

    @FormUrlEncoded
    @POST("change_location")
    Call<LocationChgangeModel> locationChange(@Field("user_id") String user_id,
                                              @Field("date") String date,
                                              @Field("address") String address,
                                              @Field("latitude") String latitude,
                                              @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("fblogin")
    Call<LoginWithFbModel> getLoginWithFb(@Field("fbid") String fbid,
                                          @Field("username") String username,
                                          @Field("email") String email,
                                          @Field("image") String image,
                                          @Field("device_id") String device_id,
                                          @Field("firebase_id") String firebase_id);

    @FormUrlEncoded
    @POST("work_location_insert")
    Call<InsertWorkLocationModel> insertWorkLocation(@Field("user_id") String user_id,
                                                     @Field("address") String address,
                                                     @Field("latitude") String latitude,
                                                     @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("work_location")
    Call<ChangeWorkLocationModel> StoreWorkLocarion(@Field("user_id") String user_id,
                                                    @Field("address") String address,
                                                    @Field("latitude") String latitude,
                                                    @Field("longitude") String longitude,
                                                    @Field("date") String date);

    @FormUrlEncoded
    @POST("time_difference_of_work_location")
    Call<WorkTimeDiffrentModel> getWorkTime(@Field("user_id") String user_id,
                                            @Field("date") String date);

    @FormUrlEncoded
    @POST("screenlockout")
    Call<LockCountModel> getLockCount(@Field("user_id") String user_id,
                                      @Field("date") String date);

    @FormUrlEncoded
    @POST("showscreenlockout")
    Call<LockCountShowModel> showLockCount(@Field("user_id") String user_id,
                                           @Field("date") String date);

    @FormUrlEncoded
    @POST("getworkaddress")
    Call<GetWorkAddressModel> getWorkAddress(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getpercentage")
    Call<PercentageModel> getPercentage(@Field("user_id") String user_id,
                                        @Field("date") String date);

    @FormUrlEncoded
    @POST("GetAverageUsage")
    Call<DashBoardAverageModel> getDashBoardData(@Field("user_id") String user_id,
                                                 @Field("date") String date);

    @FormUrlEncoded
    @POST("foot_step_count")
    Call<InsertStepCountModel> insertStep(@Field("user_id") String user_id,
                                          @Field("date") String date,
                                          @Field("foot_step") String foot_step,
                                          @Field("foot_move_minute") String foot_move_minute);

    @FormUrlEncoded
    @POST("foot_step_percentage")
    Call<StepCountPerModel> getStepPer(@Field("user_id") String user_id,
                                       @Field("date") String date);

    @FormUrlEncoded
    @POST("foot_step_info")
    Call<StepDisplayModel> setStepData(@Field("user_id") String user_id,
                                       @Field("date") String date);

    @FormUrlEncoded
    @POST("all_data_count")
    Call<SocialItegrationModel> getAllSocialData(@Field("user_id") String user_id,
                                                 @Field("date") String date,
                                                 @Field("day") String day);

    @FormUrlEncoded
    @POST("edit_profile_with_image")
    Call<EditProfileModel> editProfile(@Field("user_id") String user_id,
                                       @Field("email") String email,
                                       @Field("mobile_number") String mobile_number,
                                       @Field("username") String username);

    @Multipart
    @POST("edit_profile_with_image")
    Call<EditProfileModel> editWIthImagesProfile(@Part("user_id") RequestBody user_id,
                                                 @Part("email") RequestBody email,
                                                 @Part("mobile_number") RequestBody mobile_number,
                                                 @Part MultipartBody.Part file,
                                                 @Part("username") RequestBody username);

}

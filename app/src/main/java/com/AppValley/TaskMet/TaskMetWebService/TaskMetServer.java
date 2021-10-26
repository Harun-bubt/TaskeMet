package com.AppValley.TaskMet.TaskMetWebService;

import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.AdPostResponse;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.CommonAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.HomeAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.JobAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MarriageAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyPostModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyPostWithLimitModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PremiumAcModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.ProfileDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PropertyAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.Status_Response;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserAdsMapModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserCommonAds;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserJobAds;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserMarriageAds;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserPropertyAds;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserShoppingAds;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.ViewMyPostModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.WalletInfoModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.WalletModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface TaskMetServer {

    //------------------ Base URL ------------------
    String BASE_URL = "http://thetaskmet.com/api/";
    String BASE_CREATE_AD = "ad/store";
    //----------------------------------------------



    //For timeout calculation in calls
    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .callTimeout(6, TimeUnit.SECONDS)
            .connectTimeout(6, TimeUnit.SECONDS)
            .readTimeout(6, TimeUnit.SECONDS)
            .writeTimeout(6, TimeUnit.SECONDS)
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //------------------------------------- Registration api ---------------------------------------


    //----------------------- Checking user status -----------------------
    @GET("customer/getCustomer/{number}")
    Call<Status_Response> getUserStatus(
            @Path("number") String number
    );


    //.........................Retrive one by one Data....................
    @GET("customer/{number}")
    Call<ProfileDataModel> getProfileData(
            @Path("number") String number
    );


    //.........................Retrieve My Posts Data....................

    @GET("customer/myads/{number}/{date}")
    Call<MyPostWithLimitModel> getMyPosts(
            @Path("number") String number,
            @Path("date") String date
    );

    @GET
    Call<MyPostWithLimitModel> getMyPosts(@Url String url);

    //--------------- Creating user & storing user details ---------------
    @Multipart
    @POST("customer/store")
    Call<Status_Response> createUser(

            @Part MultipartBody.Part profile_pic,
            @Part("number") RequestBody number,
            @Part("uid") RequestBody uid,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("country_name") RequestBody country_name,
            @Part("city_name") RequestBody city_name,
            @Part("country_code") RequestBody country_code,
            @Part("address") RequestBody address,
            @Part("currency") RequestBody currency,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude

    );

    //--------------- Creating shopping post ---------------
    @Multipart
    @POST(BASE_CREATE_AD)
    Call<AdPostResponse> createShopPost(

            @Part MultipartBody.Part[] images,
            @Part("customer_number") RequestBody customer_number,
            @Part("token") RequestBody token,
            @Part("data[title]") RequestBody title,
            @Part("data[mainCategory]") RequestBody mainCategory,
            @Part("data[subCategory]") RequestBody subCategory,
            @Part("data[condition]") RequestBody condition,
            @Part("data[condition_meter]") RequestBody condition_meter,
            @Part("data[price]") RequestBody price,
            @Part("data[description]") RequestBody description,
            @Part("countryName") RequestBody countryName,
            @Part("cityName") RequestBody cityName,
            @Part("address") RequestBody address,
            @Part("currentDate") RequestBody currentDate,
            @Part("expireDate") RequestBody expireDate,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("isShowLocation") RequestBody isShowLocation,
            @Part("isShowNumber") RequestBody isShowNumber,
            @Part("deviceToken") RequestBody deviceToken,
            @Part("key") RequestBody key


    );

    //--------------- Creating job post ---------------
    @Multipart
    @POST(BASE_CREATE_AD)
    Call<AdPostResponse> createJobServicePost(
            @Part MultipartBody.Part[] images,
            @Part("customer_number") RequestBody customer_number,
            @Part("token") RequestBody token,
            @Part("data[title]") RequestBody title,
            @Part("data[mainCategory]") RequestBody mainCategory,
            @Part("data[type]") RequestBody type,
            @Part("data[jobCategory]") RequestBody jobCategory,
            @Part("data[positionType]") RequestBody positionType,
            @Part("data[salaryFrom]") RequestBody salaryFrom,
            @Part("data[salaryTo]") RequestBody salaryTo,
            @Part("data[salaryPeriod]") RequestBody salaryPeriod,
            @Part("data[expectedSalary]") RequestBody expectedSalary,
            @Part("data[companyName]") RequestBody companyName,
            @Part("data[description]") RequestBody description,
            @Part("countryName") RequestBody countryName,
            @Part("cityName") RequestBody cityName,
            @Part("address") RequestBody address,
            @Part("currentDate") RequestBody currentDate,
            @Part("expireDate") RequestBody expireDate,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("isShowLocation") RequestBody isShowLocation,
            @Part("isShowNumber") RequestBody isShowNumber,
            @Part("deviceToken") RequestBody deviceToken,
            @Part("key") RequestBody key
    );
    //..............Service marriagePost...............................
    @Multipart
    @POST(BASE_CREATE_AD)
    Call<AdPostResponse> createMarriageServicePost(
            @Part MultipartBody.Part[] images,
            @Part("customer_number") RequestBody customer_number,
            @Part("token") RequestBody token,
            @Part("data[title]") RequestBody title,
            @Part("data[mainCategory]") RequestBody mainCategory,
            @Part("data[lookingFor]") RequestBody lookingFor,
            @Part("data[maritialStatus]") RequestBody maritialStatus,
            @Part("data[myHeight]") RequestBody myHeight,
            @Part("data[myAge]") RequestBody myAge,
            @Part("data[myReligion]") RequestBody myReligion,
            @Part("data[myCastClan]") RequestBody myCastClan,
            @Part("data[myEducation]") RequestBody myEducation,
            @Part("data[myJob]") RequestBody myJob,
            @Part("data[mySalary]") RequestBody mySalary,
            @Part("data[writeMyself]") RequestBody writeMyself,
            @Part("data[partnerHeight]") RequestBody partnerHeight,
            @Part("data[partnerAge]") RequestBody partnerAge,
            @Part("data[partnerReligion]") RequestBody partnerReligion,
            @Part("data[partnerCastClan]") RequestBody partnerCastClan,
            @Part("data[partnerEducation]") RequestBody partnerEducation,
            @Part("data[partnerJob]") RequestBody partnerJob,
            @Part("data[aboutPartner]") RequestBody aboutPartner,
            @Part("countryName") RequestBody countryName,
            @Part("cityName") RequestBody cityName,
            @Part("address") RequestBody address,
            @Part("currentDate") RequestBody currentDate,
            @Part("expireDate") RequestBody expireDate,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("isShowLocation") RequestBody isShowLocation,
            @Part("isShowNumber") RequestBody isShowNumber,
            @Part("deviceToken") RequestBody deviceToken,
            @Part("key") RequestBody key
    );

    //--------------- Creating common Service post ---------------
    @Multipart
    @POST(BASE_CREATE_AD)
    Call<AdPostResponse> createCommonService(

            @Part MultipartBody.Part[] images,
            @Part("customer_number") RequestBody customer_number,
            @Part("token") RequestBody token,
            @Part("data[title]") RequestBody title,
            @Part("data[mainCategory]") RequestBody mainCategory,
            @Part("data[cost]") RequestBody cost,
            @Part("data[description]") RequestBody description,
            @Part("countryName") RequestBody countryName,
            @Part("cityName") RequestBody cityName,
            @Part("address") RequestBody address,
            @Part("currentDate") RequestBody currentDate,
            @Part("expireDate") RequestBody expireDate,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("isShowLocation") RequestBody isShowLocation,
            @Part("isShowNumber") RequestBody isShowNumber,
            @Part("deviceToken") RequestBody deviceToken,
            @Part("key") RequestBody key

    );
    //--------------- Creating Property Service post ---------------
    @Multipart
    @POST(BASE_CREATE_AD)
    Call<AdPostResponse> createPropertyService(

            @Part MultipartBody.Part[] images,
            @Part("customer_number") RequestBody customer_number,
            @Part("token") RequestBody token,
            @Part("data[title]") RequestBody title,
            @Part("data[mainCategory]") RequestBody mainCategory,
            @Part("data[description]") RequestBody description,
            @Part("data[cost]") RequestBody cost,
            @Part("data[propertyPostType]") RequestBody propertyPostType,
            @Part("data[propertyType]") RequestBody propertyType,
            @Part("data[areaType]") RequestBody areaType,
            @Part("data[area]") RequestBody area,
            @Part("data[areaUnit]") RequestBody areaUnit,
            @Part("data[furnished]") RequestBody furnished,
            @Part("data[floorLevel]") RequestBody floorLevel,
            @Part("data[totalFloors]") RequestBody totalFloors,
            @Part("countryName") RequestBody countryName,
            @Part("cityName") RequestBody cityName,
            @Part("address") RequestBody address,
            @Part("currentDate") RequestBody currentDate,
            @Part("expireDate") RequestBody expireDate,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("isShowLocation") RequestBody isShowLocation,
            @Part("isShowNumber") RequestBody isShowNumber,
            @Part("deviceToken") RequestBody deviceToken,
            @Part("key") RequestBody key

    );

    //-------------------- API to delete post  -------------------

    @DELETE("ad/delete/{id}")
    Call<Status_Response> deleteMyPost(
            @Path("id") int id
    );

    //------------------- API to make Ad Premium -----------------

    @POST("ad/makepremium/{id}/{type}")
    Call<Status_Response> makeAdPremium(
            @Path("id") int id,
            @Path("type") String type
    );

    //------------------- API to mark Ad as Sold -----------------

    @POST("ad/markcomplete/{id}")
    Call<Status_Response> soldMyPost(
            @Path("id") String id
    );

    //------------------- API to Edit Ad  -----------------

    @PATCH("data/update/{id}/{key}")
    Call<Status_Response> editMyPost(
            @Path("id") int id,
            @Path("key") String key
    );

    //.........................Get post data From API....................
    @GET("ad/view/{id}")
    Call<ViewMyPostModel> getMyPostData(
            @Path("id") String id
    );

    //......................Get All Ads Markers for map...................

    @GET("ads/map")
    Call<List<UserAdsMapModel>> getAdsMapMarkers(
            @Query("number") String number,
            @Query("mainCategory") String mainCategory,
            @Query("subCategory") String subCategory,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("date") String date
    );

    @GET("ads/map")
    Call<List<UserAdsMapModel>> getAdsMapMarkers(
            @Query("number") String number,
            @Query("mainCategory") String mainCategory,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("date") String date
    );

    //--------------------- Edit shopping post only Text------------------
    @FormUrlEncoded
    @POST("ad/update/{id}")
    Call<Status_Response> editShopPost(
            @Path("id") String id,

            @Field("data[title]") String title,
            @Field("data[subCategory]") String  subCategory,
            @Field("data[condition]") String condition,
            @Field("data[condition_meter]") String condition_meter,
            @Field("data[price]") String price,
            @Field("data[description]") String description

    );

    //--------------- Edit shopping post text with images---------------
    @Multipart
    @POST("ad/update/{id}")
    Call<Status_Response> editShopWithImagePost(

            @Path("id") String id,

            @Part MultipartBody.Part[] images,
            @Part("data[title]") RequestBody title,
            @Part("data[condition]") RequestBody condition,
            @Part("data[condition_meter]") RequestBody condition_meter,
            @Part("data[price]") RequestBody price,
            @Part("data[description]") RequestBody description

    );

    @FormUrlEncoded
    @POST("ad/update/{id}")
    Call<Status_Response> editCommonPost(
            @Path("id") String id,

            @Field("data[title]") String title,
            @Field("data[cost]") String cost,
            @Field("data[description]") String description

    );
    //--------------- Edit common post text with images---------------
    @Multipart
    @POST("ad/update/{id}")
    Call<Status_Response> editCommonWithImagePost(

            @Path("id") String id,

            @Part MultipartBody.Part[] images,
            @Part("data[title]") RequestBody title,
            @Part("data[cost]") RequestBody cost,
            @Part("data[description]") RequestBody description

    );
    @FormUrlEncoded
    @POST("ad/update/{id}")
    Call<Status_Response> editMarriagePost(

            @Path("id") String id,

            @Field("data[title]") String title,
            @Field("data[lookingFor]") String lookingFor,
            @Field("data[maritialStatus]") String maritialStatus,
            @Field("data[myHeight]") String myHeight,
            @Field("data[myAge]") String myAge,
            @Field("data[myReligion]") String myReligion,
            @Field("data[myCastClan]") String myCastClan,
            @Field("data[myEducation]") String myEducation,
            @Field("data[myJob]") String myJob,
            @Field("data[mySalary]") String mySalary,
            @Field("data[writeMyself]") String writeMyself,
            @Field("data[partnerHeight]") String partnerHeight,
            @Field("data[partnerAge]") String partnerAge,
            @Field("data[partnerReligion]") String partnerReligion,
            @Field("data[partnerCastClan]") String partnerCastClan,
            @Field("data[partnerEducation]") String partnerEducation,
            @Field("data[partnerJob]") String partnerJob,
            @Field("data[aboutPartner]") String aboutPartner
    );
    //--------------- Edit Marriage post text with images---------------
    @Multipart
    @POST("ad/update/{id}")
    Call<Status_Response> editMarriageWithImagePost(

            @Path("id") String id,

            @Part MultipartBody.Part[] images,

            @Part("data[title]") RequestBody title,
            @Part("data[lookingFor]") RequestBody lookingFor,
            @Part("data[maritialStatus]") RequestBody maritialStatus,
            @Part("data[myHeight]") RequestBody myHeight,
            @Part("data[myAge]") RequestBody myAge,
            @Part("data[myReligion]") RequestBody myReligion,
            @Part("data[myCastClan]") RequestBody myCastClan,
            @Part("data[myEducation]") RequestBody myEducation,
            @Part("data[myJob]") RequestBody myJob,
            @Part("data[mySalary]") RequestBody mySalary,
            @Part("data[writeMyself]") RequestBody writeMyself,
            @Part("data[partnerHeight]") RequestBody partnerHeight,
            @Part("data[partnerAge]") RequestBody partnerAge,
            @Part("data[partnerReligion]") RequestBody partnerReligion,
            @Part("data[partnerCastClan]") RequestBody partnerCastClan,
            @Part("data[partnerEducation]") RequestBody partnerEducation,
            @Part("data[partnerJob]") RequestBody partnerJob,
            @Part("data[aboutPartner]") RequestBody aboutPartner

    );

    @FormUrlEncoded
    @POST("ad/update/{id}")
    Call<Status_Response> editJobPost(

            @Path("id") String id,

            @Field("data[title]") String title,
            @Field("data[type]") String type,
            @Field("data[jobCategory]") String jobCategory,
            @Field("data[positionType]") String positionType,
            @Field("data[salaryFrom]") String salaryFrom,
            @Field("data[salaryTo]") String salaryTo,
            @Field("data[salaryPeriod]") String salaryPeriod,
            @Field("data[companyName]") String companyName,
            @Field("data[description]") String description

    );

    //--------------- Edit Job post text with images---------------
    @Multipart
    @POST("ad/update/{id}")
    Call<Status_Response> editJobWithImagePost(

            @Path("id") String id,

            @Part MultipartBody.Part[] images,
            @Part("data[title]") RequestBody title,
            @Part("data[type]") RequestBody type,
            @Part("data[jobCategory]") RequestBody jobCategory,
            @Part("data[positionType]") RequestBody positionType,
            @Part("data[salaryFrom]") RequestBody salaryFrom,
            @Part("data[salaryTo]") RequestBody salaryTo,
            @Part("data[salaryPeriod]") RequestBody salaryPeriod,
            @Part("data[companyName]") RequestBody companyName,
            @Part("data[description]") RequestBody description

    );

    @FormUrlEncoded
    @POST("ad/update/{id}")
    Call<Status_Response> editPropertyService(
            @Path("id") String id,

            @Field("data[title]") String title,
            @Field("data[description]") String description,
            @Field("data[cost]") String cost,
            @Field("data[propertyPostType]") String propertyPostType,
            @Field("data[propertyType]") String propertyType,
            @Field("data[areaType]") String areaType,
            @Field("data[area]") String area,
            @Field("data[areaUnit]") String areaUnit,
            @Field("data[furnished]") String furnished,
            @Field("data[floorLevel]") String floorLevel,
            @Field("data[totalFloors]") String totalFloors

    );

    //--------------- Edit Property post text with images---------------
    @Multipart
    @POST("ad/update/{id}")
    Call<Status_Response> editPropertyWithImagePost(

            @Path("id") String id,

            @Part MultipartBody.Part[] images,
            @Part("data[title]") RequestBody title,
            @Part("data[description]") RequestBody description,
            @Part("data[cost]") RequestBody cost,
            @Part("data[propertyPostType]") RequestBody propertyPostType,
            @Part("data[propertyType]") RequestBody propertyType,
            @Part("data[areaType]") RequestBody areaType,
            @Part("data[area]") RequestBody area,
            @Part("data[areaUnit]") RequestBody areaUnit,
            @Part("data[furnished]") RequestBody furnished,
            @Part("data[floorLevel]") RequestBody floorLevel,
            @Part("data[totalFloors]") RequestBody totalFloors

    );


    //.........................Get Wallet & Package data From API....................
    @GET("customer/wallet/{number}")
    Call<WalletModel> getWalletData(
            @Path("number") String number
    );



    //.........................Post Now - Paid - API....................
    @PATCH("ad/postnow/pay/{id}/{type}")
    Call<Status_Response> postNowPaid(
            @Path("id") int id,
            @Path("type") String type
    );


    //.........................Post Now - Free - API....................
    @PATCH("ad/postnow/{id}")
    Call<Status_Response> postNowFree(
            @Path("id") int id
    );

    //.........................Update profile Name....................
    @FormUrlEncoded
    @PATCH("customer/update/{number}")
    Call<Status_Response> updateProfileName(
            @Path("number") String number,
            @Field("name") String name
    );
    //.........................Update profile currecny....................
    @PATCH("customer/update/{number}")
    Call<Status_Response> updateProfileCurrency(
            @Path("number") String number,
            @Field("currency") String currency
    );
    //.........................Update profile location....................

    @FormUrlEncoded
    @PATCH("customer/update/{number}")
    Call<Status_Response> updateProfileLocation(
            @Path("number") String number,
            @Field("country_name") String country_name,
            @Field("city_name") String city_name,
            @Field("country_code") String country_code,
            @Field("address") String address,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("currency") String currency
    );

//.........................Retrieve My Posts Data....................

    @GET("ads")
    Call<UserShoppingAds> getHomeRecentAd(
            @Query("number") String number,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("date") String date
    );

    @GET
    Call<UserShoppingAds> getHomeRecentAd(@Url String url);

    //...................delete my accouont....................


    @FormUrlEncoded
    @POST("customer/delete")
    Call<Status_Response> deleteAccount(
            @Field("number") String number
    );

    //..................................... Get Search Ads Data ....................................

    @GET("ads/search")
    Call<UserShoppingAds> getSearchAds(
            @Query("search") String search,
            @Query("number") String number,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("date") String date
    );

    //For Next Page Shopping ad data
    @GET
    Call<UserShoppingAds> getSearchAds(@Url String url);

    //.........................Retrieve shopping ad according to category Data....................

    @GET("ads/shopping")
    Call<UserShoppingAds> getHomeAdByCategory(
            @Query("number") String number,
            @Query("mainCategory") String mainCategory,
            @Query("subCategory") String subCategory,
            @Query("sort") String sort,
            @Query("condition") String condition,
            @Query("priceFrom") String priceFrom,
            @Query("priceTo") String priceTo,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("date") String date,
            @Query("distance") String distance
    );

    //For Next Page Shopping ad data
    @GET
    Call<UserShoppingAds> getHomeAdByCategory(@Url String url);

    //.........................Retrieve Common Ad according to category Data....................

    @GET("ads/commonservice")
    Call<UserCommonAds> getCommonAdByCategory(
            @Query("number") String number,
            @Query("mainCategory") String mainCategory,
            @Query("sort") String sort,
            @Query("priceFrom") String priceFrom,
            @Query("priceTo") String priceTo,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("date") String date,
            @Query("distance") String distance
    );

    //For Next Page common ad data
    @GET
    Call<UserCommonAds> getCommonAdByCategory(@Url String url);

    //.........................Retrieve job Ad according to category Data....................

    @GET("ads/jobservice")
    Call<UserJobAds> getJobAdByCategory(
            @Query("number") String number,
            @Query("mainCategory") String mainCategory,
            @Query("type") String type,
            @Query("jobCategory") String jobCategory,
            @Query("positionType") String positionType,
            @Query("sort") String sort,
            @Query("salaryPeriod") String salaryPeriod,
            @Query("salaryFrom") String salaryFrom,
            @Query("salaryTo") String salaryTo,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("date") String date,
            @Query("distance") String distance
    );


    //For Next Page job ad data
    @GET
    Call<UserJobAds> getJobAdByCategory(@Url String url);


    //.........................Retrieve Marriage Ad according to category Data....................

    @GET("ads/marriageservice")
    Call<UserMarriageAds> getMarriageAdByCategory(
            @Query("number") String number,
            @Query("mainCategory") String mainCategory,
            @Query("lookingFor") String lookingFor,
            @Query("maritialStatus") String maritialStatus,
            @Query("ageFrom") String ageFrom,
            @Query("ageTo") String ageTo,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("date") String date,
            @Query("distance") String distance
    );

    //For Next Marriage job ad data
    @GET
    Call<UserMarriageAds> getMarriageAdByCategory(@Url String url);

    //.........................Retrieve Property Ad according to category Data....................

    @GET("ads/propertyservice1")
    Call<UserPropertyAds> getLandPropertyAds(
            @Query("number") String number,
            @Query("mainCategory") String mainCategory,
            @Query("propertyPostType") String propertyPostType,
            @Query("propertyType") String propertyType,
            @Query("areaType") String areaType,
            @Query("costFrom") String costFrom,
            @Query("costTo") String costTo,
            @Query("areaFrom") String areaFrom,
            @Query("areaTo") String areaTo,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("date") String date,
            @Query("distance") String distance
    );

    @GET("ads/propertyservice2")
    Call<UserPropertyAds> getHouseShopPropertyAds(
            @Query("number") String number,
            @Query("mainCategory") String mainCategory,
            @Query("propertyPostType") String propertyPostType,
            @Query("propertyType") String propertyType,
            @Query("areaType") String areaType,
            @Query("costFrom") String costFrom,
            @Query("costTo") String costTo,
            @Query("areaFrom") String areaFrom,
            @Query("areaTo") String areaTo,
            @Query(" furnished") String  furnished,
            @Query(" floorLevelFrom") String  floorLevelFrom,
            @Query(" floorLevelTo") String  floorLevelTo,
            @Query(" totalFloorsFrom") String  totalFloorsFrom,
            @Query(" totalFloorsTo") String  totalFloors,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("date") String date,
            @Query("distance") String distance
    );

    //For Next page Lands & Plots Property ad data
    @GET
    Call<UserPropertyAds> getLandPropertyAds(@Url String url);


    //For Next page Houses & Shops Property ad data
    @GET
    Call<UserPropertyAds> getHouseShopPropertyAds(@Url String url);


    //.........................Retrieve My Posts Data....................

    @GET("ad/detail")
    Call<WalletInfoModel> getAccountInfo(
            @Query("number") String number,
            @Query("date") String date
    );

    //......................... Get Premium Account ....................

    @GET("account/premium")
    Call<PremiumAcModel> getPremiumAccount(
            @Query("number") String number
    );

    //......................... Update profile Picture ....................
    @Multipart
    @POST("customer/update/picture/{number}")
    Call<Status_Response> updateProfilePicture(
            @Path("number") String number,
            @Part MultipartBody.Part profile_pic
    );

    //......................... Upload Transaction ScreenShot ....................
    @Multipart
    @POST("payment/request")
    Call<Status_Response> uploadTidScreenShot(
            @Part MultipartBody.Part image,
            @Part("customer_number") RequestBody customer_number,
            @Part("rc") RequestBody rc,
            @Part("gc") RequestBody gc,
            @Part("tid") RequestBody tid,
            @Part("cost") RequestBody cost,
            @Part("purchase_date") RequestBody purchase_date,
            @Part("deviceToken") RequestBody deviceToken,
            @Part("account_title") RequestBody account_title,
            @Part("account_number") RequestBody account_number
    );


}
package xyz.finity.vision.libs.services;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import xyz.finity.vision.libs.models.Label;
import xyz.finity.vision.libs.models.VisionData;

/**
 * Created by dwiva on 4/22/19.
 * This code is a part of Vision project
 */
public interface VisionService {
    @GET("/")
    Call<ResponseBody> test();

    @Multipart
    @POST("mobile_check_old.php")
    Call<List<Label>> checkOld(
            @Part MultipartBody.Part filePart,
            @Part("description") RequestBody description);

    @Multipart
    @POST("mobile_check.php")
    Call<VisionData> check(
            @Part MultipartBody.Part filePart,
            @Part("description") RequestBody description);
}

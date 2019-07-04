package com.retrofit.Interfaces;

import com.retrofit.Models.MyResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadFile {

    @Multipart
    @POST("retrofit.php")
    Call<MyResponse> uploadImage(@Part("image\"; filename=\"myfile1.iso1\" ") RequestBody file, @Part("desc") RequestBody desc);
    //Call<MyResponse> uploadImage(@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file, @Part("desc") RequestBody desc);
}

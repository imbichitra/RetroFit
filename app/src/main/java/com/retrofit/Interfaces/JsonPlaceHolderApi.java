package com.retrofit.Interfaces;


import com.retrofit.Models.Comment;
import com.retrofit.Models.Post;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    @GET("posts") //Get all post /posts
    Call<List<Post>> getPosts();

    @GET("posts") //get particular id  /posts?userId=1
    Call<List<Post>> getPosts(@Query("userId") int userId);

    @GET("posts") //multiple query  /posts?userId=1&_sort=id&_order=desc for null pass for userId declare Integer instead of int
    Call<List<Post>> getPosts(
            @Query("userId") int userId, //you can get multiple userId as Inter[] userId
            @Query("_sort") String sort,
            @Query("_order") String order
    );

    @GET("posts") //posts?userId=1&_sort=id&_order=desc
    Call<List<Post>> getPosts(@QueryMap Map<String,String> parametrs);

    @GET("posts/{id}/comments") //posts/2/comments
    Call<List<Comment>> getComments(@Path("id") int postId);

    @GET
    Call<List<Comment>> getComments(@Url String url);

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("body") String text
    );

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String,String> fields);

    @PUT("posts/{id}")
    Call<Post> putPost(@Path("id") int id,@Body Post post);

    @PATCH("posts/{id}")
    Call<Post> patchPost(@Path("id") int id,@Body Post post);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);
}

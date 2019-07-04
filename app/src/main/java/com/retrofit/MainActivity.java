package com.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.retrofit.Interfaces.JsonPlaceHolderApi;
import com.retrofit.Interfaces.UploadFile;
import com.retrofit.Models.Comment;
import com.retrofit.Models.MyResponse;
import com.retrofit.Models.Post;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);

        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        //getPosts();
        //getComments();
        //createPost();
        //updatePost();
        //deletePost();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }
    }

    private void getPosts() {
        Map<String, String> parmeters = new HashMap<>();
        ;
        parmeters.put("userId", "1");
        parmeters.put("_sort", "id");
        parmeters.put("_order", "desc");
        //Call<List<Post>> call = jsonPlaceHolderApi.getPosts();
        //Call<List<Post>> call = jsonPlaceHolderApi.getPosts(1);
        //Call<List<Post>> call = jsonPlaceHolderApi.getPosts(1,"id","desc"); //you can pass null parameter also

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parmeters);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    text.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();
                StringBuilder sb = new StringBuilder();
                for (Post post : posts) {

                    sb.append("Id: ").append(post.getId()).append("\n");
                    sb.append("User Id: ").append(post.getUserId()).append("\n");
                    sb.append("Title: ").append(post.getTitle()).append("\n");
                    sb.append("Text: ").append(post.getText()).append("\n\n");

                }
                text.setText(sb.toString());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                text.setText(t.getMessage());
            }
        });

    }

    private void getComments() {
        //Call<List<Comment>> call = jsonPlaceHolderApi.getComments(1);
        Call<List<Comment>> call = jsonPlaceHolderApi.getComments("posts/3/comments");

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    text.setText("Code:" + response.code());
                    return;
                }

                List<Comment> comments = response.body();
                StringBuilder sb = new StringBuilder();
                for (Comment comment : comments) {

                    sb.append("Id: ").append(comment.getId()).append("\n");
                    sb.append("User Id: ").append(comment.getUserId()).append("\n");
                    sb.append("Name: ").append(comment.getName()).append("\n");
                    sb.append("Email: ").append(comment.getEmail()).append("\n");
                    sb.append("Text: ").append(comment.getText()).append("\n\n");

                }
                text.setText(sb.toString());
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                text.setText(t.getMessage());
            }
        });
    }

    private void createPost() {
        Map<String, String> parmeters = new HashMap<>();
        ;
        parmeters.put("userId", "24");
        parmeters.put("title", "New Title");
        parmeters.put("body", "New Text");


        Post post = new Post(23, "New Title", "New Text");

        //Call<Post> call = jsonPlaceHolderApi.createPost(post);
        //Call<Post> call = jsonPlaceHolderApi.createPost(23,"New Title","New Text");
        Call<Post> call = jsonPlaceHolderApi.createPost(parmeters);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    text.setText("Code" + response.code());
                    return;
                }

                Post postResponse = response.body();

                StringBuilder sb = new StringBuilder();
                sb.append("Code: ").append(response.code()).append("\n");
                sb.append("Id: ").append(postResponse.getId()).append("\n");
                sb.append("User Id: ").append(postResponse.getUserId()).append("\n");
                sb.append("Title: ").append(postResponse.getTitle()).append("\n");
                sb.append("Text: ").append(postResponse.getText()).append("\n\n");

                text.setText(sb.toString());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                text.setText(t.getMessage());
            }
        });
    }

    private void updatePost() {
        Post post = new Post(12, null, "Bichi");

        Call<Post> call = jsonPlaceHolderApi.putPost(5, post);
        //Call<Post> call = jsonPlaceHolderApi.patchPost(5, post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    text.setText("Code:" + response.code());
                    return;
                }

                Post postResponse = response.body();

                StringBuilder sb = new StringBuilder();
                sb.append("Code: ").append(response.code()).append("\n");
                sb.append("Id: ").append(postResponse.getId()).append("\n");
                sb.append("User Id: ").append(postResponse.getUserId()).append("\n");
                sb.append("Title: ").append(postResponse.getTitle()).append("\n");
                sb.append("Text: ").append(postResponse.getText()).append("\n\n");


                text.setText(sb.toString());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                text.setText(t.getMessage());
            }
        });
    }

    private void deletePost(){
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                text.setText("Code:"+response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                text.setText(t.getMessage());
            }
        });
    }

    public void choose(View view) {
        /*Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 100);*/
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //the image URI
            Uri selectedImage = data.getData();
            uploadFile(selectedImage, "My Image");
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void uploadFile(Uri fileUri, String desc) {

        //creating a file
        File file = new File(getRealPathFromURI(fileUri));

        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);

        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);

        //The gson builder
        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://marketzoo.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        //creating our api
        UploadFile api = retrofit.create(UploadFile.class);

        //creating a call and calling the upload image method
        Call<MyResponse> call = api.uploadImage(requestFile, descBody);

        //finally performing the call
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Main", "onFailure: "+t.getMessage());
            }
        });
    }
}

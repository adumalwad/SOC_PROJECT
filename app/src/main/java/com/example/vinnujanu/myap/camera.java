package com.example.vinnujanu.myap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

//import com.android.internal.http.multipart.MultipartEntity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.entity.mime.MultipartEntity;

public class camera extends AppCompatActivity {
    Button btnCamera0;
    Button btnCamera1;
    ImageView imageView;
    ImageView imageVieww2;
    Button submit;





    String roll,course;
    String img1fp="";
    String img2fp="";
    String FileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        btnCamera0 = (Button)findViewById(R.id.select_image_0);
        btnCamera1=(Button)findViewById(R.id.select_image_1);
        imageView = (ImageView)findViewById(R.id.image_0);
        imageVieww2=(ImageView)findViewById(R.id.image_1);

        submit=(Button)findViewById(R.id.verify);
        Bundle extras=getIntent().getExtras();
        roll=extras.getString("Roll");
        course=extras.getString("Course");

        System.out.println(roll);
        System.out.println(course);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(camera.this);
                final String url="http:172.17.76.125:8000/upload/";
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response", response);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Rollno", roll);

                        params.put("img1",img1fp);
                        params.put("Course", course);
                        params.put("img2",img2fp);
                        return params;
                    }
                };
                queue.add(postRequest);


                System.exit(0);
            }



        });
        btnCamera0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,20);

            }
        });
        btnCamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,30);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            super.onActivityResult(requestCode, resultCode, data);
            if( resultCode == RESULT_OK){
                Bitmap bitma=(Bitmap)data.getExtras().get("data");
               if(requestCode==20) {
                   imageView.setImageBitmap(bitma);
                   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                   bitma.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                   byte[] byteArray = byteArrayOutputStream .toByteArray();
                   img1fp= android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
               }
               else {
                   imageVieww2.setImageBitmap(bitma);
                   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                   bitma.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                   byte[] byteArray = byteArrayOutputStream .toByteArray();
                   img2fp= android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
               }
            }

    }









}

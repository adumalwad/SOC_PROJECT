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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

//import com.android.internal.http.multipart.MultipartEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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


    String url="http://172.17.76.125:8000/upload/";


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
                // TODO Auto-generated method stub
                uploadFile(url,img1fp,img2fp);
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
       // getImageFileFromSDCard();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            super.onActivityResult(requestCode, resultCode, data);
            if( resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");

                String partFilename = currentDateFormat();
                storeCameraPhotoInSDCard(bitmap, partFilename);

                // display the image from SD Card to ImageView Control
                String storeFilename = "photo_" + partFilename + ".jpg";
                File imageFile = new File(Environment.getExternalStorageDirectory() + storeFilename);
                if(requestCode==20)
                    img1fp=imageFile.getAbsolutePath();
                else
                    img2fp=imageFile.getAbsolutePath();

                //Bitmap mBitmap = getImageFileFromSDCard(storeFilename);
                Bitmap bitma=(Bitmap)data.getExtras().get("data");
               if(requestCode==20)
                   imageView.setImageBitmap(bitma);
               else
                   imageVieww2.setImageBitmap(bitma);

            }

    }
    public  String uploadFile(String url, String filepath,String filepath2) {
        try {
            //client.getParams().setParameter("http.socket.timeout", 90000); // 90 second
            HttpPost post = new HttpPost(url);
            HttpClient client=new DefaultHttpClient();

            MultipartEntity mpEntity = new MultipartEntity();
            mpEntity.addPart("Rollno",new StringBody(roll));
            mpEntity.addPart("Course",new StringBody(course));

            mpEntity.addPart("img1", new FileBody(new File(filepath), "image/jpeg"));
            mpEntity.addPart("img2",new FileBody(new File(filepath2),"image/jpeg"));
            post.setEntity(mpEntity);

            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() != 200) { return "false"; }
            else return "true";
        } catch (Exception e) {
           // if (SyncStateContract.Constants.DEBUG) e.printStackTrace();

            return "false";
        }
    }
    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate){
        File outputFile = new File(Environment.getExternalStorageDirectory(), "photo_" + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    /*private Bitmap getImageFileFromSDCard(){
        Bitmap bitmap = null;
        File imageFile = new File(Environment.getExternalStorageDirectory()+FileName);
        img1fp=imageFile.getAbsolutePath();
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }*/
}

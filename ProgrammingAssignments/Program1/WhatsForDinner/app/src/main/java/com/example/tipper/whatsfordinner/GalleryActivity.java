package com.example.tipper.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class GalleryActivity extends AppCompatActivity {

    private ImageView selectedImage;
    private int pos;
    public static final String LOCAL_IMAGES_PREF = "IMAGES_PREF" ;
    public static final String imageID = "imageKey";
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Gallery gallery = (Gallery) findViewById(R.id.gallery);
        selectedImage=(ImageView)findViewById(R.id.select_imageView);
        gallery.setSpacing(1);
        final GalleryImageAdapter galleryImageAdapter = new GalleryImageAdapter(this);
        gallery.setAdapter(galleryImageAdapter);


        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // show the selected Image
                selectedImage.setImageResource(galleryImageAdapter.mImageIds[position]);
                pos = position;
            }
        });

        Button submitImage = (Button) findViewById(R.id.submitImage_button);
        submitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageId(GalleryActivity.this, pos);
                Intent intent = new Intent(getApplicationContext(), NewDishActivity.class);
                startActivity(intent);
            }
        });

    }

    private void saveImageId(Context context, int position) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(LOCAL_IMAGES_PREF, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(imageID, String.valueOf(position)); //3
        editor.commit(); //4
    }

}



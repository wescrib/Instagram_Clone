package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class NewsFeedActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        setTitle("UserFeed");

        linearLayout = findViewById(R.id.linearLayout);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Images");
        query.whereEqualTo("username", getUsersFeed());
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    for(ParseObject obj : objects){
                        ParseFile file = (ParseFile) obj.get("image");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e==null && data !=null){
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    imageView = new ImageView(getApplicationContext());
                                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    ));
                                    imageView.setImageBitmap(bitmap);
                                    linearLayout.addView(imageView);

                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private String getUsersFeed(){
        Intent intent = getIntent();
        return intent.getStringExtra("username");
    }
}

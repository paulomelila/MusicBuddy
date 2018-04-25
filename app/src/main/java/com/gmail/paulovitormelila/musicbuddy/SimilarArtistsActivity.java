package com.gmail.paulovitormelila.musicbuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SimilarArtistsActivity extends AppCompatActivity {
    private TextView mArtistName;
    private TextView mArtistDescription;
    private ImageView mYoutubeIcon;
    private ImageView mWikipediaIcon;
    private ImageButton mNextArtistImgButton;
    private ImageView mArtistPhoto;

    private ArrayList<Music> mArtistsList;
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_artists);

        instantiateWidgets();

        getArtistsList();

        new PhotoLoader().execute();

        displayArtistInfo();

        logs();

        onCLickListeners();
    }

    private void instantiateWidgets() {
        // widgets
        mArtistName = findViewById(R.id.artistName);
        mArtistDescription = findViewById(R.id.artistDescription);
        mYoutubeIcon = findViewById(R.id.youtubeIcon);
        mWikipediaIcon = findViewById(R.id.wikipediaIcon);
        mNextArtistImgButton = findViewById(R.id.nextArtist);
        mArtistPhoto = findViewById(R.id.artistPhoto);
    }

    private void getArtistsList() {
        Intent intent = getIntent();
        mArtistsList = intent.getParcelableArrayListExtra("ArtistsList");
        Collections.shuffle(mArtistsList);
    }

    private void displayArtistInfo() {
        mArtistName.setText(mArtistsList.get(mIndex).getName());
        mArtistDescription.setText(mArtistsList.get(mIndex).getwTeaser());
    }

    private void onCLickListeners() {
        mYoutubeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youtubeAppIntent(mArtistsList.get(mIndex).getyID());
            }
        });

        mWikipediaIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wikipediaArticleIntent(mArtistsList.get(mIndex).getwUrl());
            }
        });

        mNextArtistImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIndex = (mIndex + 1) % mArtistsList.size();
                mArtistName.setText(mArtistsList.get(mIndex).getName());
                mArtistDescription.setText(mArtistsList.get(mIndex).getwTeaser());
                new PhotoLoader().execute();

                ScrollView mScrollView = findViewById(R.id.scrollview);
                mScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void youtubeAppIntent(String videoID) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + videoID));
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }

    private void wikipediaArticleIntent(String wikiPage) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(wikiPage));
        startActivity(intent);
    }

    private class PhotoLoader extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... voids) {
            String photoURL = "";

            try {
                Document doc = Jsoup.connect(mArtistsList.get(mIndex).getwUrl()).get();
                Element table = doc.getElementsByTag("table").first();
                Element td = table.getElementsByTag("td").first();

                if (!td.getElementsByTag("img").isEmpty()) {

                    if (td.getElementsByTag("img").first().hasAttr("src")) {
                        photoURL = "https://" + td.getElementsByTag("img")
                                .first()
                                .attr("src");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return photoURL;
        }

        @Override
        protected void onPostExecute(String photoURL) {
            attachPhoto(photoURL, mArtistPhoto);
        }
    }

    private void attachPhoto(String imageURL, ImageView imageView) {
        if (imageURL.isEmpty()) {
            Picasso.get()
                    .load(R.mipmap.photo_placeholder)
                    .into(imageView);
        } else {
            Picasso.get()
                    .load(imageURL)
                    .placeholder(R.mipmap.photo_placeholder)
                    .into(imageView);
        }
    }

    private void logs() {
        for (int i = 0; i < mArtistsList.size(); i++) {
            System.out.println((i+1) + ". " + mArtistsList.get(i).getName());
        }
    }
}



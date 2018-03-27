package com.gmail.paulovitormelila.musicbuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SimilarArtistsActivity extends AppCompatActivity {
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_artists);

        // widgets
        final TextView artistName = findViewById(R.id.name);
        final TextView artistDescription = findViewById(R.id.description);
        ImageView youtubeIcon = findViewById(R.id.youtubeIcon);
        ImageView wikipediaIcon = findViewById(R.id.wikipediaIcon);
        ImageButton findAnother = findViewById(R.id.findAnother);
        final ImageView artistPhoto = findViewById(R.id.artistPhoto);

        // intent to receive the ArrayList from the MainActivity
        Intent intent = getIntent();
        final ArrayList<Music> artistsList = intent.getParcelableArrayListExtra("ArtistsList");
        Collections.shuffle(artistsList);

        artistName.setText(artistsList.get(index).getName());
        artistDescription.setText(artistsList.get(index).getwTeaser());

        for (int i = 0; i < artistsList.size(); i++) {
            System.out.println((i+1) + ". " + artistsList.get(i).getName());
        }

        youtubeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youtubeAppIntent(artistsList.get(index).getyID());
            }
        });

        wikipediaIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wikipediaArticleIntent(artistsList.get(index).getwUrl());
            }
        });

        findAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = (index + 1) % artistsList.size();
                artistName.setText(artistsList.get(index).getName());
                artistDescription.setText(artistsList.get(index).getwTeaser());
                getArtistPhoto(artistsList.get(index).getwUrl(), artistPhoto);
            }
        });

        getArtistPhoto(artistsList.get(index).getwUrl(), artistPhoto);
    }

    // method to open youtube app when user taps on the youtube icon
    private void youtubeAppIntent(String videoID) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + videoID));
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }

    // method to open wikipedia when user taps on the wikipedia icon
    private void wikipediaArticleIntent(String wikiPage) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(wikiPage));
        startActivity(intent);
    }

    // method to set each artist's profile photo
    private void getArtistPhoto(String wikipediaURL, final ImageView artistPhoto) {
        //TODO: Fix NetworkOnMainThreadException
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Document doc = Jsoup.connect(wikipediaURL).get();
            Element table = doc.getElementsByTag("table").first();
            Element td = table.getElementsByTag("td").first();
            String img = "https://" + td.getElementsByTag("img").first().attr("src");
            downloadProfilePhoto(img, artistPhoto);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void downloadProfilePhoto(String imageURL, ImageView placeholder) {
        Picasso.get()
                .load(imageURL)
                .placeholder(R.mipmap.photo_placeholder)
                .into(placeholder);
    }
}

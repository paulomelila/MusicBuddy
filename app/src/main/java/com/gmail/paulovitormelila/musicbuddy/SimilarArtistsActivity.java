package com.gmail.paulovitormelila.musicbuddy;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class SimilarArtistsActivity extends AppCompatActivity {
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_artists);

        final TextView artistName = (TextView) findViewById(R.id.name);
        final TextView artistDescription = (TextView) findViewById(R.id.description);
        ImageView youtubeIcon = (ImageView) findViewById(R.id.youtubeIcon);
        ImageView wikipediaIcon = (ImageView) findViewById(R.id.wikipediaIcon);
        Button findAnother = (Button) findViewById(R.id.findAnother);

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
}

package com.gmail.paulovitormelila.musicbuddy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
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
                artistPhoto.setImageResource(R.mipmap.photo_placeholder);
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
    public void getArtistPhoto(String wikipediaURL, final ImageView artistPhoto) {
        String baseURL = "http://motyar.info/webscrapemaster/api/?url=";
        String endURL = "&xpath=//div[@id=mw-content-text]/div/table[1]/tbody/tr[2]/td/a/img#vws";
        String fullURL = baseURL + wikipediaURL + endURL;

        RequestQueue queue = Volley.newRequestQueue(this);

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, fullURL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                            String imageURL = "https:" + jsonObject.getString("src");

                            Log.e("Image URL: ", imageURL);

                            new DownloadImageTask(artistPhoto).execute(imageURL);

                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });

        queue.add(jsonArrayRequest);
    }

    // method to download each artist's profile photo
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        private DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

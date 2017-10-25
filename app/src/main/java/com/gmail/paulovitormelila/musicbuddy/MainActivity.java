package com.gmail.paulovitormelila.musicbuddy;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private EditText firstBand;
    private EditText secondBand;
    private Button findBand;
    private TextView resultBand;
    private Button youtubeBtn;
    private Button wikipediaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstBand = (EditText) findViewById(R.id.firstBand);
        secondBand = (EditText) findViewById(R.id.secondBand);
        findBand = (Button) findViewById(R.id.findBand);
        resultBand = (TextView) findViewById(R.id.resultBand);
        youtubeBtn = (Button) findViewById(R.id.youtubeBtn);
        wikipediaBtn = (Button) findViewById(R.id.wikipediaBtn);

        findBand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSimilarArtists();
            }
        });
    }

    private void getSimilarArtists() {
        final String BASE_URL = "https://tastedive.com/api/similar";
        final String query = "?q=";
        final String first_band = firstBand.getText().toString();
        final String and1 = "%2C+";
        final String second_band = secondBand.getText().toString();
        final String and2 = "&";
        final String key = "k=";
        final String api_key = "288613-SoundBud-D2CL1EP4";
        final String limit = "limit=100";
        final String type = "type=music&info=1";

        final String full_url = BASE_URL + query + first_band + and1 + second_band + and2 + key + api_key + and2 + limit + and2 + type;
        System.out.print(full_url);

        RequestQueue queue = Volley.newRequestQueue(this);

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, full_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject similar = response.getJSONObject("Similar");
                            JSONArray results = similar.getJSONArray("Results");

                            ArrayList<Music> artistsList = new Gson().fromJson(results.toString(), new TypeToken<List<Music>>() {}.getType());
                            Random rand = new Random();
                            int value = rand.nextInt(100);

                            String suggestion = artistsList.get(value).getName();
                            resultBand.setText(suggestion);

                            final String yVideoID = artistsList.get(value).getyID();
                            final String wikiURL = artistsList.get(value).getwUrl();

                            youtubeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    youtubeAppIntent(yVideoID);
                                }
                            });

                            wikipediaBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    wikipediaArticleIntent(wikiURL);
                                }
                            });

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

        queue.add(jsObjRequest);
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

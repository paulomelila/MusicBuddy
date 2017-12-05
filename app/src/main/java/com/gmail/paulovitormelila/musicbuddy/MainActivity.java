package com.gmail.paulovitormelila.musicbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    private EditText mFirstArtist;
    private EditText mSecondArtist;
    private EditText mThirdArtist;
    private ImageButton mFindSimilar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirstArtist = findViewById(R.id.firstBand);
        mSecondArtist = findViewById(R.id.secondBand);
        mThirdArtist = findViewById(R.id.thirdBand);
        mFindSimilar = findViewById(R.id.findBand);

        mFindSimilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if     (mFirstArtist.getText().toString().trim().length() <= 0 &&
                        mSecondArtist.getText().toString().trim().length() <= 0 &&
                        mThirdArtist.getText().toString().trim().length() <= 0) {
                    Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                } else getSimilarArtists();
            }
        });
    }

    // method to create a request for the API
    private void getSimilarArtists() {
        final String base_url = "https://tastedive.com/api/similar";
        final String query = "?q=" + mFirstArtist.getText().toString() + "%2C+" + mSecondArtist.getText().toString() + "%2C+" + mThirdArtist.getText().toString() + "&";
        final String key = "k=288613-SoundBud-D2CL1EP4&";
        final String limit = "limit=100&";
        final String type = "type=music&";
        final String info = "info=1";

        final String full_url = base_url + query + key + limit + type + info;
        System.out.println(full_url);

        RequestQueue queue = Volley.newRequestQueue(this);

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, full_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject similar = response.getJSONObject("Similar");
                            JSONArray results = similar.getJSONArray("Results");

                            // Creating an ArrayList with all the artists and sending as an extra to the SimilarArtistsActivity
                            ArrayList<Music> artistsList = new Gson().fromJson(results.toString(), new TypeToken<List<Music>>() {}.getType());
                            Intent intent = new Intent(MainActivity.this, SimilarArtistsActivity.class);
                            intent.putExtra("ArtistsList", artistsList);
                            startActivity(intent);

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
}
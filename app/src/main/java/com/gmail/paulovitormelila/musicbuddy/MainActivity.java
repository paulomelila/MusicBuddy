package com.gmail.paulovitormelila.musicbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private EditText firstBand;
    private EditText secondBand;
    private EditText thirdBand;
    private Button findBand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstBand = (EditText) findViewById(R.id.firstBand);
        secondBand = (EditText) findViewById(R.id.secondBand);
        thirdBand = (EditText) findViewById(R.id.thirdBand);
        findBand = (Button) findViewById(R.id.findBand);

        findBand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSimilarArtists();
            }
        });
    }

    private void getSimilarArtists() {
        final String base_url = "https://tastedive.com/api/similar";
        final String query = "?q=" + firstBand.getText().toString() + "%2C+" + secondBand.getText().toString() + "%2C+" + thirdBand.getText().toString() + "&";
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
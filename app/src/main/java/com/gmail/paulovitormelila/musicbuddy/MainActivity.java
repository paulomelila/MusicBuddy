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

        instantiateWidgets();
        onClickListeners();
    }

    private void getSimilarArtists() {
        final String base_url = getString(R.string.base_url);
        final String query = mFirstArtist.getText().toString() + "%2C+" + mSecondArtist.getText().toString() + "%2C+" + mThirdArtist.getText().toString() + "&";
        final String key = getString(R.string.api_key);
        final String limit = getString(R.string.limit);
        final String type = getString(R.string.type);
        final String info = getString(R.string.info);

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
                        error.printStackTrace();
                    }
                });

        queue.add(jsObjRequest);
    }

    private void instantiateWidgets() {
        mFirstArtist = findViewById(R.id.firstBand);
        mSecondArtist = findViewById(R.id.secondBand);
        mThirdArtist = findViewById(R.id.thirdBand);
        mFindSimilar = findViewById(R.id.findBand);
    }

    private boolean areFieldsEmpty() {
        return mFirstArtist.getText().toString().trim().length() <= 0 &&
                mSecondArtist.getText().toString().trim().length() <= 0 &&
                mThirdArtist.getText().toString().trim().length() <= 0;
    }

    private void displayToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void onClickListeners() {
        mFindSimilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areFieldsEmpty()) { displayToast(getString(R.string.error));}
                else getSimilarArtists();
            }
        });
    }
}
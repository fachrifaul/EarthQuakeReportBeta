package com.hidayatasep.makersinstitute.earthquakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private static final String USGS_REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=4&limit=30";

    //adapter
    EarthQuakeAdapter adapter;

    //list eartquake
    ListView earthquakeListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //membuat data dummy dari eartquake
        ArrayList<EarthQuake> earthQuakes = new ArrayList<EarthQuake>();

        //listview
        earthquakeListView = (ListView) findViewById(R.id.list);

        //membuat adapter
        adapter = new EarthQuakeAdapter(this,earthQuakes);

        //set Adapter
        earthquakeListView.setAdapter(adapter);

        //set on item click listener
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //mendapatkan data eratquke
                EarthQuake currentEarthQuake = adapter.getItem(position);

                //convert String url menjadi URI object
                Uri earthQuakeUri = Uri.parse(currentEarthQuake.getUrl());

                //create new intent untuk membuka halaman website dari eartquake
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,earthQuakeUri);

                //mengirim intent untuk membuka website
                startActivity(websiteIntent);
            }
        });

        new DownloadTaskEartQuake().execute();

    }

    private class DownloadTaskEartQuake extends AsyncTask<Void,Void,ArrayList<EarthQuake>>{


        @Override
        protected ArrayList<EarthQuake> doInBackground(Void... voids) {

            //set up URL
            URL url = QueryUtils.createURL(USGS_REQUEST_URL);

            //membuat http request ke URL dan menerima response JSON
            String jsonResponse = null;

            jsonResponse = QueryUtils.makeHttoRequest(url);

            // convert respone dalam bentuk jason menjadi array list
            ArrayList<EarthQuake> earthQuakes = QueryUtils.extraEartquake(jsonResponse);

            //return list
            return earthQuakes;

        }

        @Override
        protected void onPostExecute(ArrayList<EarthQuake> earthQuakes) {
            super.onPostExecute(earthQuakes);
            for(int i = 0;i < earthQuakes.size(); i++){
                //add eartquake to list
                adapter.add(earthQuakes.get(i));
            }

        }
    }

}

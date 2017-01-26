package com.hidayatasep.makersinstitute.earthquakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    private static final String USGS_REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=4&limit=30";

    //adapter
    EarthQuakeAdapter adapter;

    //list eartquake
    ListView earthquakeListView;

    //view for no data
    TextView emptyEartquake;

    //view for no connecton
    TextView noInternetConnectionLabel;
    Button refreshButton;

    //loading view
    ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyEartquake = (TextView) findViewById(R.id.empty_view);

        refreshButton = (Button) findViewById(R.id.refreshButton);
        noInternetConnectionLabel = (TextView) findViewById(R.id.noInternetConnectionLabel);

        loadingProgress = (ProgressBar) findViewById(R.id.loadingIndicator);

        //membuat data dummy dari eartquake
        ArrayList<EarthQuake> earthQuakes = new ArrayList<EarthQuake>();

        //listview
        earthquakeListView = (ListView) findViewById(R.id.list);

        //membuat adapter
        adapter = new EarthQuakeAdapter(this, earthQuakes);

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
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthQuakeUri);

                //mengirim intent untuk membuka website
                startActivity(websiteIntent);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshData();
            }
        });

    }

    //show progress bar
    public void showLoadingIndicator() {
        if (loadingProgress.getVisibility() == View.INVISIBLE) {
            //show progress bar
            //and hide another view
            loadingProgress.setVisibility(View.VISIBLE);
            earthquakeListView.setVisibility(View.INVISIBLE);
            emptyEartquake.setVisibility(View.INVISIBLE);
            noInternetConnectionLabel.setVisibility(View.INVISIBLE);
            refreshButton.setVisibility(View.INVISIBLE);
            refreshButton.setEnabled(false);
        }
    }

    //method to show eartquake list and hide progress bar
    public void showList() {
        loadingProgress.setVisibility(View.INVISIBLE);
        earthquakeListView.setVisibility(View.VISIBLE);
    }

    //method to show eartquake empty textview and hide progress bar
    public void showEmptyData() {
        loadingProgress.setVisibility(View.INVISIBLE);
        emptyEartquake.setVisibility(View.VISIBLE);
    }

    //method to show no internet connection vie  and hide progress bar
    public void showNoInternetView() {
        loadingProgress.setVisibility(View.INVISIBLE);
        noInternetConnectionLabel.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
        refreshButton.setEnabled(true);
    }

    //get link to get data from usgs
    //use minimal magnitude from preferences
    private String getLinkEartQuake() {
        //use min magnitude from preferences
        SharedPreferences sharedPrefences = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefences.getString(
                getString(R.string.setting_min_magnitude_key),
                getString(R.string.setting_min_magnitude_default));
        //return url
        String url = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=" + minMagnitude + "&limit=30";
        return url;
    }

    private class DownloadTaskEartQuake extends AsyncTask<String, Void, ArrayList<EarthQuake>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //clear data in list
            adapter.clear();
        }

        @Override
        protected ArrayList<EarthQuake> doInBackground(String... urlString) {

            //set up URL
            URL url = QueryUtils.createURL(urlString[0]);
//
//            URL url = QueryUtils.createURL(USGS_REQUEST_URL);

            //membuat http request ke URL dan menerima response JSON
            String jsonResponse = QueryUtils.makeHttoRequest(url);

            // convert respone dalam bentuk jason menjadi array list
            ArrayList<EarthQuake> earthQuakes = QueryUtils.extraEartquake(jsonResponse);

            //return list
            return earthQuakes;

        }

        @Override
        protected void onPostExecute(ArrayList<EarthQuake> earthQuakes) {
            super.onPostExecute(earthQuakes);
//            for (int i = 0; i < earthQuakes.size(); i++) {
//                //add eartquake to list
//                adapter.add(earthQuakes.get(i));
//            }

            //add eartquake to list view
            if (earthQuakes.isEmpty()) {
                //if no data show empty text view
                showEmptyData();
            } else {
                showList();
                for (int i = 0; i < earthQuakes.size(); i++) {
                    //add eartquake to list
                    adapter.add(earthQuakes.get(i));
                }
            }

        }
    }

    //method to get data from server
    private void refreshData() {
        showLoadingIndicator();
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            String url = getLinkEartQuake();
            new DownloadTaskEartQuake().execute(url);
        } else {
            showNoInternetView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //refresh data
        refreshData();
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent intent = new Intent(this, PrefsActivity.class);
            startActivity(intent);
            return true;
        } else {
            refreshData();
        }
        return super.onOptionsItemSelected(item);
    }
}

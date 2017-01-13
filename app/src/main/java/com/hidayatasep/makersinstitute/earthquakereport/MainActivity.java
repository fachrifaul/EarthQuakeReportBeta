package com.hidayatasep.makersinstitute.earthquakereport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //membuat data dummy dari eartquake
        ArrayList<EarthQuake> earthQuakes = QueryUtils.extraEartquake();

        //listview
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        //membuat adapter
        EarthQuakeAdapter adapter = new EarthQuakeAdapter(this,earthQuakes);

        //set Adapter
        earthquakeListView.setAdapter(adapter);


    }
}

package com.hidayatasep.makersinstitute.earthquakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by hidayatasep43 on 1/12/2017.
 */

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {

    private static final String LOCATION_SEPARATOR = " of ";
    private static final String TAG = EarthQuakeAdapter.class.getSimpleName();

    public EarthQuakeAdapter(Context context, List<EarthQuake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //check jika ada view untuk item dari list (convert view)
        //jika tidak ada, maka membuat baru

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earth_quake_list_item,parent,false);
        }

        //temukan data eartquake sesuai posisi
        EarthQuake currentEartquake = getItem(position);

        //TextView magnitude
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        //format nilai dari magnitude sehingga hanya menampilkan satu angka dibelakang koma
        String formatMagnitude = formatMagnitude(currentEartquake.getMagnitude());
        //display magniitude
        magnitudeView.setText(formatMagnitude);

        //get background color of magnitude
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        //menemukan backgroud warna yang cocok untuk magnitude
        int magnitudeColor = getMagnitudeColor(currentEartquake.getMagnitude());
        //set background color dari magnitude
        magnitudeCircle.setColor(magnitudeColor);

        //mendapatkan lokasi sebenarnya dari eartquake
        String originalLocation = currentEartquake.getLocation();

        //memisahkan origanal lokasi menajdi primary lokasi dan loction offset
        String primaryLocation;
        String locationOffset;

        //check jika original lokasi mengandung kata "of"
        if(originalLocation.contains(LOCATION_SEPARATOR)){
            //jika mengandung kata of, maka akan dipisahkan
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR ;
            primaryLocation = parts[1];
        }else{
            //jika tidak ada kata of, maka ditambahkan manual
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        //menampilkan lokasi primary
        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.locationPrimary);
        primaryLocationView.setText(primaryLocation);

        //menampilkan location offset
        TextView offsetLocationView = (TextView) listItemView.findViewById(R.id.locationOffset);
        offsetLocationView.setText(locationOffset);

        //membuat object Date dari waktu eartquake dalam satuan milllisecond
        Date dateObject = new Date(currentEartquake.getTImeMillisecond());

        //menampilkan tanggal
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        //format date
        String formatedDate = formatDate(dateObject);
        dateView.setText(formatedDate);

        //menampilkan waktu
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        //format time
        String formatedTime = formatTime(dateObject);
        timeView.setText(formatedTime);

        return listItemView;
    }

    //return format tanggal ("Mar 3, 1995") dari object date
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(dateObject);
    }

    //return format jam (""4:30 PM") dari object date
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    //method untuk menentukan backgroud warna yang cocok sesuai ukuran magnitude
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        Log.e(TAG,""+ magnitudeFloor);
        //menentukan warna
        switch (magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10;
                break;
        }

        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    //method untuk menformat nilai magnitude
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }
}

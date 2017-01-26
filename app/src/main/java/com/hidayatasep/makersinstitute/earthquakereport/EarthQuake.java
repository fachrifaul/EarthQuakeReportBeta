package com.hidayatasep.makersinstitute.earthquakereport;

/**
 * Created by hidayatasep43 on 1/12/2017.
 */

public class EarthQuake {

    //magnitude
    private double mMagnitude;

    //location
    private String mLocation;

    //time
    private long mTImeMillisecond;

    //website url
    private String mUrl;

    //contuctor
    public EarthQuake(double magnitude, String location, long TImeMillisecond, String url) {
        mMagnitude = magnitude;
        mLocation = location;
        mTImeMillisecond = TImeMillisecond;
        mUrl = url;
    }

    public EarthQuake(double magnitude, String location, long TImeMillisecond) {
        mMagnitude = magnitude;
        mLocation = location;
        mTImeMillisecond = TImeMillisecond;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public void setMagnitude(double magnitude) {
        mMagnitude = magnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public long getTImeMillisecond() {
        return mTImeMillisecond;
    }

    public void setTImeMillisecond(long TImeMillisecond) {
        mTImeMillisecond = TImeMillisecond;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}

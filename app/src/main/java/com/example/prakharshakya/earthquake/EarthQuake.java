package com.example.prakharshakya.earthquake;

public class EarthQuake {
    private double mMagnitude;
    private String mPlace;
    private long mTimeInMillisec;
    private String mUrl;

    public EarthQuake(double Magnitude, String Place, long TimeInMillisec ,String Url) {
        mMagnitude = Magnitude;
        mPlace = Place;
        mTimeInMillisec=TimeInMillisec;
        mUrl=Url;
    }

    public double getMagnitude() {
        return mMagnitude;
    }
    public String getPlace()
    {
        return mPlace;
    }
    public long getTimeInMillisec(){return mTimeInMillisec;}
    public String getUrl(){return mUrl;}
}



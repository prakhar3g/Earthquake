package com.example.prakharshakya.earthquake;

import android.content.Context;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class EarthQuakeLoader extends AsyncTaskLoader<List<EarthQuake>> {

    private static final String LOG_TAG = EarthQuakeLoader.class.getName();
    private String mUrl;

   public EarthQuakeLoader(Context context,String url)
   {
       super(context);
       mUrl=url;
   }

    @Override
    protected void onForceLoad() {
        Log.i(LOG_TAG,"onForceLoad Method called..");


        forceLoad();
    }


    @Override
    public List<EarthQuake> loadInBackground() {
        Log.i(LOG_TAG,"onloadInBackground called..");

        if(mUrl==null)
        {
            return null;
        }
        List<EarthQuake> earthQuakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthQuakes;
    }
}

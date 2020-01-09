package com.example.prakharshakya.earthquake;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Menu;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import android.net.NetworkInfo;
import android.widget.TextView;


public class Earthquake_MainActivity extends AppCompatActivity  {

    public static final String LOG_TAG = Earthquake_MainActivity.class.getName();

    private TextView mEmptyTextView;


    private static final String USGS_REQUEST_URL=" https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&starttime=2018-01-01";

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private EarthQuakeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake__main);

        Log.i(LOG_TAG,"onCreate Method called..");

        ListView earthQuakeListView = (ListView) findViewById(R.id.list_view);

        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        earthQuakeListView.setEmptyView(mEmptyTextView);

        mAdapter = new EarthQuakeAdapter(this,new ArrayList<EarthQuake>());

        earthQuakeListView.setAdapter(mAdapter);


      earthQuakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              EarthQuake currentEarthQuake = mAdapter.getItem(position);
              Uri earthquakeUri = Uri.parse(currentEarthQuake.getUrl());

              Intent i = new Intent(Intent.ACTION_VIEW,earthquakeUri);
              startActivity(i);
          }
      });

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ntwrkInfo = connMgr.getActiveNetworkInfo();

        if(ntwrkInfo!=null && ntwrkInfo.isConnected())
        {
            EarthQuakeAsyncTask task = new EarthQuakeAsyncTask();

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            String minMagnitude = sharedPrefs.getString(
                    getString(R.string.settings_min_magnitude_key),
                    getString(R.string.settings_min_magnitude_default));

            String itemNumber = sharedPrefs.getString(
                    getString(R.string.settings_item_number_key),
                    getString(R.string.settings_item_number_default));

            String maxMagnitude = sharedPrefs.getString(
                    getString(R.string.settings_max_magnitude_key),
                    getString(R.string.settings_max_magnitude_default));

            String orderBy = sharedPrefs.getString(
                    getString(R.string.settings_order_by_key),
                    getString(R.string.settings_order_by_default)
            );

            Uri baseUri = Uri.parse(USGS_REQUEST_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            uriBuilder.appendQueryParameter("format", "geojson");
            uriBuilder.appendQueryParameter("limit", itemNumber);
            uriBuilder.appendQueryParameter("minmag", minMagnitude);
            uriBuilder.appendQueryParameter("maxmag",maxMagnitude);
            uriBuilder.appendQueryParameter("orderby", orderBy);
            uriBuilder.appendQueryParameter("starttime","2018-01-01");

            task.execute(uriBuilder.toString());
           // task.execute(USGS_REQUEST_URL);

        }
        else {
            View loadindIndicator = findViewById(R.id.progress_bar);
            loadindIndicator.setVisibility(View.GONE);
            mEmptyTextView.setText(R.string.no_internet_connection);
        }


    }
    private class EarthQuakeAsyncTask extends AsyncTask<String, Void,List<EarthQuake>>
    {

        @Override
        protected List<EarthQuake> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<EarthQuake> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<EarthQuake> data) {

            View loading_indicator = findViewById(R.id.progress_bar);
            loading_indicator.setVisibility(View.GONE);

           mEmptyTextView.setText(R.string.no_earthquakes);
            mAdapter.clear();

            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

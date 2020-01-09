package com.example.prakharshakya.earthquake;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.prakharshakya.earthquake.Earthquake_MainActivity.LOG_TAG;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils()
    { }

   public static List<EarthQuake> fetchEarthquakeData(String requestUrl)
   {
       URL url = createUrl(requestUrl);
       String jsonResponse = null;

       try{
           Thread.sleep(2000);
       }
       catch (InterruptedException e)
       {
           e.printStackTrace();
       }
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e)
        {
            Log.e(LOG_TAG, "Problem making the Http request.",e);
        }

        List<EarthQuake> earthQuakes = extractFeatureFromJson(jsonResponse);
        return earthQuakes;
   }

    private static List<EarthQuake> extractFeatureFromJson(String earthQuakeJSON) {
        if (TextUtils.isEmpty(earthQuakeJSON))
        {
            return null;
        }

        List<EarthQuake> earthQuakes = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(earthQuakeJSON);
            JSONArray earthQuakeArray = baseJsonResponse.getJSONArray("features");
             for (int i =0; i<earthQuakeArray.length();i++)
             {
                 JSONObject currentEarthQuake = earthQuakeArray.getJSONObject(i);
                 JSONObject properties = currentEarthQuake.getJSONObject("properties");
                 double magnitude = properties.getDouble("mag");
                 String location = properties.getString("place");
                 long time = properties.getLong("time");
                 String url = properties.getString("url");
                 EarthQuake earthquake =new  EarthQuake(magnitude,location,time,url);
                 earthQuakes.add(earthquake);
             }
        }catch (JSONException e)
        {
            Log.e("QueryUtils","Problem parsing the earthquake JSON results",e);
        }
        return earthQuakes;

    }

    private static URL createUrl(String stringUrl)
   {
       URL url = null;
       try {
           url = new URL(stringUrl);
       } catch (MalformedURLException e)
       {
           Log.e(LOG_TAG, "Problem building the URL",e);
       }
       return url;
   }

   private static String makeHttpRequest(URL url) throws IOException
   {
       String jsonresponse = "";
       if (url==null)
       {
           return jsonresponse;
       }
       HttpURLConnection urlConnection =null;
       InputStream inputStream = null;
       try {
           urlConnection = (HttpURLConnection) url.openConnection();
           urlConnection.setReadTimeout(10000);
           urlConnection.setConnectTimeout(15000);
           urlConnection.setRequestMethod("GET");
           urlConnection.connect();

           if (urlConnection.getResponseCode()==200)
           {
               inputStream = urlConnection.getInputStream();
               jsonresponse = readFromStream(inputStream);
           }
           else {
               Log.e(LOG_TAG,"Error response code:"+urlConnection.getResponseCode());
           }
       } catch (IOException e)
       {
           Log.e(LOG_TAG,"Problem retrieving the earthquake JSON results.",e);
       }finally {
           if (urlConnection != null)
           {
               urlConnection.disconnect();
           }
           if (inputStream!=null)
           {
               inputStream.close();
           }
       }
       return jsonresponse;
   }

   private static String readFromStream(InputStream inputStream) throws IOException
   {
       StringBuilder output = new StringBuilder();
       if (inputStream != null)
       {
           InputStreamReader inputStreamReader = new InputStreamReader(inputStream,Charset.forName("UTF-8"));
           BufferedReader reader = new BufferedReader(inputStreamReader);
           String line = reader.readLine();
           while (line != null)
           {
               output.append(line);
               line = reader.readLine();
           }

       }
       return output.toString();
   }
}
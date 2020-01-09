package com.example.prakharshakya.earthquake;

import android.app.Activity;
import java.text.DecimalFormat;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.graphics.drawable.GradientDrawable;


public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {

    private String primaryLocation;
    private String locationOffset;
    private static final String LOCATION_SEPARATOR = "of";

 public EarthQuakeAdapter(Activity context, ArrayList<EarthQuake> earthQuake)
 {
     super(context,0,earthQuake);
 }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView==null)
        {
            listItemView=LayoutInflater.from(getContext()).inflate(R.layout.eathquake_list,parent,false);
        }

        EarthQuake currentEarthQuake= getItem(position);


        // Magnitude textview
        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude_view);
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        String formattedMagnitude = magnitudeFormat.format(currentEarthQuake.getMagnitude());
        magnitudeTextView.setText(formattedMagnitude);


        //magnitude circle color
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthQuake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);


        // Location textview
        String originalLocation = currentEarthQuake.getPlace();
        if(originalLocation.contains(LOCATION_SEPARATOR))
        {
            String parts[] = originalLocation.split("of");
            locationOffset = parts[0]+"of";
            primaryLocation = parts[1];
        }
        else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        TextView locationOffsetTextView = (TextView) listItemView.findViewById(R.id.location_offset);
        locationOffsetTextView.setText(locationOffset);

        TextView primaryLocationTextView = (TextView) listItemView.findViewById(R.id.primary_location);
        primaryLocationTextView.setText(primaryLocation);


        Date dateObject = new Date(currentEarthQuake.getTimeInMillisec());

        //date textview
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_view);
        SimpleDateFormat formatDate = new SimpleDateFormat("EEE, MMM d, ''yy");
        String formattedDate = formatDate.format(dateObject);
        dateTextView.setText(formattedDate);

        //time textview
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_view);
        SimpleDateFormat formattime = new SimpleDateFormat("h:mm a");
        String formattedTime = formattime.format(dateObject);
        timeTextView.setText(formattedTime);

        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {

     int magnitudeColorResourceId;
     int magnitudeFloor = (int) Math.floor(magnitude);
     switch (magnitudeFloor)
     {
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
             magnitudeColorResourceId= R.color.magnitude8;
             break;
         case 9:
             magnitudeColorResourceId = R.color.magnitude9;
             break;
         default:
             magnitudeColorResourceId = R.color.magnitude10plus;
             break;

     }
     return ContextCompat.getColor(getContext(),magnitudeColorResourceId);
    }

}

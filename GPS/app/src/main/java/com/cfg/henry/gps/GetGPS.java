package com.cfg.henry.gps;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class GetGPS extends AppCompatActivity
implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener  {


    protected static final String TAG = "GetGPS";
    protected GoogleApiClient mGoogleApiClient;

    protected Location mLastLocation;
    protected Location mCurrentLocation;

    protected String mLatitudeLabel;
    protected String mLongitudeLabel;

    protected LocationRequest mLocationRequest;


    // Runs on creation of the GetGPS action. Initialises Google API client and sets up the
    // frequency of location requests.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        createLocationRequest();
    }

    // Runs when the GetGPS action starts. Makes sure that the Google API client connects properly.
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    // Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //Runs when a GoogleApiClient object successfully connects. Finds the last known location, and
    // starts the phone continually updating its location information.
    @Override
    public void onConnected(Bundle connectionHint) {
        getLastLocation();
        startLocationUpdates();
    }

    // Finds the last known location of the phone. Note: not the same as current location, but good
    // enough as a starting point!
    public void getLastLocation(){
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mCurrentLocation = mLastLocation;
        setLocationLabels();
        sendData();
    }

    // Converts the current location into use-able strings. Changes mCurrentLocation into
    // mLatitudeLabel and mLongitudeLabel
    public void setLocationLabels(){
        if (mCurrentLocation != null) {
            mLatitudeLabel = String.valueOf(mCurrentLocation.getLatitude());
            mLongitudeLabel = String.valueOf(mCurrentLocation.getLongitude());
        } else {
            //System.out.println("OOPS");
        }
    }

    // Output reasonable error messages
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    // How often should the app update the GPS signal?
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    // Send data to the server every time the location changes.
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        setLocationLabels();
        sendData();
    }

    // Do the actual sending of the data
    protected void sendData(){
        Thread myThread = new Thread(runnable);
        myThread.start();
    }


    // Creates a new thread to deal with the web-requests. Sends get requests to the server, with
    // the location information embedded into the URL.
    public Runnable runnable = new Runnable() {
        public void run() {
            HttpURLConnection urlConnection = null;
            try {

                String urlstr = "http://ec2-54-78-37-212.eu-west-1.compute.amazonaws.com:8000/runnerdb/send_location/?runner_id=0&x=" +mLongitudeLabel +"&y=" + mLatitudeLabel;
                URL url = new URL(urlstr);
                //URL url = new URL("http://www.google.com");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(false);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();



            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
             if(urlConnection!=null);
             urlConnection.disconnect();
            }
        }
    };

}



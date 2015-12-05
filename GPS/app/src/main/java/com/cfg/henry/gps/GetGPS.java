package com.cfg.henry.gps;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class GetGPS extends AppCompatActivity
implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener  {


    protected static final String TAG = "GetGPS";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    protected Location mLastLocation;
    protected Location mCurrentLocation;

    protected String mLatitudeLabel;
    protected String mLongitudeLabel;

    protected LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        createLocationRequest();
        //conn = (HttpURLConnection) url.openConnection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        getLastLocation();
        startLocationUpdates();
    }

    public void getLastLocation(){
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mCurrentLocation = mLastLocation;
        setLocationLabels();
        sendData();
    }

    public void setLocationLabels(){
        if (mCurrentLocation != null) {
            mLatitudeLabel = String.valueOf(mCurrentLocation.getLatitude());
            mLongitudeLabel = String.valueOf(mCurrentLocation.getLongitude());
        } else {
            //System.out.println("OOPS");
        }
    }


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

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        setLocationLabels();
    }

    protected void sendData(){
        Thread myThread = new Thread(runnable);
        myThread.start();
    }

    public Runnable runnable = new Runnable() {
        public void run() {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://ec2-54-78-37-212.eu-west-1.compute.amazonaws.com:8000/runnerdb/send_donation/");
                //URL url = new URL("http://www.google.com");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                //Create JSONObject here
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("id", String.valueOf(0));
                jsonParam.put("x", mLongitudeLabel);
                jsonParam.put("y", mLatitudeLabel);

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonParam.toString());
                out.close();


                int hello = -1;
                hello = urlConnection.getResponseCode();



            } catch (IOException e) {
                e.printStackTrace();
            } catch(JSONException e) {
            // TODO Auto-generated catch block
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



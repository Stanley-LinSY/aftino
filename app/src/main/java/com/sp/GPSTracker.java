package com.sp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentResultListener;

public class GPSTracker extends Service implements LocationListener {
    private Context mContext = null;
    boolean isGPSEnabled = false;  //flag for GPS status
    boolean isNetworkEnabled = false;  //flag for network status
    boolean canGetLocation = false;  //flag for GPS status

    Location location;  //location
    double latitude;  //latitude
    double longitude;  //longitude

    //the min distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;  //10 meters
    //the min time between Updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;  //1 minutes
    //declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker() { checkGPSPermissions(); }

    public GPSTracker (Context context) {
        this.mContext = context;
        checkGPSPermissons();
    }

    private void checkGPSPermissons() {
    }

    public Location getLocation() {
        this.canGetLocation = false;
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled){
                //no network provider is endabled
                //prompt user to enable location services
                showEnableLocationAlert();
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    //Permission granted
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                //if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null){
                        locationManager.requestLocationUpdates(
                                locationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (SecurityException e){
            e.printStackTrace();
        }
        return location;
    }

    //function to get latitude
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    //function to get longitude
    public double getLongitude(){
        if (location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    /*
    stop using GPS listener
    Calling this function will stop using GPS in your app
     */

    public void stopUsingGPS(){
        if (locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /*
    function to check GPS/wifi enabled
    @return Boolean
     */

    public boolean canGetLocation(){
        checkGPSPermissions();
        return canGetLocation;
    }

    //check for location permission

    public void checkGPSPermissions(){
        int permissionState1 = ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionState2 = ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionState1 == PackageManager.PERMISSION_GRANTED &&
                permissionState2 == PackageManager.PERMISSION_GRANTED){
            //permission granted, get GPS location
            getLocation();
        }
        else {
            //prompt user to enable location permission
            showEnablePermissionAlert();
        }
    }

    /*
    Function to show settings alert dialog On pressing Settings button will launch Settings Options
     */
    public void showEnablePermissionAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        //setting Dialog Title
        alertDialog.setTitle("Location Permission Settings");
        //setting Dialog Message
        alertDialog.setMessage("Restaurant List Location Permission is not enabled. " +
                "Do you want to go to settings menu?");
        //on pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                //go to Application Setting
                intent.setAction(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",
                        BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        //on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        //showing Alert Message
        alertDialog.show();
    }

    public void showEnableLocationAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        //setting Dialog Title
        alertDialogBuilder.setTitle("Location Service Settings");
        alertDialogBuilder.setMessage("Location service is disable in your device. " +
                "Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Go to Setting Page To Enable Location Service",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id) {
                                //go to location service setting
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                mContext.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(Location location) { getLocation(); }

    @Override
    public void onProviderDisabled(String provider){
    }

    @Override
    public void onProviderEnabled(String provider){
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){
    }

    @Override
    public IBinder onBind (Intent intent){
        //TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
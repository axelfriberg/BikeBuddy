package com.axelfriberg.bikebuddy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v4.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.common.api.Status;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 150;
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 151;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private TextView tv;
    private boolean enableUpdates;
    private boolean enableVibration;
    private Button button1;
    private Button button2;
    private int counter = 0;

    private double currentLatitude;
    private double currentLongitude;
    private double markerLatitude;
    private double markerLongitude;


    // för att få sin egen position
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private Marker marker;

    //Save data to shared preferences
    private SharedPreferences activityPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_48dp);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapOld);
        mMapFragment.getMapAsync(this);

        button1 = (Button) findViewById(R.id.mark_button);
        button2 = (Button) findViewById(R.id.remove_button);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        //set text textview
        tv = (TextView) findViewById(R.id.distance);

        setTitle(R.string.title_activity_maps);

        // egen position
        // Create the LocationRequest object

        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(0);// 1 seconds, in milliseconds
        mLocationRequest.setFastestInterval(0);// 1 second, in milliseconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // google client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();



        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* toolbar */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        );

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //setting up selected item listener
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        //Check to see which item was being clicked and perform appropriate action

                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                        switch (menuItem.getItemId()) {
                            case R.id.navigation_item_map:
                                fragmentTransaction
                                        .show(mMapFragment)
                                        .addToBackStack(null).commit();
                                setTitle(R.string.title_activity_maps);
                                return true;
                            case R.id.navigation_item_nfc:
                                NFCFragment nfcFragment = new NFCFragment();
                                fragmentTransaction
                                        .hide(mMapFragment)
                                        .replace(R.id.frame, nfcFragment)
                                        .addToBackStack(null)
                                        .commit();
                                return true;
                            case R.id.navigation_item_lock:
                                LockFragment lockFragment = new LockFragment();
                                fragmentTransaction.hide(mMapFragment)
                                        .replace(R.id.frame, lockFragment)
                                        .addToBackStack(null)
                                        .commit();
                                return true;
                            case R.id.navigation_item_statistics:
                                StatisticFragment statisticFragment = new StatisticFragment();
                                fragmentTransaction.hide(mMapFragment)
                                        .replace(R.id.frame, statisticFragment)
                                        .addToBackStack(null)
                                        .commit();
                                return true;
                            case R.id.navigation_item_notification:
                                NotificationFragment notificationFragment = new NotificationFragment();
                                fragmentTransaction.replace(R.id.frame,notificationFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                return true;
                            default:
                                Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                    }
                });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.navigation_item_map) {
            Toast.makeText(MainActivity.this, " Nav", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            FragmentManager fm = getSupportFragmentManager();
            int backCount = fm.getBackStackEntryCount();
            if (backCount > 0)
                fm.popBackStack();
            else
                super.onBackPressed();
        }
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // Should we show an explanation?

           /* if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {*/

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.


            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_COARSE_LOCATION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }
        mMap.setMyLocationEnabled(true);

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }
    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped");
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed");
        }

    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        counter ++;


        // marker for current position
        if (enableUpdates) {
            if (marker == null) {
            } else {
                marker.remove();
            }

            markerLatitude = currentLatitude;
            markerLongitude = currentLongitude;
            LatLng latLng = new LatLng(markerLatitude, markerLongitude);
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title("Your bike");
            marker = mMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            Log.i(TAG, "put marker");
            enableUpdates = false;
        }
        //display distance

        if (markerLatitude == 0 && markerLongitude == 0) {
            tv.setText("Bike is not parked");
            enableVibration = false;
        } else {
            tv.setText("Distance: " + distance(markerLatitude, markerLongitude, 0, 0) + " m");
            Log.v("Tag", "updates");
            enableVibration = true;
        }
    }

    public String distance(double lat, double lon, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat - currentLatitude);
        Double lonDistance = Math.toRadians(lon - currentLongitude);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(currentLatitude)) * Math.cos(Math.toRadians(lat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        double height = el1 - el2;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        distance = Math.sqrt(distance);
    if (enableVibration && counter == 5) {
         if (distance <= 1) {
             vibrate(100);
            } else if (distance <= 5) {
             vibrate(200);
            } else if (distance <= 10) {
              vibrate(300);
         } else if (distance <= 25) {
             vibrate(400);
            } else if (distance <= 50) {
             vibrate(500);
            } else {

            }
        counter = 0;
    }
        DecimalFormat df = new DecimalFormat("#.##");
        String distance2 = df.format(distance);
        return distance2;
    }

    public void vibrate(int ms) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, ms, ms, ms};
        v.vibrate(pattern, -1);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mark_button) {
            enableUpdates = true;
            enableVibration= true;
        } else if (v.getId() == R.id.remove_button) {
            if (marker == null) {
                Log.v("hej", "null markör" );
            } else {
                Log.v("hej", "null ej markör" );
                marker.remove();
                enableVibration= false;
                enableUpdates = false;
                tv.setText("Bike is not parked");
            }
            markerLatitude = 0;
            markerLongitude = 0;
        }
    }


}

package es.app.alexandercontreras.proyectocat.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import es.app.alexandercontreras.proyectocat.EjemploActivity;
import es.app.alexandercontreras.proyectocat.Places.LocationsContentProvider;
import es.app.alexandercontreras.proyectocat.Places.LocationsDB;
import es.app.alexandercontreras.proyectocat.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,GoogleMap.OnMarkerClickListener {
    private static final String LOG_TAG = "App";
    private static final String SERVICE_URL = "http://wssitiosudb.com/wssitios/Lugares/resultado/generar?filtro=tipo&tipo=1";
    protected GoogleMap map;
    MediaPlayer m;
    Marker ma;

    CoordinatorLayout coordinatorLayout;

    public MapsActivity(){
        hand = new Handler();
    }
    Handler hand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .cor);
        FloatingActionButton f =(FloatingActionButton) findViewById(R.id.k);
        f.setOnClickListener(this);

        m =MediaPlayer.create(this, R.raw.add);
        cargar();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Para que cargue el SQlite
        getSupportLoaderManager().initLoader(0, null, this);

        //locationmanager, encender gps si se esta apagado
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        setUpMapIfNeeded();

        map.setOnMarkerClickListener(this);

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng point) {
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("¿Desea agregar este marcador a sus favoritos?")
                        .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        guardar(point);
                                    }
                                }
                        )
                        .setNegativeButton("Cancelar", null)
                        .show();

            }
        });

        opcion();
    }


    @Override
    protected void onResume() {
        super.onResume();
        cargar();
        setUpMapIfNeeded();
    }

    @Override
    protected void onPause() {
        //setUpMapIfNeeded();
        //cargar();
        super.onPause();
    }

    private void cargar() {
        if (map == null) {
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (map != null) {
                cargarmciudad();
            }
        }
    }

    private void cargarmciudad() {

        new Thread(new Runnable() {
            public void run() {
                try {
                    recuperaragregar();
                } catch (IOException e) {
                    Log.v(LOG_TAG, "No carga ciudad", e);
                    return;
                }
            }
        }).start();
    }

    private void guardar (LatLng point){
        drawMarker(point);
        m.start();

        // Creating an instance of ContentValues
        ContentValues contentValues = new ContentValues();

        // Setting latitude in ContentValues
        contentValues.put(LocationsDB.FIELD_LAT, point.latitude );

        // Setting longitude in ContentValues
        contentValues.put(LocationsDB.FIELD_LNG, point.longitude);

        // Setting zoom in ContentValues
        contentValues.put(LocationsDB.FIELD_ZOOM, map.getCameraPosition().zoom);

        // Creating an instance of LocationInsertTask
        LocationInsertTask insertTask = new LocationInsertTask();

        // Storing the latitude, longitude and zoom level to SQLite database
        insertTask.execute(contentValues);

        Toast.makeText(getBaseContext(), "Marcador personal agregado", Toast.LENGTH_SHORT).show();

    }

    protected void recuperaragregar() throws IOException {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL(SERVICE_URL);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Read the JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error conexion con webservice", e);
            throw new IOException("Error conexion con webservice", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

      //Creacion de marcadaor a partir de JSON
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    marcadoresJSOn(json.toString());
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "No se carga JSON", e);
                }
            }
        });
    }

    void marcadoresJSOn(String json) throws JSONException {

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                map.addMarker(new MarkerOptions()
                        .title(jsonObj.getString("nombre"))
                        .snippet(Integer.toString(jsonObj.getInt("id")))

                        .position(new LatLng(
                                jsonObj.getDouble("lat"),
                                jsonObj.getDouble("lng")
                        ))

                );
            }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng magnac = new LatLng(13.6368565,-89.4985976);
        map.moveCamera(CameraUpdateFactory.newLatLng(magnac));
        map.getMaxZoomLevel();
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(8);
        map.animateCamera(zoom);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            }

            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }


    private void setUpMap() {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            Toast.makeText(MapsActivity.this, "Servicios otorgados!", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }
    }

}

    private void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        markerOptions.title("Guardo punto en:"+LocationsDB.FIELD_LAT+","+LocationsDB.FIELD_LNG);

        // Adding marker on the Google Map
        ma = map.addMarker(markerOptions);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.k:
                Intent in = new Intent(this,MainActivity.class);
                startActivity(in);
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker){

        String valor = marker.getSnippet();

        Toast.makeText(this,"Ejecutado " +valor,Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, EjemploActivity.class);

       i.putExtra("id",valor);

        startActivity(i);

        return true;
    }


    private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void>{
        @Override
        protected Void doInBackground(ContentValues... contentValues) {

            /** Setting up values to insert the clicked location into SQLite database */
            getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]);
            return null;
        }
    }

    private class LocationDeleteTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {

            /** Deleting all the locations stored in SQLite database */
            getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null);
            return null;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

        // Uri to the content provider LocationsContentProvider
        Uri uri = LocationsContentProvider.CONTENT_URI;

        // Fetches all the rows from locations table
        return new CursorLoader(this, uri, null, null, null, null);

    }



    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        int locationCount = 0;
        double lat=0;
        double lng=0;
        float zoom=0;

        // Numero de locaciones disponibles
        locationCount = arg1.getCount();

        // Mover el punto aasctual de primero a la tabla
        arg1.moveToFirst();

        for(int i=0;i<locationCount;i++){

            // Obtener latitud
            lat = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT));

            // Obtener longitude
            lng = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG));

            // Obtener zoom
            zoom = arg1.getFloat(arg1.getColumnIndex(LocationsDB.FIELD_ZOOM));

            // creación de una instancia de LatLng para trazar la ubicación en Google Maps
            LatLng location = new LatLng(lat, lng);

            // Dibujando el marcador
            drawMarker(location);

            //siguiente fila
            arg1.moveToNext();
        }

        if(locationCount>0){
            // Posicion de camara
            //map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));

            // Aplicando el zoom
           // map.animateCamera(CameraUpdateFactory.zoomTo(zoom));

        }
    }


    public void opcion() {
        CharSequence mapas[] = new CharSequence[] {"Mapa Normal", "Mapa Terreno", "Mapa Satelital", "Mapa Híbrido"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione el tipo de mapa:");
        builder.setItems(mapas, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case 2:
                        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 3:
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                }
            }
        });
        builder.show();


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



}

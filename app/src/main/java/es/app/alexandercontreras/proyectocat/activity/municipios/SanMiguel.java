package es.app.alexandercontreras.proyectocat.activity.municipios;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import es.app.alexandercontreras.proyectocat.R;

public class SanMiguel extends FragmentActivity implements OnMapReadyCallback {
    private static final String LOG_TAG = "ExampleApp";

    private static final String SERVICE_URL = "http://wssitiosudb.com/wssitios/Lugares/resultado/generar";

    protected GoogleMap map;
    Spinner spinner;

    public SanMiguel(){
        hand = new Handler();
    }
    Handler hand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapabuscar);

        cargar();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //locationmanager, encender gps si se esta apagado
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        setUpMapIfNeeded();

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


    protected void recuperaragregar() throws IOException {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {

            // Connect to the web service
            StringBuilder sb = new StringBuilder(SERVICE_URL);
            sb.append("?filtro=depar");
            sb.append("&depar=Miguel");
            URL url = new URL(sb.toString());
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
                    .snippet(jsonObj.getString("departamento"))
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
            Toast.makeText(SanMiguel.this, "Servicios otorgados!", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }
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



}

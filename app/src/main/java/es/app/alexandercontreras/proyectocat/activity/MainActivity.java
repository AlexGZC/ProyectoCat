package es.app.alexandercontreras.proyectocat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Timer;
import java.util.TimerTask;

import es.app.alexandercontreras.proyectocat.Places.PlacesAutoCompleteAdapter;
import es.app.alexandercontreras.proyectocat.R;

public class MainActivity extends AppCompatActivity {
    private int[] gallery = { R.drawable.uno, R.drawable.dos, R.drawable.tres };
    private int position;
    private static final Integer DURATION = 2500;
    private Timer timer = null;
    private PlacePicker.IntentBuilder builder;
    private PlacesAutoCompleteAdapter mPlacesAdapter;
    private Button pickerBtn;
    private AutoCompleteTextView myLocation;
    private static final int PLACE_PICKER_FLAG = 1;

ImageSwitcher imageSwitcher;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(37.775, -122.419), new LatLng(47.620, -73.986));
    protected GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .build();
        setContentView(R.layout.buscarlugares);
        builder = new PlacePicker.IntentBuilder();
        myLocation = (AutoCompleteTextView) findViewById(R.id.myLocation);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                return new ImageView(MainActivity.this);
            }
        });
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        imageSwitcher.setInAnimation(fadeIn);
        imageSwitcher.setOutAnimation(fadeOut);

        mPlacesAdapter = new PlacesAutoCompleteAdapter(this, android.R.layout.simple_list_item_2,
                mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
        myLocation.setOnItemClickListener(mAutocompleteClickListener);
        myLocation.setAdapter(mPlacesAdapter);
        pickerBtn = (Button) findViewById(R.id.pickerBtn);
        pickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    builder = new PlacePicker.IntentBuilder();
                    Intent intent = builder.build(MainActivity.this);
                    // Start the Intent by requesting a result, identified by a request code.
                    startActivityForResult(intent, PLACE_PICKER_FLAG);

                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(), MainActivity.this, 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(MainActivity.this, "Google Play Services is not available.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PLACE_PICKER_FLAG:
                    Place place = PlacePicker.getPlace(data, this);
                    myLocation.setText(place.getName() + ", " + place.getAddress()+", "+ place.getLatLng());

                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        timer.cancel();
        super.onStop();
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mPlacesAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("place", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                myLocation.setVisibility(View.VISIBLE);
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
        }
    };


    public void start(View button) {
        if (timer != null) {
            timer.cancel();
        }
        position = 0;
        startSlider();
    }

    public void stop(View button) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void startSlider() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                // avoid exception:
                // "Only the original thread that created a view hierarchy can touch its views"
                runOnUiThread(new Runnable() {
                    public void run() {
                        imageSwitcher.setImageResource(gallery[position]);
                        position++;
                        if (position == gallery.length) {
                            position = 0;
                        }
                    }
                });
            }

        }, 0, DURATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSlider();
    }

}

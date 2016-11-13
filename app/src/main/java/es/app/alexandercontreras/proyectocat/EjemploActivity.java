package es.app.alexandercontreras.proyectocat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class EjemploActivity extends Activity {
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejemplo);
        String i = getIntent().getStringExtra("id");
        t = (TextView)findViewById(R.id.muni);
        t.setText(""+i);
    }
}

package es.app.alexandercontreras.proyectocat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import es.app.alexandercontreras.proyectocat.R;

public class InsertLugar extends AppCompatActivity implements View.OnClickListener {

    private static final String REGISTER_URL = "http://wssitiosudb.com/wssitios/Lugares/resultado/volleyRegister.php";

    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_DEPARTAMENTO = "departamento";
    public static final String KEY_MUNICIPIO = "municipio";
    public static final String KEY_ESTRELLAS = "estrellas";
    public static final String KEY_DESCRIPCION = "descripcion";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";
    public static final String KEY_IDTIPO = "idtipo";


    private EditText edt1,edt2,edt3,edt4,edt5,edt6,edt7,edt8;


    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertlugar);

        edt1 = (EditText) findViewById(R.id.edt1);
        edt2= (EditText) findViewById(R.id.edt2);
        edt3 =  (EditText) findViewById(R.id.edt3);
        edt4 =  (EditText) findViewById(R.id.edt4);
        edt5 =  (EditText) findViewById(R.id.edt5);
        edt6 =  (EditText) findViewById(R.id.edt6);
        edt7 =  (EditText) findViewById(R.id.edt7);
        edt8 =  (EditText) findViewById(R.id.edt8);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(this);
    }

    private void registerUser(){
        final String nombre = edt1.getText().toString().trim();
        final String departamento = edt2.getText().toString().trim();
        final String municipio = edt3.getText().toString().trim();
        final String estrellas = edt4.getText().toString().trim();
        final String descripcion = edt5.getText().toString().trim();
        final String lat = edt6.getText().toString().trim();
        final String idtipo = edt7.getText().toString().trim();
        final String lng = edt8.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(InsertLugar.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InsertLugar.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_NOMBRE,nombre);
                params.put(KEY_DEPARTAMENTO,departamento);
                params.put(KEY_MUNICIPIO, municipio);
                params.put(KEY_ESTRELLAS, estrellas);
                params.put(KEY_DESCRIPCION, descripcion);
                params.put(KEY_LAT, lat);
                params.put(KEY_LNG, lng);
                params.put(KEY_IDTIPO, idtipo);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();
            edt1.setText("");edt2.setText("");edt3.setText("");
            edt4.setText("");edt5.setText("");edt6.setText("");edt7.setText("");edt8.setText("");

        }
    }
}

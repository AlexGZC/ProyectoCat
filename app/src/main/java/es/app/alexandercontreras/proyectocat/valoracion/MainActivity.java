package es.app.alexandercontreras.proyectocat.valoracion;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.app.alexandercontreras.proyectocat.R;

public class MainActivity extends AppCompatActivity {
    String urlJson;
    ArrayList<Sitios> sitioses = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_start);
        Resources res = this.getResources();
        urlJson = res.getString(R.string.url_json);
    }
    @Override
    protected void onResume() {
        super.onResume();
        initContent();
    }
    @Override
    protected void onPause() {
        this.sitioses.clear();
        super.onPause();
    }
    private void initContent(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlJson, new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            parseContent(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void parseContent(JSONArray jsonArray) throws JSONException {
        for(int i=0; i < jsonArray.length(); i++) {
            JSONObject tmp = jsonArray.getJSONObject(i);
            Gson gson = new Gson();
            Sitios t = gson.fromJson(tmp.toString(),Sitios.class);
            sitioses.add(t);
        }
        RecyclerView recyclerView = (RecyclerView)
                findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new Adaptador(sitioses,this ));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
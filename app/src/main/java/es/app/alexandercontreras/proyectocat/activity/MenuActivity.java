package es.app.alexandercontreras.proyectocat.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.pushbots.push.Pushbots;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import es.app.alexandercontreras.proyectocat.R;
import es.app.alexandercontreras.proyectocat.fragmentos.Buscarmun;
import es.app.alexandercontreras.proyectocat.fragmentos.Forminsertar;
import es.app.alexandercontreras.proyectocat.fragmentos.HomeFragment;
import es.app.alexandercontreras.proyectocat.fragmentos.Logueo;
import es.app.alexandercontreras.proyectocat.fragmentos.Museo;
import es.app.alexandercontreras.proyectocat.fragmentos.Nego;
import es.app.alexandercontreras.proyectocat.fragmentos.Parques;
import es.app.alexandercontreras.proyectocat.fragmentos.Playa;
import es.app.alexandercontreras.proyectocat.fragmentos.Valoracion;

public class MenuActivity extends FragmentActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private MenuActivity mContext;
    private ResideMenuItem inicio;
    private ResideMenuItem playa;
    private ResideMenuItem parques;
    private ResideMenuItem museoo;
    private ResideMenuItem inser;
    private ResideMenuItem buscarmun;
    private ResideMenuItem neg;
    private ResideMenuItem star;
    private ResideMenuItem login;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Pushbots.sharedInstance().init(this);
        mContext = this;
        setUpMenu();
        if( savedInstanceState == null )
            changeFragment(new HomeFragment());


    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);

        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //espacio
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        inicio = new ResideMenuItem(this, R.drawable.icon_home,     R.string.inicio);
        playa = new ResideMenuItem(this, R.drawable.playa,  R.string.playa);
        parques = new ResideMenuItem(this, R.drawable.ic_park, R.string.parque);
        museoo = new ResideMenuItem(this, R.drawable.ic_museo, R.string.Museos);
        buscarmun = new ResideMenuItem(this, R.drawable.ic_search, R.string.mun);
        inser = new ResideMenuItem(this, R.drawable.ic_list, R.string.inser);
        neg = new ResideMenuItem(this, R.drawable.ic_negocio, R.string.neg);
        star = new ResideMenuItem(this, R.drawable.ic_start, R.string.star);
        login = new ResideMenuItem(this, R.drawable.ic_facebook, R.string.log);

        neg.setOnClickListener(this);
        inicio.setOnClickListener(this);
        playa.setOnClickListener(this);
        parques.setOnClickListener(this);
        museoo.setOnClickListener(this);
        inser.setOnClickListener(this);
        buscarmun.setOnClickListener(this);
        star.setOnClickListener(this);
        login.setOnClickListener(this);

        resideMenu.addMenuItem(inicio, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(playa, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(parques, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(museoo, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(buscarmun, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(inser, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(neg, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(star, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(login, ResideMenu.DIRECTION_RIGHT);

        //direccion
        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == inicio){
            changeFragment(new HomeFragment());
        }else if (view == playa){
            changeFragment(new Playa());
        }else if (view == parques){
            changeFragment(new Parques());
        }else if (view == museoo){
            changeFragment(new Museo());
        }else if (view == inser){
            changeFragment(new Forminsertar());
        }else if (view == buscarmun){
            changeFragment(new Buscarmun());
        }else if (view == neg){
            changeFragment(new Nego());
        }else if (view == star){
            changeFragment(new Valoracion());
        }else if (view == login){
            changeFragment(new Logueo());
        }



        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                builder
                        .setTitle("Salir de Turismo ESA")
                        .setMessage("Desa realizar esta acción?")
                        .setIcon(R.mipmap.iconos)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("No", null)						//Do nothing on no
                        .show();
                break;
        }

        return super.onKeyDown(keyCode, event);
    }
}

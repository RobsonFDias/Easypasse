package com.example.gustavo.easypasse.view;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gustavo.easypasse.R;
import com.example.gustavo.easypasse.adapter.NavDrawerListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private static boolean alreadyOpen = false;
    public static Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RelativeLayout linearLayout;
    public static FragmentManager fragmentManager;
    private Bundle savedInstanceStates;
    private String quantNotification = "0";
    private Boolean exit = false, voltar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceStates = savedInstanceState;
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linearLayout = (RelativeLayout) findViewById(R.id.menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        iniciar();

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @SuppressLint("ResourceType")
    private void iniciar() {
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        if (!quantNotification.equals("0")) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1), true, quantNotification));
        } else {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        }

        if (!quantNotification.equals("0")) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1), true, quantNotification));
        } else {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        }

        if (quantNotification.equals("0")) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        } else {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, quantNotification));
        }
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
        //navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        if (savedInstanceStates == null) {
            displayView(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!alreadyOpen) {
            mDrawerLayout.openDrawer(linearLayout);
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDrawerLayout.closeDrawer(linearLayout);
                }
            }, 500);

            alreadyOpen = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            if (voltar) {
                Toast.makeText(this, "Pressione novamente para sair.", Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);
            } else {
                displayView(0);
                voltar = true;
            }
        }
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mudaCorFundoMenu(parent, position);
            displayView(position);
        }

    }

    private void mudaCorFundoMenu(AdapterView<?> parent, Integer position) {
        try {
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (i == position) {
                    parent.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorClickMenu));
                } else {
                    parent.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorFundoMenu));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayView(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new PrincipalFragment();
                break;
            case 1:

                break;
            case 2:

                break;
            case 3:
                fragment = new AjudaFragment();
                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
            case 8:

                break;
            default:
                break;
        }

        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                try {
                    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            setTitle("");
        }
    }
}

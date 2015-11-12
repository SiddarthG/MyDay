package com.appdoor.sid.myday;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RadioGroup radioGroup;

    private RadioButton price, rating, discount;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Spinner spinner;

    RequestQueue mRequestQueue;
    String url="https://mygola.0x10.info/api/mygola?type=json&query=list_activity";

    ArrayList<DataSet> list=new ArrayList<>();
    ArrayList<DataSet> listF=new ArrayList<>();


    public void sort(String category){
        if(category.equals("All")){
            mAdapter = new MyRecAdapter(MainActivity.this, list);
            mRecyclerView.setAdapter(mAdapter);

        }
        else {
            listF.clear();
            for (DataSet a : list) {
                if (a.city.equals(category)) {
                    listF.add(a);
                    mAdapter = new MyRecAdapter(MainActivity.this, listF);
                    mRecyclerView.setAdapter(mAdapter);

                }
            }
        }

    }


    public void parse(){


        mRequestQueue = VolleySingleton.getsInstance().getmRequestQueue();


        try {
            Toast.makeText(MainActivity.this, "entered try json", Toast.LENGTH_LONG).show();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(MainActivity.this, "entered onResponse", Toast.LENGTH_LONG).show();

                    JSONArray activities = null;
                    try {
                        activities = response.getJSONArray("activities");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < activities.length(); i++) {


                        JSONObject obj = null;
                        try {
                            obj = activities.getJSONObject(i);

                            String name=obj.getString("name");
                            String image=obj.getString("image");
                            float actual_price=(float)obj.getLong("actual_price");
                            String discount=obj.getString("discount");
                            float discount_percentage=Float.parseFloat(discount.substring(0, discount.length() - 1));
                            //Toast.makeText(MainActivity.this,""+discount_percentage,Toast.LENGTH_LONG).show();
                            float discount_value=(actual_price*discount_percentage)/100;
                            float effective_price=actual_price-discount_value;

                            int rating=obj.getInt("rating");
                            String city=obj.getString("city");
                            String location=obj.getString("location");
                            String description=obj.getString("description");

                            DataSet object = new DataSet(name, image, discount_percentage,actual_price, effective_price,discount, rating, city, location, description);

                            list.add(object);

                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "exception", Toast.LENGTH_LONG).show();

                        }


                    }

                    mAdapter=new MyRecAdapter(MainActivity.this,list);
                    mRecyclerView.setAdapter(mAdapter);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            mRequestQueue.add(jsonObjectRequest);
        }catch (Exception e){
            Toast.makeText(this,"outer exception",Toast.LENGTH_LONG).show();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner= (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> sAdapter=ArrayAdapter.createFromResource(this,R.array.city,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        sort("All");
                        break;
                    case 1:
                        sort("Bangalore");
                        break;
                    case 2:
                        sort("Mumbai");
                        break;
                    case 3:
                        sort("Delhi");
                        break;
                    case 4:
                        sort("Kolkatta");
                        break;
                    case 5:
                        sort("Chennai");
                        break;
                    case 6:
                        sort("Ahmedabad");
                        break;
                    case 7:
                        sort("Goa");
                        break;




                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // list=getData();
        // mAdapter.notifyDataSetChanged();
        // specify an adapter (see also next example)
        mAdapter = new MyRecAdapter(this,new DataSet());
        mRecyclerView.setAdapter(mAdapter);

        parse();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        radioGroup = (RadioGroup) findViewById(R.id.radioGroupSort);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pricesort) {
                    Collections.sort(list, DataSet.PriceComparator);
                    mAdapter.notifyDataSetChanged();
                } else if (checkedId == R.id.ratingsort) {
                    Collections.sort(list, DataSet.RatingComparator);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Collections.sort(list, DataSet.DiscountComparator);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share with.."));


        } else if (id == R.id.favs) {
            Intent intent=new Intent(MainActivity.this,FavActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}




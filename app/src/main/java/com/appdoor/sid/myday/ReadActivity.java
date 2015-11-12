package com.appdoor.sid.myday;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class ReadActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageLoader mImageLoader;
    VolleySingleton volley;

    TextView city;
    ImageView imageView;
    TextView name;
    TextView actual_price;
    TextView discount;
    TextView location;
    TextView description;
    RatingBar rating;
    TextView effective_price;


    DataSet dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        volley = VolleySingleton.getsInstance();
        mImageLoader = volley.getImageLoader();
        imageView= (ImageView) findViewById(R.id.imageViewContent);

        city=(TextView)findViewById(R.id.city);
        name=(TextView)findViewById(R.id.name);
        actual_price=(TextView)findViewById(R.id.actual_price);
        effective_price= (TextView) findViewById(R.id.effective_price);
        discount=(TextView)findViewById(R.id.discount);
        location=(TextView)findViewById(R.id.location);
        description=(TextView)findViewById(R.id.description);
        rating= (RatingBar) findViewById(R.id.rating);

        LayerDrawable drawable = (LayerDrawable) rating.getProgressDrawable();
        Drawable progress = drawable.getDrawable(2);
        DrawableCompat.setTint(progress, ContextCompat.getColor(this, R.color.colorAccent));
        progress = drawable.getDrawable(1);
        DrawableCompat.setTintMode(progress, PorterDuff.Mode.DST_ATOP);
        DrawableCompat.setTint(progress, ContextCompat.getColor(this, R.color.colorAccent));
        DrawableCompat.setTintMode(progress, PorterDuff.Mode.SRC_ATOP);
        DrawableCompat.setTint(progress, ContextCompat.getColor(this, R.color.colorAccentTransparent));
        progress = drawable.getDrawable(0);
        DrawableCompat.setTint(progress, ContextCompat.getColor(this, R.color.colorAccentTransparent));

        Intent i=getIntent();
        dataSet= (DataSet) i.getExtras().getSerializable(MyRecAdapter.DATA_ARRAY);
        city.setText(dataSet.city);
        name.setText(dataSet.name);
        actual_price.setText(String.valueOf(dataSet.actual_price));
        effective_price.setText(String.valueOf(dataSet.effective_price));
        discount.setText(String.valueOf(dataSet.discount));
        description.setText(dataSet.description);
        location.setText(dataSet.location);
        rating.setRating(dataSet.rating);


        String Thumburl=dataSet.image;

        loadImages(Thumburl);


    }


    private void loadImages(String urlThumbnail) {
        if (urlThumbnail!=null) {
            mImageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    imageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }


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
        getMenuInflater().inflate(R.menu.read, menu);
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
            Intent intent=new Intent(ReadActivity.this,FavActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

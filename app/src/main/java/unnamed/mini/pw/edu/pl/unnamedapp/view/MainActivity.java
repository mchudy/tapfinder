package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.f2prateek.rx.preferences.Preference;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import javax.inject.Inject;

import butterknife.Bind;
import timber.log.Timber;
import unnamed.mini.pw.edu.pl.unnamedapp.App;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import unnamed.mini.pw.edu.pl.unnamedapp.di.qualifier.UsernamePreference;

public class MainActivity extends BaseActivity {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.drawer)
    NavigationView drawer;

    @Inject
    @UsernamePreference
    Preference<String> usernamePreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        setContentView(R.layout.activity_main);
        initToolbar();

        drawer.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return true;
        });
        View headerView = drawer.getHeaderView(0);
        LinearLayout drawerHeader = (LinearLayout) headerView.findViewById(R.id.drawer_header);
        drawerHeader.setOnClickListener(v -> {
            changeFragment(new MyProfileFragment());
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        changeFragment(new MapFragment());
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
                drawerLayout.openDrawer(GravityCompat.START);
            }
        } else if (item.getItemId() == R.id.action_search) {
            showSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSearch() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                    .build();
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Timber.e(e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                //TODO
                PlaceDetailsFragment detailsFragment = new PlaceDetailsFragment();
                detailsFragment.setPlace(place);
                changeFragment(detailsFragment);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Timber.e(status.getStatusMessage());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    private void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_item:
                changeFragment(new MapFragment());
                break;
            case R.id.logout_item:
                logout();
                break;
            case R.id.my_vicinity:
                //changeFragment(new BaseFragment());
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void logout() {
        usernamePreference.delete();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}

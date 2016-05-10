package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.f2prateek.rx.preferences.Preference;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import javax.inject.Inject;

import butterknife.Bind;
import timber.log.Timber;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import unnamed.mini.pw.edu.pl.unnamedapp.di.qualifier.AccessTokenPreference;
import unnamed.mini.pw.edu.pl.unnamedapp.di.qualifier.UsernamePreference;
import unnamed.mini.pw.edu.pl.unnamedapp.view.place.PlaceFragment;

public class MainActivity extends BaseActivity {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private TextView currentUsername;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.drawer)
    NavigationView drawer;

    @Inject
    @UsernamePreference
    Preference<String> usernamePreference;

    @Inject
    @AccessTokenPreference
    Preference<String> accessTokenPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        setContentView(R.layout.activity_main);

        drawer.setNavigationItemSelectedListener(item -> {
            if (!item.isChecked()) {
                item.setChecked(true);
                selectDrawerItem(item);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
        View headerView = drawer.getHeaderView(0);
        LinearLayout drawerHeader = (LinearLayout) headerView.findViewById(R.id.drawer_header);
        currentUsername = (TextView) drawerHeader.findViewById(R.id.drawer_username);
        currentUsername.setText(usernamePreference.get());

        drawerHeader.setOnClickListener(v -> {
            uncheckAllMenuItems();
            changeFragment(new MyProfileFragment());
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        changeFragment(new MapFragment());
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
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
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
                PlaceFragment detailsFragment = PlaceFragment.newInstance(place.getId(), place.getName().toString());
                changeFragmentAndAddToStack(detailsFragment);
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
            case R.id.favourites:
                //changeFragment(new BaseFragment());
                break;
        }
    }

    private void logout() {
        usernamePreference.delete();
        accessTokenPreference.delete();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void uncheckAllMenuItems() {
        final Menu menu = drawer.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    subMenuItem.setChecked(false);
                }
            } else {
                item.setChecked(false);
            }
        }
    }

}

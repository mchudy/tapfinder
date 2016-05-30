package tk.tapfinderapp.view;

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
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.Constants;
import tk.tapfinderapp.R;
import tk.tapfinderapp.di.qualifier.AccessTokenPreference;
import tk.tapfinderapp.di.qualifier.UserImagePreference;
import tk.tapfinderapp.di.qualifier.UsernamePreference;
import tk.tapfinderapp.event.UserImageChangedEvent;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.view.findbeer.FindBeerFragment;
import tk.tapfinderapp.view.login.LoginActivity;
import tk.tapfinderapp.view.map.MapFragment;
import tk.tapfinderapp.view.profile.MyProfileFragment;

import static butterknife.ButterKnife.findById;

public class MainActivity extends BaseActivity {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private TextView currentUsername;
    private CircleImageView userImage;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.drawer)
    NavigationView drawer;

    @Inject
    TapFinderApiService apiService;

    @Inject
    @UsernamePreference
    Preference<String> usernamePreference;

    @Inject
    @AccessTokenPreference
    Preference<String> accessTokenPreference;

    @Inject
    @UserImagePreference
    Preference<String> userImagePreference;

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
        userImage = findById(headerView, R.id.profile_image);
        currentUsername = findById(headerView, R.id.drawer_username);
        currentUsername.setText(usernamePreference.get());

        drawerHeader.setOnClickListener(v -> {
            uncheckAllMenuItems();
            changeFragment(new MyProfileFragment());
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            }
        });
        loadImage();
        changeFragment(new MapFragment());
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(UserImageChangedEvent event){
        refreshProfileImage();
    }

    private void refreshProfileImage() {
        Picasso.with(this)
                .load(Constants.API_BASE_URI + userImagePreference.get())
                .into(userImage);
    }

    private void loadImage() {
        apiService.getUser(usernamePreference.get())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(user -> {
                    userImagePreference.set(user.getImagePath());
                    refreshProfileImage();
                }, t -> Timber.wtf(t.getMessage()));
    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
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
                openDrawer();
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //TODO: custom search
                //Place place = PlaceAutocomplete.getPlace(this, data);
                //PlaceFragment detailsFragment = PlaceFragment.newInstance(p);
                //changeFragmentWithBackStack(detailsFragment);
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
            case R.id.find_beer_item:
                changeFragment(new FindBeerFragment());
                break;
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

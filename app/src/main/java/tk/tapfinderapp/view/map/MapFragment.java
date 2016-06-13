package tk.tapfinderapp.view.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.googleplaces.Place;
import tk.tapfinderapp.model.googleplaces.PlacesResult;
import tk.tapfinderapp.service.GoogleMapsApiService;
import tk.tapfinderapp.view.BaseActivity;
import tk.tapfinderapp.view.LocationAwareFragment;
import tk.tapfinderapp.view.place.PlaceFragment;

@SuppressWarnings("ResourceType")
public class MapFragment extends LocationAwareFragment implements OnMapReadyCallback {

    private static final int PLACES_SEARCH_MAX_RESULTS = 20;
    private static final float MIN_RELOAD_DISTANCE = 10f; // in meters

    private HashMap<String, Place> markerPlacesIds = new HashMap<>();
    private GoogleMap map;
    private SupportMapFragment mapFragment;

    @Inject
    GoogleMapsApiService googleApiService;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activityComponent().inject(this);
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            fm.beginTransaction()
                    .add(R.id.map, mapFragment, "mapFragment")
                    .commit();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onPermissionsGranted() {
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        }
        map.setOnInfoWindowClickListener(marker -> {
            Place place = markerPlacesIds.get(marker.getId());
            PlaceFragment detailsFragment = PlaceFragment.newInstance(place);
            ((BaseActivity) getActivity()).changeFragment(detailsFragment);
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if(userLocation == null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 15));
            loadPubs(location);
            userLocation = location;
        } else {
            float[] distance = new float[1];
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), location.getLatitude(),
                    location.getLongitude(), distance);
            if (distance[0] > MIN_RELOAD_DISTANCE) {
                loadPubs(location);
                userLocation = location;
            }
        }
    }

    private void loadPubs(Location location) {
        markerPlacesIds.clear();
        map.clear();
        String locationString = location.getLatitude() + "," + location.getLongitude();
        googleApiService.getNearbyPubs(locationString, getString(R.string.google_places_key))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(r -> {
                    showMarkers(r);
                    if (r.size() == PLACES_SEARCH_MAX_RESULTS) {
                        loadNextPage(r, 2);
                    }
                });
    }

    private void loadNextPage(PlacesResult r, int pageNumber) {
        if (pageNumber > 3) return;
        Observable.just(1)
                .delay(3, TimeUnit.SECONDS)
                .flatMap(a -> googleApiService.getNextPage(r.getNextPageToken(), getString(R.string.google_places_key)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(r2 -> {
                            showMarkers(r2);
                            if (r2.size() == PLACES_SEARCH_MAX_RESULTS) {
                                loadNextPage(r2, pageNumber + 1);
                            }
                        },
                        e -> Timber.wtf(e, "Loading next page results"));
    }

    private void showMarkers(PlacesResult result) {
        Timber.d("Loaded %d places", result.size());
        for (Place place : result) {
            Place.Location location = place.getGeometry().getLocation();
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLat(), location.getLng()))
                    .title(place.getName())
                    .snippet(place.getFormattedAddress()));
            markerPlacesIds.put(marker.getId(), place);
        }
    }

    @Override
    protected int getTitleResId() {
        return R.string.nearby;
    }
}

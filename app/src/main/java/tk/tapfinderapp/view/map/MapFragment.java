package tk.tapfinderapp.view.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
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
import tk.tapfinderapp.model.googleplaces.Place;
import tk.tapfinderapp.model.googleplaces.PlacesResult;
import tk.tapfinderapp.service.GoogleMapsApiService;
import tk.tapfinderapp.view.BaseActivity;
import tk.tapfinderapp.view.BaseFragment;
import tk.tapfinderapp.view.place.PlaceFragment;
import tk.tapfinderapp.R;

@SuppressWarnings("ResourceType")
public class MapFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int PLACES_SEARCH_MAX_RESULTS = 20;
    private static final int LOCATION_PERMISSION_REQUEST = 1;

    private HashMap<String, Place> markerPlacesIds = new HashMap<>();
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location userLocation;
    private static LocationRequest locationRequest;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }
        mapFragment.getMapAsync(this);
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                        if(googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        map.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getActivity(), "Location permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
            ((BaseActivity) getActivity()).changeFragmentWithBackStack(detailsFragment);
        });
    }

    private void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(Places.GEO_DATA_API)
                    .build();
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        userLocation = location;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 15));
        loadPubs(location);
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
                        e -> Timber.wtf(e.getMessage()));
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
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected int getTitleResId() {
        return R.string.nearby;
    }
}

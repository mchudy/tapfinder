package unnamed.mini.pw.edu.pl.unnamedapp.view.map;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces.Place;
import unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces.PlacesResult;
import unnamed.mini.pw.edu.pl.unnamedapp.service.GoogleMapsApiService;
import unnamed.mini.pw.edu.pl.unnamedapp.view.BaseActivity;
import unnamed.mini.pw.edu.pl.unnamedapp.view.BaseFragment;
import unnamed.mini.pw.edu.pl.unnamedapp.view.place.PlaceFragment;

@SuppressWarnings("ResourceType")
public class MapFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int PLACES_SEARCH_MAX_RESULTS = 20;

    private HashMap<String, Place> markerPlacesIds = new HashMap<>();
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location userLocation;
    private static LocationRequest locationRequest;

    @Inject
    GoogleMapsApiService googleApiService;

    public MapFragment() {}

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
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            fm.beginTransaction()
                .add(R.id.map, mapFragment, "mapFragment")
                .commit();
        }
        mapFragment.getMapAsync(this);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.setOnInfoWindowClickListener(marker -> {
            Place place = markerPlacesIds.get(marker.getId());
            PlaceFragment detailsFragment = PlaceFragment.newInstance(place);
            ((BaseActivity)getActivity()).changeFragmentWithBackStack(detailsFragment);
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        userLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (userLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            if(isAdded()) {
                handleNewLocation(userLocation);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        googleApiClient.connect();
        super.onResume();
    }

    private void handleNewLocation(Location location) {
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
                    if(r.size() == PLACES_SEARCH_MAX_RESULTS) {
                        loadNextPage(r, 2);
                    }
                });
    }

    private void loadNextPage(PlacesResult r, int pageNumber) {
        if(pageNumber > 3) return;
        Observable.just(1)
                .delay(3, TimeUnit.SECONDS)
                .flatMap(a -> googleApiService.getNextPage(r.getNextPageToken(), getString(R.string.google_places_key)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(r2 -> {
                            showMarkers(r2);
                            if(r2.size() == PLACES_SEARCH_MAX_RESULTS) {
                                loadNextPage(r2, pageNumber + 1);
                            }
                        },
                        e -> Timber.wtf(e.getMessage()));
    }

    private void showMarkers(PlacesResult result) {
        Timber.d("Loaded %d places" , result.size());
        for(Place place : result) {
            Place.Location location = place.getGeometry().getLocation();
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLat(), location.getLng()))
                    .title(place.getName())
                    .snippet(place.getFormattedAddress()));
            markerPlacesIds.put(marker.getId(), place);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        userLocation = location;
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

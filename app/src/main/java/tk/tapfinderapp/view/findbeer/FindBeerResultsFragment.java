package tk.tapfinderapp.view.findbeer;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.beer.BeerStyleDto;
import tk.tapfinderapp.model.search.FindBeerSearchResultDto;
import tk.tapfinderapp.model.search.FindBeerSearchResultItem;
import tk.tapfinderapp.model.googleplaces.Place;
import tk.tapfinderapp.model.googleplaces.PlacesResult;
import tk.tapfinderapp.service.GoogleMapsApiService;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.view.BaseActivity;
import tk.tapfinderapp.view.FragmentChanger;
import tk.tapfinderapp.view.LocationAwareFragment;

public class FindBeerResultsFragment extends LocationAwareFragment {

    private static final String BEER_STYLE_KEY = "beerStyle";
    private static final String MAX_PRICE_KEY = "maxPriceKey";

    private BeerStyleDto beerStyle;
    private double maxPrice;
    private boolean resultsShown = false;
    private FindBeerResultsAdapter adapter;

    @Bind(R.id.results)
    RecyclerView results;

    @Inject
    TapFinderApiService service;

    @Inject
    GoogleMapsApiService googleMapsService;

    public static FindBeerResultsFragment newInstance(BeerStyleDto beerStyle, double maxPrice) {
        FindBeerResultsFragment fragment = new FindBeerResultsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BEER_STYLE_KEY, Parcels.wrap(beerStyle));
        args.putDouble(MAX_PRICE_KEY, maxPrice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        beerStyle = Parcels.unwrap(args.getParcelable(BEER_STYLE_KEY));
        maxPrice = args.getDouble(MAX_PRICE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_beer_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityComponent().inject(this);
        ButterKnife.bind(this, view);
        ActionBar actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(getString(R.string.nearby_pubs_serving, beerStyle.getName()));
        }
        setAdapter();
    }

    private void setAdapter() {
        results.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FindBeerResultsAdapter((FragmentChanger)getActivity());
        results.setAdapter(adapter);
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        if(!resultsShown) {
            resultsShown = true;
            adapter.setUserLocation(location);
            loadResults();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resultsShown = false;
        if(userLocation != null) {
            adapter.setUserLocation(userLocation);
            loadResults();
        }
    }

    private void loadResults() {
        String locationString = userLocation.getLatitude() + "," + userLocation.getLongitude();
        googleMapsService.getNearbyPubs(locationString, getString(R.string.google_places_key))
                .map(PlacesResult::getResults)
                .flatMap(places -> service.getPlacesWithBeer(beerStyle.getId(), maxPrice, getIds(places)),
                        Pair::create)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::showItems, t -> Timber.wtf(t, "Loading nearby pubs"));
    }

    private void showItems(Pair<List<Place>, List<FindBeerSearchResultDto>> pair) {
        List<FindBeerSearchResultItem> items = transformResults(pair);
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

    private List<FindBeerSearchResultItem> transformResults(Pair<List<Place>, List<FindBeerSearchResultDto>> pair) {
        List<Place> places = pair.first;
        List<FindBeerSearchResultDto> results = pair.second;
        List<FindBeerSearchResultItem> items = new ArrayList<>();
        for(FindBeerSearchResultDto result : results) {
            Place place = null;
            for(Place p : places) {
                if(p.getPlaceId().equals(result.getPlaceId())) {
                    place = p;
                }
            }
            items.add(new FindBeerSearchResultItem(place, result.getBeers()));
        }
        return items;
    }

    @Override
    protected int getTitleResId() {
        return R.string.search_results;
    }

    @Override
    protected void onPermissionsSuccess() {
        buildGoogleApiClient();
    }

    private List<String> getIds(List<Place> places) {
        List<String> ids = new ArrayList<>();
        for(Place p : places) {
            ids.add(p.getPlaceId());
        }
        return ids;
    }
}

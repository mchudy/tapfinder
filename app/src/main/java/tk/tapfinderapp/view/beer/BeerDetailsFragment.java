package tk.tapfinderapp.view.beer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.Constants;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.beer.BeerDetailsDto;
import tk.tapfinderapp.model.place.PlaceWithBeerDto;
import tk.tapfinderapp.service.GoogleMapsApiService;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.util.DividerItemDecoration;
import tk.tapfinderapp.util.EmptyRecyclerView;
import tk.tapfinderapp.view.BaseActivity;
import tk.tapfinderapp.view.BaseFragment;

public class BeerDetailsFragment extends BaseFragment{

    private static final String BEER_ID_KEY = "beerId";

    private PlacesWithBeerAdapter adapter;
    private int beerId;

    @Bind(R.id.brewery)
    TextView brewery;

    @Bind(R.id.beer_name)
    TextView beerName;

    @Bind(R.id.description)
    TextView description;

    @Bind(R.id.style)
    TextView style;

    @Bind(R.id.beer_image)
    ImageView beerImage;

    @Bind(R.id.places)
    EmptyRecyclerView places;

    @Bind(R.id.no_results)
    TextView emptyView;

    @Inject
    TapFinderApiService apiService;

    @Inject
    BeerDetailsPresenter presenter;

    @Inject
    GoogleMapsApiService googleMapsService;

    public static BeerDetailsFragment newInstance(int beerId) {
        BeerDetailsFragment fragment = new BeerDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(BEER_ID_KEY, beerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beerId = getArguments().getInt(BEER_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beer_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        activityComponent().inject(this);
        presenter.loadBeerDetails(beerId, this);
        initAdapter();
    }

    private void initAdapter() {
        adapter = new PlacesWithBeerAdapter((BaseActivity)getActivity());
        places.setLayoutManager(new LinearLayoutManager(getContext()));
        places.addItemDecoration(new DividerItemDecoration(getContext()));
        places.setAdapter(adapter);
        places.setEmptyView(emptyView);
    }

    public void updateDetails(BeerDetailsDto details) {
        brewery.setText(details.getBrewery().getName());
        beerName.setText(details.getName());
        description.setText(details.getDescription());
        style.setText(details.getStyle());
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(details.getName());
        }
        loadPhoto(details.getImagePath());
        loadPlaces(details.getPlaces());
    }

    private void loadPlaces(List<PlaceWithBeerDto> places) {
        for(PlaceWithBeerDto p : places) {
            googleMapsService.getPlaceDetails(p.getPlaceId(), getString(R.string.google_places_key))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(placeDetails -> adapter.addPlace(placeDetails.getResult(), p.getPrice()),
                            t -> Timber.wtf(t, "Error while loading place detaila"));
        }
    }

    private void loadPhoto(String imagePath) {
        Picasso.with(getContext())
                .load(Constants.API_BASE_URI + imagePath)
                .placeholder(R.drawable.image_placeholder)
                .into(beerImage);
    }

    @Override
    protected int getTitleResId() {
        return R.string.beer_details;
    }
}

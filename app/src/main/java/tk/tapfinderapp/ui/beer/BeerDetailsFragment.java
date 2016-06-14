package tk.tapfinderapp.ui.beer;

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

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import tk.tapfinderapp.Constants;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.beer.BeerDetailsDto;
import tk.tapfinderapp.model.googleplaces.PlaceDetails;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.ui.base.BaseActivity;
import tk.tapfinderapp.ui.base.BaseFragment;
import tk.tapfinderapp.util.DividerItemDecoration;
import tk.tapfinderapp.util.EmptyRecyclerView;

public class BeerDetailsFragment extends BaseFragment implements BeerDetailsView {

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
    BeerDetailsPresenter presenter;

    @Inject
    TapFinderApiService apiService;

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
        activityComponent().inject(this);
        beerId = getArguments().getInt(BEER_ID_KEY);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter.attachView(this);
        return inflater.inflate(R.layout.fragment_beer_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.loadBeerDetails(beerId);
        initAdapter();
    }

    @Override
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
        presenter.loadPlaces(getString(R.string.google_places_key));
    }

    @Override
    public void addPlace(PlaceDetails result, double price) {
        adapter.addPlace(result, price);
        adapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        adapter = new PlacesWithBeerAdapter((BaseActivity)getActivity());
        places.setLayoutManager(new LinearLayoutManager(getContext()));
        places.addItemDecoration(new DividerItemDecoration(getContext()));
        places.setAdapter(adapter);
        places.setEmptyView(emptyView);
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

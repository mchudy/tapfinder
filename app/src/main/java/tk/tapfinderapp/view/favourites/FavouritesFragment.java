package tk.tapfinderapp.view.favourites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f2prateek.rx.preferences.Preference;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.R;
import tk.tapfinderapp.di.qualifier.UsernamePreference;
import tk.tapfinderapp.service.GoogleMapsApiService;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.util.EmptyRecyclerView;
import tk.tapfinderapp.view.BaseFragment;
import tk.tapfinderapp.view.FragmentChanger;

public class FavouritesFragment extends BaseFragment {

    private static final String PLACES_IDS_KEY = "placesIds";

    private List<String> placesIds;

    @Bind(R.id.favourite_places)
    EmptyRecyclerView favouritePlaces;

    @Bind(R.id.no_results)
    TextView emptyView;

    @Inject
    @UsernamePreference
    Preference<String> usernamePreference;

    @Inject
    TapFinderApiService apiService;

    @Inject
    GoogleMapsApiService googleApiService;

    private FavouritePlacesAdapter adapter;

    public static FavouritesFragment newInstance() {
        return new FavouritesFragment();
    }

    public static FavouritesFragment newInstance(List<String> placesIds) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putParcelable(PLACES_IDS_KEY, Parcels.wrap(placesIds));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        if(getArguments() == null) {
            loadFavorites();
        } else {
            placesIds = Parcels.unwrap(getArguments().getParcelable(PLACES_IDS_KEY));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initAdapter();
        loadPlaces();
    }

    private void loadFavorites() {
        apiService.getUser(usernamePreference.get())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(details -> {
                    placesIds = details.getFavouritePlaces();
                    loadPlaces();
                }, t -> Timber.wtf(t, "Error getting favorite places"));
    }

    private void loadPlaces() {
        if(placesIds == null) return;
        for(String id : placesIds) {
            googleApiService.getPlaceDetails(id, getString(R.string.google_places_key))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(details -> {
                        adapter.addItem(details.getResult());
                        adapter.notifyDataSetChanged();
                    }, t -> Timber.wtf(t, "Error getting place details"));
        }
    }

    private void initAdapter() {
        favouritePlaces.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavouritePlacesAdapter((FragmentChanger)getActivity());
        favouritePlaces.setAdapter(adapter);
        favouritePlaces.setEmptyView(emptyView);
    }

    @Override
    protected int getTitleResId() {
        return R.string.favourites;
    }
}

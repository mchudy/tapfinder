package tk.tapfinderapp.view.place.beers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.R;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.util.DividerItemDecoration;
import tk.tapfinderapp.view.BaseActivity;
import tk.tapfinderapp.view.FabFragmentHandler;
import tk.tapfinderapp.view.place.addbeer.AddBeerOnTapFragment;

public class PlaceBeersFragment extends Fragment implements FabFragmentHandler {

    private String placeId;

    @Bind(R.id.beers)
    RecyclerView beers;

    @Inject
    TapFinderApiService apiService;

    @Inject
    PlaceBeersAdapter beersAdapter;

    public static PlaceBeersFragment newInstance(String placeId){
        PlaceBeersFragment fragment = new PlaceBeersFragment();
        Bundle args = new Bundle();
        args.putString("placeId", placeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        placeId = args.getString("placeId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_beers, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        ButterKnife.bind(this, view);
        initAdapter();
        loadBeers();
    }

    private void initAdapter() {
        beers.setLayoutManager(new LinearLayoutManager(getActivity()));
        beers.setAdapter(beersAdapter);
        beers.addItemDecoration(new DividerItemDecoration(getContext()));
    }

    private void loadBeers() {
        apiService.getBeersAtPlace(placeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(b -> {
                    beersAdapter.setBeers(b);
                    beersAdapter.notifyDataSetChanged();
                }, t -> Timber.wtf(t.getMessage()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void handleFab(FloatingActionButton fab) {
        fab.setOnClickListener(v -> ((BaseActivity)getActivity()).changeFragmentWithBackStack(AddBeerOnTapFragment.newInstance(placeId)));
    }
}

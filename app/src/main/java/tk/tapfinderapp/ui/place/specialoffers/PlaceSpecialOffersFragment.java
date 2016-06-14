package tk.tapfinderapp.ui.place.specialoffers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.R;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.util.EmptyRecyclerView;
import tk.tapfinderapp.ui.base.BaseActivity;
import tk.tapfinderapp.ui.base.FabFragmentHandler;
import tk.tapfinderapp.ui.place.addspecialoffer.AddSpecialOfferFragment;

public class PlaceSpecialOffersFragment extends Fragment implements FabFragmentHandler {

    private String placeId;

    @Bind(R.id.special_offers)
    EmptyRecyclerView specialOffers;

    @Bind(R.id.no_results)
    TextView emptyView;

    @Inject
    SpecialOffersAdapter adapter;

    @Inject
    TapFinderApiService apiService;

    public static PlaceSpecialOffersFragment newInstance(String placeId){
        PlaceSpecialOffersFragment fragment = new PlaceSpecialOffersFragment();
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
        return inflater.inflate(R.layout.fragment_place_special_offers, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        ButterKnife.bind(this, view);
        initAdapter();
        loadSpecialOffers();
    }

    private void initAdapter() {
        specialOffers.setLayoutManager(new LinearLayoutManager(getActivity()));
        specialOffers.setAdapter(adapter);
        specialOffers.setEmptyView(emptyView);
    }

    private void loadSpecialOffers() {
        apiService.getSpecialOffers(placeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(b -> {
                    adapter.setSpecialOffers(b);
                    adapter.notifyDataSetChanged();
                }, t -> Timber.wtf(t, "Loading special offers"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void handleFab(FloatingActionButton fab) {
        fab.setOnClickListener(v -> (
                (BaseActivity)getActivity()).changeFragment(AddSpecialOfferFragment.newInstance(placeId))
        );
    }
}
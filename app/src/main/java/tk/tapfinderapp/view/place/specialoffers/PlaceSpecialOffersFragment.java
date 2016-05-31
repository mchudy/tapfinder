package tk.tapfinderapp.view.place.specialoffers;

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
import tk.tapfinderapp.view.BaseActivity;
import tk.tapfinderapp.view.FabFragmentHandler;
import tk.tapfinderapp.view.place.addspecialoffer.AddSpecialOfferFragment;

public class PlaceSpecialOffersFragment extends Fragment implements FabFragmentHandler {

    private String placeId;

    @Bind(R.id.special_offers)
    RecyclerView specialOffers;

    //TODO: inject
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
        adapter = new SpecialOffersAdapter(getContext());
        specialOffers.setLayoutManager(new LinearLayoutManager(getActivity()));
        specialOffers.setAdapter(adapter);
    }

    private void loadSpecialOffers() {
        apiService.getSpecialOffers(placeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(b -> {
                    adapter.setSpecialOffers(b);
                    adapter.notifyDataSetChanged();
                }, t -> Timber.wtf(t.getMessage()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void handleFab(FloatingActionButton fab) {
        fab.setOnClickListener(v -> (
                (BaseActivity)getActivity()).changeFragmentWithBackStack(AddSpecialOfferFragment.newInstance(placeId))
        );
    }
}
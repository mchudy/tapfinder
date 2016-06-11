package tk.tapfinderapp.view.beer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.beer.BeerDetailsDto;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.view.BaseFragment;

public class BeerDetailsFragment extends BaseFragment{

    private static final String BEER_ID_KEY = "beerId";

    private int beerId;

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
        loadBeerDetails();
    }

    private void loadBeerDetails() {
        apiService.getBeerDetails(beerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateDetails,
                        t -> Timber.wtf(t, "loading beer details"));
    }

    private void updateDetails(BeerDetailsDto details) {
        loadPhoto();
    }

    private void loadPhoto() {

    }

    @Override
    protected int getTitleResId() {
        return R.string.beer_details;
    }
}

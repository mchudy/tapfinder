package tk.pubfinderapp.view.findbeer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.ButterKnife;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import tk.pubfinderapp.model.BeerStyleDto;
import tk.pubfinderapp.service.ApiService;
import tk.pubfinderapp.view.BaseActivity;
import tk.pubfinderapp.view.BaseFragment;

public class FindBeerResultsFragment extends BaseFragment {

    private static final String BEER_STYLE_KEY = "beerStyleId";
    private static final String MAX_PRICE_KEY = "maxPriceKey";

    private BeerStyleDto beerStyle;
    private double maxPrice;

    @Inject
    ApiService service;

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
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        ButterKnife.bind(this, view);
        ActionBar actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(getString(R.string.nearby_pubs_serving, beerStyle.getName()));
        }
    }

    @Override
    protected int getTitleResId() {
        return R.string.search_results;
    }
}

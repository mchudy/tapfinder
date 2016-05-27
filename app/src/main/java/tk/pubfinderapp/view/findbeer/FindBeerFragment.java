package tk.pubfinderapp.view.findbeer;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import tk.pubfinderapp.model.BeerStyleDto;
import tk.pubfinderapp.service.ApiService;
import tk.pubfinderapp.view.BaseActivity;
import tk.pubfinderapp.view.BaseFragment;

public class FindBeerFragment extends BaseFragment {

    @Inject
    ApiService service;

    @Bind(R.id.beer_styles)
    Spinner stylesSpinner;

    @Bind(R.id.max_price)
    EditText maxPrice;

    @Bind(R.id.max_price_layout)
    TextInputLayout maxPriceLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_find_beer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        ButterKnife.bind(this, view);
        loadStyles();
    }

    @OnClick(R.id.find_beer_button)
    public void findBeer() {
        BeerStyleDto style = (BeerStyleDto) stylesSpinner.getSelectedItem();
        maxPriceLayout.setError(null);
        if(TextUtils.isEmpty(maxPrice.getText())) {
            maxPriceLayout.setError(getString(R.string.error_field_required));
            return;
        }
        ((BaseActivity)getActivity()).changeFragmentWithBackStack(
                FindBeerResultsFragment.newInstance(style, Double.parseDouble(maxPrice.getText().toString())));
    }

    private void loadStyles() {
        service.getBeerStyles()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onStylesLoaded,
                        e -> Timber.wtf(e.getMessage()));
    }

    private void onStylesLoaded(List<BeerStyleDto> styles) {
        ArrayAdapter<BeerStyleDto> dataAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, styles);
        stylesSpinner.setAdapter(dataAdapter);
    }

    @Override
    protected int getTitleResId() {
        return R.string.find_a_beer;
    }
}

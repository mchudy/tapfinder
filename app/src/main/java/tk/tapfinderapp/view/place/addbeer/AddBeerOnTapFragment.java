package tk.tapfinderapp.view.place.addbeer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.place.AddPlaceBeerDto;
import tk.tapfinderapp.model.beer.BeerDto;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.util.DelayAutoCompleteTextView;
import tk.tapfinderapp.util.KeyboardUtils;
import tk.tapfinderapp.view.BaseFragment;

public class AddBeerOnTapFragment extends BaseFragment {

    private String placeId;

    //TODO: inject
    private BeersAdapter adapter;
    private int selectedBeerPosition = -1;

    @Bind(R.id.beer)
    DelayAutoCompleteTextView beerAutoComplete;

    @Bind(R.id.price)
    EditText price;

    @Bind(R.id.description)
    EditText description;

    @Bind(R.id.price_layout)
    TextInputLayout priceLayout;

    @Bind(R.id.progress_bar_autocomplete)
    ProgressBar progressBar;

    @Inject
    TapFinderApiService apiService;

    public static AddBeerOnTapFragment newInstance(String placeId) {
        AddBeerOnTapFragment fragment = new AddBeerOnTapFragment();
        Bundle args = new Bundle();
        args.putString("placeId", placeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        placeId = args.getString("placeId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_beer_on_tap, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        activityComponent().inject(this);
        initAutocomplete();
    }

    private void initAutocomplete() {
        beerAutoComplete.setThreshold(0);
        beerAutoComplete.setProgressBar(progressBar);
        adapter = new BeersAdapter(getContext(), R.layout.beer_item, apiService);
        beerAutoComplete.setAdapter(adapter);
        beerAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            selectedBeerPosition = position;
        });
    }

    @Override
    protected int getTitleResId() {
        return R.string.add_beer_on_tap;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                addBeer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addBeer() {
        if(!validateInput()) return;
        BeerDto selectedBeer = adapter.getItem(selectedBeerPosition);
        AddPlaceBeerDto dto = new AddPlaceBeerDto(selectedBeer.getId(), placeId, description.getText().toString(),
                Double.valueOf(price.getText().toString()));
        apiService.addBeerAtPlace(dto, placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.code() == 201) {
                        Toast.makeText(getContext(), R.string.beer_added, Toast.LENGTH_SHORT).show();
                        KeyboardUtils.hideSoftKeyboard(getActivity());
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                    } else if (response.code() == 409) {
                        Toast.makeText(getContext(), R.string.error_beer_already_added, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.error_occurred), Toast.LENGTH_SHORT).show();
                    }
                }, t -> Timber.wtf(t, "Adding beer"));
    }

    private boolean validateInput() {
        boolean valid = true;
        priceLayout.setError(null);
        if(selectedBeerPosition == -1) {
            valid = false;
        }
        if(TextUtils.isEmpty(price.getText())) {
            priceLayout.setError(getString(R.string.error_field_required));
            valid = false;
        }
        return valid;
    }
}

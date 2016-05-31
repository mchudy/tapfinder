package tk.tapfinderapp.view.place.beers.addbeer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import tk.tapfinderapp.R;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.util.DelayAutoCompleteTextView;
import tk.tapfinderapp.view.BaseFragment;

public class AddBeerOnTapFragment extends BaseFragment {

    private String placeId;

    //TODO: inject
    private BeersAdapter adapter;

    @Bind(R.id.beer)
    DelayAutoCompleteTextView beerAutoComplete;

    @Bind(R.id.price)
    EditText price;

    @Bind(R.id.description)
    EditText description;

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
                getActivity().getFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package tk.tapfinderapp.view.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import timber.log.Timber;
import tk.tapfinderapp.R;
import tk.tapfinderapp.service.GoogleMapsApiService;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.view.BaseActivity;
import tk.tapfinderapp.view.BaseFragment;

public class SearchFragment extends BaseFragment {

    @Inject
    TapFinderApiService service;

    @Inject
    GoogleMapsApiService googleMapsService;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        ButterKnife.bind(this, view);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate( R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search_view);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconified(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                Timber.d(s);
                return false;
            }
        });
    }

    @Override
    protected int getTitleResId() {
        return R.string.empty_string;
    }
}

package tk.tapfinderapp.view.favourites;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import tk.tapfinderapp.R;
import tk.tapfinderapp.util.EmptyRecyclerView;
import tk.tapfinderapp.view.BaseFragment;

public class FavouritesFragment extends BaseFragment {

    @Bind(R.id.favourite_places)
    EmptyRecyclerView favouritePlaces;

    @Bind(R.id.no_results)
    TextView emptyView;

    public static FavouritesFragment newInstance() {
        return new FavouritesFragment();
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
    }

    private void initAdapter() {
        favouritePlaces.setLayoutManager(new LinearLayoutManager());
    }

    @Override
    protected int getTitleResId() {
        return R.string.favourites;
    }
}

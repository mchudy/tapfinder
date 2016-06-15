package tk.tapfinderapp.view.place;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.googleplaces.Photo;
import tk.tapfinderapp.model.googleplaces.Place;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.view.BaseFragment;
import tk.tapfinderapp.view.FabFragmentHandler;

public class PlaceFragment extends BaseFragment {

    public static final String PLACE_KEY = "place";

    private Place place;
    private MenuItem favouriteMenuItem;
    private boolean isFavourite;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.background)
    ImageView background;

    @Inject
    TapFinderApiService apiService;

    public static PlaceFragment newInstance(Place place){
        PlaceFragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putParcelable(PLACE_KEY, Parcels.wrap(place));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        place = Parcels.unwrap(getArguments().getParcelable(PLACE_KEY));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        activityComponent().inject(this);
        viewPager.setOffscreenPageLimit(5);
        setupTabs();
        collapsingToolbarLayout.setTitle(place.getName());
        loadPhoto();
    }

    private void checkIfFavourite() {
        apiService.checkIfInFavourites(place.getPlaceId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
                            if(response.code() == 200) {
                                favouriteMenuItem.setIcon(R.drawable.ic_favorite_white_24dp);
                                isFavourite = true;
                            } else if (response.code() == 404) {
                                favouriteMenuItem.setIcon(R.drawable.ic_favorite_border_white_24dp);
                                isFavourite = false;
                            } else {
                                try {
                                    Timber.wtf(response.errorBody().string());
                                } catch (IOException e) {
                                    Timber.wtf(e, "Error reading error body");
                                }
                            }
                        },
                        t -> Timber.wtf(t, "Checking if place is in favourites"));
    }

    private void loadPhoto() {
        if(place.getPhotos() != null && place.getPhotos().size() > 0) {
            Photo photo = place.getPhotos().get(0);
            Picasso.with(getActivity())
                    .load(photo.getUrl(getString(R.string.google_places_key), 500))
                    .into(background);
        }
    }

    private void setupTabs() {
        PlacePagerAdapter pagerAdapter = new PlacePagerAdapter(getChildFragmentManager(), place.getPlaceId());
        viewPager.setAdapter(pagerAdapter);
        fab.hide();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Fragment pageFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                switch (position) {
                    case PlacePagerAdapter.GENERAL_TAB:
                        fab.hide();
                        break;
                    default:
                        fab.show();
                        ((FabFragmentHandler)pageFragment).handleFab(fab);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem searchItem =  menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.place_menu, menu);
        favouriteMenuItem = menu.findItem(R.id.action_favourite);
        checkIfFavourite();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favourite:
                onFavouriteMenuClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onFavouriteMenuClick() {
        if(favouriteMenuItem == null) return;
        if(isFavourite) {
            apiService.deleteFromFavourites(place.getPlaceId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(response -> checkIfFavourite(),
                        t -> Timber.wtf(t, "Error adding to favourites"));
        } else {
            apiService.addToFavourites(place.getPlaceId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(response -> checkIfFavourite(),
                        t -> Timber.wtf(t, "Error adding to favourites"));
        }
    }

    @Override
    protected int getTitleResId() {
        return R.string.place_details;
    }
}

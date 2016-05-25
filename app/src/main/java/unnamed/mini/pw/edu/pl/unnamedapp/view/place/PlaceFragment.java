package unnamed.mini.pw.edu.pl.unnamedapp.view.place;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces.Photo;
import unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces.Place;
import unnamed.mini.pw.edu.pl.unnamedapp.view.BaseFragment;
import unnamed.mini.pw.edu.pl.unnamedapp.view.FabFragmentHandler;

public class PlaceFragment extends BaseFragment {

    public static final String PLACE_KEY = "place";

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

    private Place place;

    public PlaceFragment() {
    }

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
        View view =  inflater.inflate(R.layout.fragment_place, container, false);
        ButterKnife.bind(this, view);
        setupTabs();
        collapsingToolbarLayout.setTitle(place.getName());
        loadPhoto();
        return view;
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
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.place_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favourite:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getTitleResId() {
        return R.string.place_details;
    }
}

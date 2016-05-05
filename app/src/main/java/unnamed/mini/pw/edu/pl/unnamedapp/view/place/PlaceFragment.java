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

import butterknife.Bind;
import butterknife.ButterKnife;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import unnamed.mini.pw.edu.pl.unnamedapp.view.BaseFragment;

public class PlaceFragment extends BaseFragment {

    public static final String PLACE_ID_KEY = "placeId";
    public static final String PLACE_NAME_KEY = "placeName";

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    private String placeId;
    private String placeName;

    public PlaceFragment() {
    }

    public static PlaceFragment newInstance(String placeId, String placeName){
        PlaceFragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putString(PLACE_ID_KEY, placeId);
        args.putString(PLACE_NAME_KEY, placeName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        placeId = getArguments().getString(PLACE_ID_KEY);
        placeName = getArguments().getString(PLACE_NAME_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_place, container, false);
        ButterKnife.bind(this, view);
        PlacePagerAdapter pagerAdapter = new PlacePagerAdapter(getFragmentManager(), placeId);
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
        collapsingToolbarLayout.setTitle(placeName);
        return view;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        placeId = bundle.getString(PLACE_ID_KEY);
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

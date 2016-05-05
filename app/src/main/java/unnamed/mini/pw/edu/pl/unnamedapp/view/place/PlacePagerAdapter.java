package unnamed.mini.pw.edu.pl.unnamedapp.view.place;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PlacePagerAdapter extends FragmentStatePagerAdapter {
    public static final int TABS_COUNT = 3;
    public static final int GENERAL_TAB = 0;
    public static final int BEERS_TAB = 1;
    public static final int SPECIAL_OFFERS_TAB = 2;

    private String placeId;

    public PlacePagerAdapter(FragmentManager fragmentManager, String placeId) {
        super(fragmentManager);
        this.placeId = placeId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case BEERS_TAB:
                return PlaceBeersFragment.newInstance(placeId);
            case SPECIAL_OFFERS_TAB:
                return PlaceSpecialOffersFragment.newInstance(placeId);
            case GENERAL_TAB:
            default:
                return PlaceGeneralFragment.newInstance(placeId);
        }
    }

    @Override
    public int getCount() {
        return TABS_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case GENERAL_TAB:
                return "General";
            case BEERS_TAB:
                return "Beers";
            case SPECIAL_OFFERS_TAB:
                return "Special offers";
        }
        return null;
    }
}

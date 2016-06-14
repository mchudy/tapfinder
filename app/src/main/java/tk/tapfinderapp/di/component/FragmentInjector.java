package tk.tapfinderapp.di.component;

import com.google.android.gms.location.LocationListener;

import tk.tapfinderapp.ui.beer.BeerDetailsFragment;
import tk.tapfinderapp.ui.findbeer.FindBeerFragment;
import tk.tapfinderapp.ui.findbeer.FindBeerResultsFragment;
import tk.tapfinderapp.ui.map.MapFragment;
import tk.tapfinderapp.ui.place.addbeer.AddBeerOnTapFragment;
import tk.tapfinderapp.ui.place.addspecialoffer.AddSpecialOfferFragment;
import tk.tapfinderapp.ui.place.beers.PlaceBeersFragment;
import tk.tapfinderapp.ui.place.general.PlaceGeneralFragment;
import tk.tapfinderapp.ui.place.specialoffers.PlaceSpecialOffersFragment;
import tk.tapfinderapp.ui.profile.MyProfileFragment;
import tk.tapfinderapp.ui.search.SearchFragment;

public interface FragmentInjector {
    void inject(MapFragment mapFragment);
    void inject(PlaceGeneralFragment placeDetailsGeneralFragment);
    void inject(PlaceBeersFragment placeBeersFragment);
    void inject(PlaceSpecialOffersFragment placeSpecialOffersFragment);
    void inject(FindBeerFragment findBeerFragment);
    void inject(FindBeerResultsFragment findBeerResultsFragment);
    void inject(MyProfileFragment myProfileFragment);
    void inject(AddBeerOnTapFragment addBeerOnTapFragment);
    void inject(AddSpecialOfferFragment addSpecialOfferFragment);
    void inject(LocationListener locationAwareFragment);
    void inject(BeerDetailsFragment beerDetailsFragment);
    void inject(SearchFragment searchFragment);
}

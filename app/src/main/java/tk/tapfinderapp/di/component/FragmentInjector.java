package tk.tapfinderapp.di.component;

import com.google.android.gms.location.LocationListener;

import tk.tapfinderapp.view.beer.BeerDetailsFragment;
import tk.tapfinderapp.view.findbeer.FindBeerFragment;
import tk.tapfinderapp.view.findbeer.FindBeerResultsFragment;
import tk.tapfinderapp.view.map.MapFragment;
import tk.tapfinderapp.view.place.addbeer.AddBeerOnTapFragment;
import tk.tapfinderapp.view.place.addspecialoffer.AddSpecialOfferFragment;
import tk.tapfinderapp.view.place.beers.PlaceBeersFragment;
import tk.tapfinderapp.view.place.general.PlaceGeneralFragment;
import tk.tapfinderapp.view.place.specialoffers.PlaceSpecialOffersFragment;
import tk.tapfinderapp.view.profile.MyProfileFragment;
import tk.tapfinderapp.view.search.SearchFragment;

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

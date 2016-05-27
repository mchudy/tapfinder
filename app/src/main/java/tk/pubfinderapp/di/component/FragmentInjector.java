package tk.pubfinderapp.di.component;

import tk.pubfinderapp.view.findbeer.FindBeerFragment;
import tk.pubfinderapp.view.findbeer.FindBeerResultsFragment;
import tk.pubfinderapp.view.map.MapFragment;
import tk.pubfinderapp.view.place.beers.PlaceBeersFragment;
import tk.pubfinderapp.view.place.general.PlaceGeneralFragment;
import tk.pubfinderapp.view.place.specialoffers.PlaceSpecialOffersFragment;
import tk.pubfinderapp.view.profile.MyProfileFragment;

public interface FragmentInjector {
    void inject(MapFragment mapFragment);
    void inject(PlaceGeneralFragment placeDetailsGeneralFragment);
    void inject(PlaceBeersFragment placeBeersFragment);
    void inject(PlaceSpecialOffersFragment placeSpecialOffersFragment);
    void inject(FindBeerFragment findBeerFragment);
    void inject(FindBeerResultsFragment findBeerResultsFragment);
    void inject(MyProfileFragment myProfileFragment);
}

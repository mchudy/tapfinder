package unnamed.mini.pw.edu.pl.unnamedapp.di.component;

import unnamed.mini.pw.edu.pl.unnamedapp.view.FindBeerFragment;
import unnamed.mini.pw.edu.pl.unnamedapp.view.MapFragment;
import unnamed.mini.pw.edu.pl.unnamedapp.view.place.PlaceBeersFragment;
import unnamed.mini.pw.edu.pl.unnamedapp.view.place.PlaceGeneralFragment;
import unnamed.mini.pw.edu.pl.unnamedapp.view.place.PlaceSpecialOffersFragment;

public interface FragmentInjector {
    void inject(MapFragment mapFragment);
    void inject(PlaceGeneralFragment placeDetailsGeneralFragment);
    void inject(PlaceBeersFragment placeBeersFragment);
    void inject(PlaceSpecialOffersFragment placeSpecialOffersFragment);
    void inject(FindBeerFragment findBeerFragment);
}

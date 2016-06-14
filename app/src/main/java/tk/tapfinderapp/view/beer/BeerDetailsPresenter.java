package tk.tapfinderapp.view.beer;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.service.GoogleMapsApiService;
import tk.tapfinderapp.service.TapFinderApiService;

public class BeerDetailsPresenter {

    @Inject
    TapFinderApiService apiService;

    @Inject
    GoogleMapsApiService googleMapsService;

    @Inject
    public BeerDetailsPresenter() {
    }

    public void loadBeerDetails(int beerId, BeerDetailsFragment fragment) {
        apiService.getBeerDetails(beerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fragment::updateDetails,
                        t -> Timber.wtf(t, "loading beer details"));
    }
}

package tk.tapfinderapp.view.beer;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.service.GoogleMapsApiService;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.view.BasePresenter;

public class BeerDetailsPresenter extends BasePresenter<BeerDetailsFragment> {

    private Subscription subscription;
    private TapFinderApiService apiService;
    private GoogleMapsApiService googleMapsService;

    @Inject
    public BeerDetailsPresenter(TapFinderApiService apiService, GoogleMapsApiService googleMapsService) {
        this.apiService = apiService;
        this.googleMapsService = googleMapsService;
    }

    public void loadBeerDetails(int beerId, BeerDetailsFragment fragment) {
        subscription = apiService.getBeerDetails(beerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fragment::updateDetails,
                        t -> Timber.wtf(t, "loading beer details"));
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}

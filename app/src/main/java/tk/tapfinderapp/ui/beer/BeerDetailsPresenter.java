package tk.tapfinderapp.ui.beer;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;
import tk.tapfinderapp.model.beer.BeerDetailsDto;
import tk.tapfinderapp.model.place.PlaceWithBeerDto;
import tk.tapfinderapp.service.GoogleMapsApiService;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.ui.base.BasePresenter;

public class BeerDetailsPresenter extends BasePresenter<BeerDetailsView>{

    private CompositeSubscription subscription = new CompositeSubscription();
    private TapFinderApiService apiService;
    private GoogleMapsApiService googleMapsService;
    private BeerDetailsDto beerDetails;

    @Inject
    public BeerDetailsPresenter(TapFinderApiService apiService, GoogleMapsApiService googleMapsService) {
        this.apiService = apiService;
        this.googleMapsService = googleMapsService;
    }

    public void loadBeerDetails(int beerId) {
        subscription.add(apiService.getBeerDetails(beerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    beerDetails = result;
                    view.updateDetails(result);
                }, t -> Timber.wtf(t, "loading beer details"))
        );
    }

    public void loadPlaces(String apiKey) {
        subscription.clear();
        for(PlaceWithBeerDto p : beerDetails.getPlaces()) {
            subscription.add(
                    googleMapsService.getPlaceDetails(p.getPlaceId(), apiKey)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(placeDetails -> view.addPlace(placeDetails.getResult(), p.getPrice()),
                                t -> Timber.wtf(t, "Error while loading place detaila"))
            );
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        releaseSubscription();
    }

    private void releaseSubscription() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}

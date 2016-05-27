package tk.pubfinderapp.service;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import tk.pubfinderapp.model.googleplaces.PlaceDetailsResult;
import tk.pubfinderapp.model.googleplaces.PlacesResult;

public interface GoogleMapsApiService {

    @GET("place/nearbysearch/json?type=bar&radius=5000")
    Observable<PlacesResult> getNearbyPubs(@Query("location") String location,
                                           @Query("key") String apiKey);

    @GET("place/nearbysearch/json")
    Observable<PlacesResult> getNextPage(@Query("pagetoken") String pageToken,
                                         @Query("key") String apiKey);

    @GET("place/details/json")
    Observable<PlaceDetailsResult> getPlaceDetails(@Query("placeid")String placeId,
                                                   @Query("key")String apiKey);
}

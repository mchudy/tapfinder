package unnamed.mini.pw.edu.pl.unnamedapp.service;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces.PlaceDetailsResult;
import unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces.PlacesResult;

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

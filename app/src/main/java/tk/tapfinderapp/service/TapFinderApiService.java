package tk.tapfinderapp.service;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import tk.tapfinderapp.model.CommentDto;
import tk.tapfinderapp.model.LikeDto;
import tk.tapfinderapp.model.RatingDto;
import tk.tapfinderapp.model.beer.BeerDetailsDto;
import tk.tapfinderapp.model.beer.BeerDto;
import tk.tapfinderapp.model.beer.BeerStyleDto;
import tk.tapfinderapp.model.place.AddPlaceBeerDto;
import tk.tapfinderapp.model.place.AddSpecialOfferDto;
import tk.tapfinderapp.model.place.PlaceBeerDto;
import tk.tapfinderapp.model.place.SpecialOfferDto;
import tk.tapfinderapp.model.search.FindBeerSearchResultDto;
import tk.tapfinderapp.model.user.AccessTokenDto;
import tk.tapfinderapp.model.user.UserDto;
import tk.tapfinderapp.model.user.UserImageDto;
import tk.tapfinderapp.model.user.UserRegisterDto;
import tk.tapfinderapp.model.user.UserRegisterExternalDto;

public interface TapFinderApiService {

    @HEAD("users")
    Observable<Response<Void>> isUsernameAvailable(@Query("username") String username);

    @HEAD("users")
    Observable<Response<Void>> isEmailAvailable(@Query("email") String email);

    @POST("token")
    @FormUrlEncoded
    Observable<AccessTokenDto> getToken(@Field("username") String username,
                                        @Field("password") String password,
                                        @Field("grant_type") String grantType);

    @GET("users/token")
    Observable<Response<AccessTokenDto>> getTokenFromExternal(@Query("provider") String provider,
                                                              @Query("externalAccessToken") String externalAccessToken);

    @POST("users")
    Observable<ResponseBody> register(@Body UserRegisterDto dto);

    @POST("users/external")
    Observable<Response<AccessTokenDto>> registerExternal(@Body UserRegisterExternalDto dto);

    @GET("beers/{id}")
    Observable<BeerDetailsDto> getBeerDetails(@Path("id") int id);

    @GET("beers/styles")
    Observable<List<BeerStyleDto>> getBeerStyles();

    @GET("beers/search")
    Observable<List<BeerDto>> findBeers(@Query("query") String query);

    @GET("places/{placeId}/comments")
    Observable<List<CommentDto>> getComments(@Path("placeId") String placeId);

    @POST("places/comments")
    Observable<ResponseBody> postComment(@Body CommentDto dto);

    @GET("places/{id}/specialoffers")
    Observable<List<SpecialOfferDto>> getSpecialOffers(@Path("id") String placeId);

    @POST("places/{id}/specialoffers")
    Observable<Response<ResponseBody>> addSpecialOffer(@Body AddSpecialOfferDto dto, @Path("id") String placeId);

    @GET("places/{id}/beers")
    Observable<List<PlaceBeerDto>> getBeersAtPlace(@Path("id") String placeId);

    @POST("places/{id}/beers")
    Observable<Response<ResponseBody>> addBeerAtPlace(@Body AddPlaceBeerDto dto, @Path("id") String placeId);

    @GET("places/search")
    Observable<List<FindBeerSearchResultDto>> getPlacesWithBeer(@Query("beerStyleId") int beerStyleId,
                                                                @Query("maxPrice") double maxPrice,
                                                                @Query("placesIds") List<String> placesIds);

    @PUT("users/image")
    Observable<UserDto> changeImage(@Body UserImageDto dto);

    @GET("users/{username}")
    Observable<UserDto> getUser(@Path("username") String username);

    @PUT("likes")
    Observable<ResponseBody> updateLike(@Body LikeDto dto);

    @GET("rating")
    Observable<RatingDto> getRating(@Query("itemId") int id);

    @HEAD("favorites/places")
    Observable<Response<Void>> checkIfInFavourites(@Query("placeId") String placeId);

    @POST("favorites/places")
    Observable<ResponseBody> addToFavourites(@Query("placeId") String placeId);

    @DELETE("favorites/places")
    Observable<ResponseBody> deleteFromFavourites(@Query("placeId") String placeId);
}

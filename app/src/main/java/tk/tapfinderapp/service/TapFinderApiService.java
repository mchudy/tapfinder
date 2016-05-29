package tk.tapfinderapp.service;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import tk.tapfinderapp.model.AccessTokenModel;
import tk.tapfinderapp.model.BeerStyleDto;
import tk.tapfinderapp.model.CommentDto;
import tk.tapfinderapp.model.PlaceBeerDto;
import tk.tapfinderapp.model.SpecialOfferDto;
import tk.tapfinderapp.model.UserRegisterDto;
import tk.tapfinderapp.model.UserRegisterExternalDto;

public interface TapFinderApiService {

    @HEAD("users")
    Observable<Response<Void>> isUsernameAvailable(@Query("username") String username);

    @HEAD("users")
    Observable<Response<Void>> isEmailAvailable(@Query("email") String email);

    @POST("token")
    @FormUrlEncoded
    Observable<AccessTokenModel> getToken(@Field("username") String username,
                                          @Field("password") String password,
                                          @Field("grant_type") String grantType);

    @GET("users/token")
    Observable<Response<AccessTokenModel>> getTokenFromExternal(@Query("provider") String provider,
                                                                       @Query("externalAccessToken") String externalAccessToken);

    @POST("users")
    Observable<ResponseBody> register(@Body UserRegisterDto dto);

    @POST("users/external")
    Observable<Response<AccessTokenModel>> registerExternal(@Body UserRegisterExternalDto dto);

    @GET("beers/styles")
    Observable<List<BeerStyleDto>> getBeerStyles();

    @GET("places/{placeId}/comments")
    Observable<List<CommentDto>> getComments(@Path("placeId") String placeId);

    @POST("places/comments")
    Observable<ResponseBody> postComment(@Body CommentDto dto);

    @GET("places/{id}/specialoffers")
    Observable<List<SpecialOfferDto>> getSpecialOffers(@Path("id") String placeId);

    @GET("places/{id}/beers")
    Observable<List<PlaceBeerDto>> getBeersAtPlace(@Path("id") String placeId);
}

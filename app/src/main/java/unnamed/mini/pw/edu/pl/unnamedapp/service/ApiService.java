package unnamed.mini.pw.edu.pl.unnamedapp.service;


import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HEAD;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import unnamed.mini.pw.edu.pl.unnamedapp.model.AccessTokenModel;
import unnamed.mini.pw.edu.pl.unnamedapp.model.UserRegisterDto;

public interface ApiService {

    @HEAD("users")
    Observable<Response<Void>> isUsernameAvailable(@Query("username") String username);

    @HEAD("users")
    Observable<Response<Void>> isEmailAvailable(@Query("email") String email);

    @POST("token")
    @FormUrlEncoded
    Observable<AccessTokenModel> getToken(@Field("username") String username,
                                          @Field("password") String password,
                                          @Field("grant_type") String grantType);

    @POST("users")
    Observable<ResponseBody> register(@Body UserRegisterDto dto);
}

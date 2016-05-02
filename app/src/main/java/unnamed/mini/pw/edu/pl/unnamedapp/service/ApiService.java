package unnamed.mini.pw.edu.pl.unnamedapp.service;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import rx.Observable;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import unnamed.mini.pw.edu.pl.unnamedapp.model.AccessTokenModel;
import unnamed.mini.pw.edu.pl.unnamedapp.model.UserRegisterDto;

public interface ApiService {

    @POST("token")
    @FormUrlEncoded
    Observable<AccessTokenModel> getToken(@Field("username") String username,
                                          @Field("password") String password,
                                          @Field("grant_type") String grantType);

    @POST("users")
    Observable<ResponseBody> register(@Body UserRegisterDto dto);
}

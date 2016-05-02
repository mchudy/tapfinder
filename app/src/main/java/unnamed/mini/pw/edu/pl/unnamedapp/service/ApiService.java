package unnamed.mini.pw.edu.pl.unnamedapp.service;


import rx.Observable;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import unnamed.mini.pw.edu.pl.unnamedapp.model.UserRegisterDto;

public interface ApiService {

    @POST("/users")
    Observable<ResponseBody> register(@Body UserRegisterDto dto);
}

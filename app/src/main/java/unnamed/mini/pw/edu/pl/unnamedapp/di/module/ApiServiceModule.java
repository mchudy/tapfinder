package unnamed.mini.pw.edu.pl.unnamedapp.di.module;


import android.text.TextUtils;

import com.f2prateek.rx.preferences.Preference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import unnamed.mini.pw.edu.pl.unnamedapp.Constants;
import unnamed.mini.pw.edu.pl.unnamedapp.di.qualifier.AccessTokenPreference;
import unnamed.mini.pw.edu.pl.unnamedapp.service.ApiService;
import unnamed.mini.pw.edu.pl.unnamedapp.service.GoogleMapsApiService;
import unnamed.mini.pw.edu.pl.unnamedapp.util.NullStringToEmptyAdapterFactory;

@Module
public class ApiServiceModule {

    public static final int TIMEOUT = 20;

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Interceptor networkInterceptor,
                                     HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(networkInterceptor)
                .build();
        return client;
    }

    @Provides
    @Singleton
    Interceptor provideNetworkInterceptor(final @AccessTokenPreference Preference<String> accessTokenPreference) {
        return chain -> {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("Accept-Language", Locale.getDefault().getLanguage());
            String token = accessTokenPreference.get();
            if (!TextUtils.isEmpty(token)) {
                builder.addHeader("Authorization", "Bearer " + token);
            }
            return chain.proceed(builder.build());
        };
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
    }

    @Provides
    @Singleton
    @Named("pubAppApi")
    Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URI)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    @Named("googlePlacesApi")
    Retrofit provideGooglePlacesRetrofit(HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.GOOGLE_MAPS_API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    ApiService provideApiService(@Named("pubAppApi") Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    GoogleMapsApiService provideGooglePlacesApiService(@Named("googlePlacesApi") Retrofit retrofit) {
        return retrofit.create(GoogleMapsApiService.class);
    }
}

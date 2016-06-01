package tk.tapfinderapp.di.module;

import javax.inject.Named;

import retrofit2.Retrofit;
import tk.tapfinderapp.service.GoogleMapsApiService;
import tk.tapfinderapp.service.TapFinderApiService;

import static org.mockito.Mockito.mock;

public class TestApiModule extends ApiServiceModule {

    @Override
    TapFinderApiService provideApiService(@Named("pubAppApi") Retrofit retrofit) {
        return mock(TapFinderApiService.class);
    }

    @Override
    GoogleMapsApiService provideGooglePlacesApiService(@Named("googlePlacesApi") Retrofit retrofit) {
        return mock(GoogleMapsApiService.class);
    }
}

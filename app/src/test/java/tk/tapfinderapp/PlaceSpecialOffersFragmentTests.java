package tk.tapfinderapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.view.MainActivity;
import tk.tapfinderapp.view.place.specialoffers.PlaceSpecialOffersFragment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startVisibleFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class PlaceSpecialOffersFragmentTests {

    private PlaceSpecialOffersFragment fragment;

    @Mock
    private TapFinderApiService mockService;

    @Before
    public void setup()  {
        fragment = PlaceSpecialOffersFragment.newInstance("id");
        startVisibleFragment(fragment, MainActivity.class, R.id.fragment);
    }

    @Test
    public void checkFragmentNotNull() throws Exception {
        assertThat(fragment).isNotNull();
    }

    @Test
    public void shouldLoadOffersFromBackend() {
        Mockito.verify(mockService).getSpecialOffers("id");
    }
}
package tk.tapfinderapp;

import android.support.design.widget.CollapsingToolbarLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import butterknife.ButterKnife;
import tk.tapfinderapp.model.googleplaces.Place;
import tk.tapfinderapp.view.MainActivity;
import tk.tapfinderapp.view.place.PlaceFragment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startVisibleFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class PlaceFragmentTests {

    private PlaceFragment fragment;
    private Place place;

    @Before
    public void setup()  {
        place = new Place();
        place.setId("id");
        place.setName("name");
        fragment = PlaceFragment.newInstance(place);
        startVisibleFragment(fragment, MainActivity.class, R.id.fragment);
    }

    @Test
    public void checkFragmentNotNull() throws Exception {
        assertThat(fragment).isNotNull();
        assertThat(fragment.getView()).isNotNull();
    }

    @Test
    public void shouldSetToolbarTitleToPlaceName() throws Exception {
        CollapsingToolbarLayout toolbar = ButterKnife.findById(fragment.getView(), R.id.collapsing_toolbar);
        assertThat(toolbar.getTitle()).isEqualTo(place.getName());
    }
}

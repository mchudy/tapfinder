package tk.tapfinderapp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import tk.tapfinderapp.view.MainActivity;

import static butterknife.ButterKnife.findById;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class MainActivityTests {
    private MainActivity activity;

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setup()  {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application);
        sharedPreferences
                .edit()
                .putString(Constants.USERNAME_PREFERENCE, "username")
                .commit();
        activity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void shouldSetUsernameInNavigationDrawerHeader() throws Exception {
        NavigationView drawer = findById(activity, R.id.drawer);
        View headerView = drawer.getHeaderView(0);
        TextView usernameView = findById(headerView, R.id.drawer_username);
        assertThat(usernameView.getText().toString()).isEqualTo("username");
    }

}

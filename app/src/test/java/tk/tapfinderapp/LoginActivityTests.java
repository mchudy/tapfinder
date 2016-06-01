package tk.tapfinderapp;

import android.content.Intent;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import butterknife.ButterKnife;
import tk.tapfinderapp.view.login.LoginActivity;
import tk.tapfinderapp.view.login.RegisterActivity;

import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class LoginActivityTests {
    private LoginActivity activity;

    @Before
    public void setup()  {
        activity = Robolectric.setupActivity(LoginActivity.class);
    }

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void signUpClickShouldStartRegisterActivity() throws Exception {
        TextView button = ButterKnife.findById(activity, R.id.sign_up_button);
        button.performClick();
        Intent intent = Shadows.shadowOf(activity).peekNextStartedActivity();
        assertEquals(RegisterActivity.class.getCanonicalName(), intent.getComponent().getClassName());
    }
}

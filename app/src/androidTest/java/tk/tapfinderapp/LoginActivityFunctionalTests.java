package tk.tapfinderapp;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tk.tapfinderapp.view.login.LoginActivity;
import tk.tapfinderapp.view.login.RegisterActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class LoginActivityFunctionalTests {

    @Rule
    public IntentsTestRule<LoginActivity> rule =
            new IntentsTestRule<>(LoginActivity.class);

    @Test
    public void clickingRegisterShouldStartNewActivity() {
        onView(withId(R.id.sign_up_button)).perform(click());
        intended(hasComponent(RegisterActivity.class.getName()));
    }
}

package tk.tapfinderapp.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import timber.log.Timber;
import tk.tapfinderapp.TapFinderApp;
import tk.tapfinderapp.R;
import tk.tapfinderapp.di.component.ActivityComponent;

public class BaseActivity extends AppCompatActivity implements FragmentChanger {

    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()) {
            ButterKnife.unbind(this);
            activityComponent = null;
        }
    }

    @Override
    public void changeFragment(Fragment fragment) {
        Timber.d("Changing fragment %s and adding to backstack", fragment.toString());
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                        R.anim.slide_in_right, R.anim.slide_out_right)
                .replace(R.id.fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void changeFragmentWithoutBackStack(Fragment fragment) {
        Timber.d("Changing fragment %s", fragment.toString());
        cleanBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    public ActivityComponent activityComponent() {
        if (activityComponent == null) {
            activityComponent = ActivityComponent.Initializer.init(TapFinderApp.component(this));
        }
        return activityComponent;
    }

    private void cleanBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        while (manager.getBackStackEntryCount() > 0){
            manager.popBackStackImmediate();
        }
    }
}

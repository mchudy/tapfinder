package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import timber.log.Timber;
import unnamed.mini.pw.edu.pl.unnamedapp.App;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import unnamed.mini.pw.edu.pl.unnamedapp.di.component.ActivityComponent;

public class BaseActivity extends AppCompatActivity{

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

    public void changeFragment(Fragment fragment) {
        Timber.d("Changing fragment %s", fragment.toString());
        cleanBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    public void changeFragmentWithBackStack(Fragment fragment) {
        Timber.d("Changing fragment %s and adding to backstack", fragment.toString());
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                        R.anim.slide_in_right, R.anim.slide_out_right)
                .replace(R.id.fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    public ActivityComponent activityComponent() {
        if (activityComponent == null) {
            activityComponent = ActivityComponent.Initializer.init(App.component(this));
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

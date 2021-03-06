package tk.tapfinderapp.view;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import tk.tapfinderapp.R;
import tk.tapfinderapp.di.component.ActivityComponent;

public abstract class BaseFragment extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    protected ActivityComponent activityComponent() {
        return ((BaseActivity) getActivity()).activityComponent();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initToolbar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @StringRes
    protected abstract int getTitleResId();

    private void initToolbar() {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if(getFragmentManager().getBackStackEntryCount() > 0) {
                getActivity().onBackPressed();
            } else {
                ((MainActivity) getActivity()).openDrawer();
            }
        });
        final ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(getTitleResId()));
        }
    }
}

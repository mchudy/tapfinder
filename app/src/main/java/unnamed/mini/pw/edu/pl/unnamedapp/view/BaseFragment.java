package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;
import unnamed.mini.pw.edu.pl.unnamedapp.di.ActivityComponent;
import unnamed.mini.pw.edu.pl.unnamedapp.di.FragmentInjector;

public class BaseFragment extends Fragment {

    protected ActivityComponent activityComponent() {
        return ((BaseActivity)getActivity()).activityComponent();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

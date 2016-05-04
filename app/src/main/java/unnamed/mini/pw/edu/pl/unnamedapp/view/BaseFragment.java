package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.support.v4.app.Fragment;

import butterknife.ButterKnife;
import unnamed.mini.pw.edu.pl.unnamedapp.di.component.ActivityComponent;

public class BaseFragment extends Fragment {

    protected ActivityComponent activityComponent() {
        return ((BaseActivity)getActivity()).activityComponent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

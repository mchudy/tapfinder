package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;

public class BaseFragment extends Fragment{

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

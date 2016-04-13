package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;

public class BaseFragment extends android.support.v4.app.Fragment{

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

package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import unnamed.mini.pw.edu.pl.unnamedapp.service.ApiService;

public class FindBeerFragment extends BaseFragment {

    @Inject
    ApiService service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_find_beer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        ButterKnife.bind(this, view);
    }

    @Override
    protected int getTitleResId() {
        return R.string.find_a_beer;
    }
}

package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import unnamed.mini.pw.edu.pl.unnamedapp.R;

/**
 * Created by Grzegorz on 14/04/2016.
 */
public class MyProfileFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }
}

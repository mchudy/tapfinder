package tk.pubfinderapp.view.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f2prateek.rx.preferences.Preference;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import tk.pubfinderapp.di.qualifier.UsernamePreference;
import tk.pubfinderapp.view.BaseActivity;
import tk.pubfinderapp.view.BaseFragment;

public class MyProfileFragment extends BaseFragment {

    @Inject
    @UsernamePreference
    Preference<String> usernamePreference;

    @Bind(R.id.username)
    TextView username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        ButterKnife.bind(this, view);
        username.setText(usernamePreference.get());
    }

    @Override
    protected int getTitleResId() {
        return R.string.my_profile;
    }
}

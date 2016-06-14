package tk.tapfinderapp.view.badges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import tk.tapfinderapp.R;
import tk.tapfinderapp.view.BaseFragment;

public class BadgesFragment extends BaseFragment {

    public static BadgesFragment newInstance() {
        return new BadgesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_badges, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    protected int getTitleResId() {
        return R.string.badges;
    }
}

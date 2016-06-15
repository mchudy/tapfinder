package tk.tapfinderapp.view.badges;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.BadgeDto;
import tk.tapfinderapp.util.EmptyRecyclerView;
import tk.tapfinderapp.view.BaseFragment;

public class BadgesFragment extends BaseFragment {

    private static final String BADGES_KEY = "badgesView";
    private List<BadgeDto> badges;

    @Bind(R.id.badges)
    EmptyRecyclerView badgesView;

    @Bind(R.id.no_results)
    TextView emptyView;

    @Inject
    BadgesAdapter adapter;

    public static BadgesFragment newInstance(List<BadgeDto> badges) {
        BadgesFragment fragment = new BadgesFragment();
        Bundle args = new Bundle();
        args.putParcelable(BADGES_KEY, Parcels.wrap(badges));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        badges = Parcels.unwrap(getArguments().getParcelable(BADGES_KEY));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_badges, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityComponent().inject(this);
        ButterKnife.bind(this, view);
        initAdapter();
    }

    private void initAdapter() {
        badgesView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setItems(badges);
        badgesView.setAdapter(adapter);
        badgesView.setEmptyView(emptyView);
    }

    @Override
    protected int getTitleResId() {
        return R.string.badges;
    }
}

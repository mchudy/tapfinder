package unnamed.mini.pw.edu.pl.unnamedapp.view.place.specialoffers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import unnamed.mini.pw.edu.pl.unnamedapp.view.BaseFragment;

public class AddSpecialOfferFragment extends BaseFragment {

    private String placeId;

    public static AddSpecialOfferFragment newInstance(String placeId){
        AddSpecialOfferFragment fragment = new AddSpecialOfferFragment();
        Bundle args = new Bundle();
        args.putString("placeId", placeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        placeId = args.getString("placeId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_special_offer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected int getTitleResId() {
        return R.string.add_special_offer;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                getActivity().getFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package unnamed.mini.pw.edu.pl.unnamedapp.view.place;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URISyntaxException;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces.PlaceDetails;
import unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces.PlaceDetailsResult;
import unnamed.mini.pw.edu.pl.unnamedapp.service.GoogleMapsApiService;
import unnamed.mini.pw.edu.pl.unnamedapp.view.BaseActivity;

public class PlaceGeneralFragment extends Fragment {

    private String placeId;

    @Bind(R.id.address)
    TextView address;

    @Bind(R.id.phone_number)
    TextView phoneNumber;

    @Bind(R.id.website)
    TextView website;

    @Bind(R.id.phone_image)
    ImageView phoneImage;

    @Bind(R.id.website_image)
    ImageView websiteImage;

    @Inject
    GoogleMapsApiService googleApiService;

    public static PlaceGeneralFragment newInstance(String placeId) {
        PlaceGeneralFragment fragment = new PlaceGeneralFragment();
        Bundle args = new Bundle();
        args.putString("placeId", placeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        placeId = args.getString("placeId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_general, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        ButterKnife.bind(this, view);

        loadData();
    }

    private void loadData() {
        googleApiService.getPlaceDetails(placeId, getString(R.string.google_places_key))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::showPlaceData, t -> Timber.wtf(t.getMessage()));
    }

    private void showPlaceData(PlaceDetailsResult result) {
        PlaceDetails place = result.getResult();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(place.getName());
        }
        phoneNumber.setText(place.getFormattedPhoneNumber());
        address.setText(place.getFormattedAddress());
        website.setText(place.getWebsite().toString());

        phoneImage.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + place.getFormattedPhoneNumber()));
            dialIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialIntent);
        });

        websiteImage.setOnClickListener(v -> {
            if (place.getWebsite() != null) {
                Intent browserIntent = null;
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(place.getWebsite().toString()));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

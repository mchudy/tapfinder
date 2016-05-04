package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;
import unnamed.mini.pw.edu.pl.unnamedapp.R;

public class PlaceDetailsFragment extends Fragment {

    public static final String PLACE_ID_KEY = "placeId";

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

    private String placeId;

    public PlaceDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_place_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        placeId = bundle.getString(PLACE_ID_KEY);
        Timber.d(placeId);
//       ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(place.getName());
//        phoneNumber.setText(place.getPhoneNumber());
//        address.setText(place.getAddress());
//        website.setText(place.getWebsiteUri().toString());
//
//        phoneImage.setOnClickListener(v -> {
//            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + place.getPhoneNumber()));
//            dialIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(dialIntent);
//        });
//
//        websiteImage.setOnClickListener(v -> {
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, place.getWebsiteUri());
//            startActivity(browserIntent);
//        });
    }
}

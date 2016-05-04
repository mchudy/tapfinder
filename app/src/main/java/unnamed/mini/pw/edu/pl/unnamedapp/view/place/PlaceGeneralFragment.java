package unnamed.mini.pw.edu.pl.unnamedapp.view.place;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import unnamed.mini.pw.edu.pl.unnamedapp.view.BaseActivity;

public class PlaceGeneralFragment extends Fragment {

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

        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(place.getName());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

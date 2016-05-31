package tk.tapfinderapp.view.place.general;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.CommentDto;
import tk.tapfinderapp.model.googleplaces.PlaceDetails;
import tk.tapfinderapp.model.googleplaces.PlaceDetailsResult;
import tk.tapfinderapp.service.GoogleMapsApiService;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.util.DividerItemDecoration;
import tk.tapfinderapp.util.KeyboardUtils;
import tk.tapfinderapp.view.BaseActivity;

public class PlaceGeneralFragment extends Fragment {

    private String placeId;
    private CommentsAdapter commentsAdapter;

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

    @Bind(R.id.comments)
    RecyclerView comments;

    @Bind(R.id.new_comment_text)
    EditText newCommentText;

    @Inject
    GoogleMapsApiService googleApiService;

    @Inject
    TapFinderApiService tapFinderService;

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
        setupComments();
    }

    @OnClick(R.id.post_comment)
    public void postComment() {
        CommentDto dto = new CommentDto();
        dto.setText(newCommentText.getText().toString());
        dto.setPlaceId(placeId);
        tapFinderService.postComment(dto)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(b -> loadComments(), e -> Timber.wtf(e.getMessage()));
        newCommentText.setText("");
        KeyboardUtils.hideSoftKeyboard(getActivity());
    }

    private void setupComments() {
        commentsAdapter = new CommentsAdapter(getContext());
        comments.setLayoutManager(new LinearLayoutManager(getActivity()));
        comments.setAdapter(commentsAdapter);
        comments.addItemDecoration(new DividerItemDecoration(getContext()));
        loadComments();
    }

    private void loadComments() {
        tapFinderService.getComments(placeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(comments -> {
                    commentsAdapter.setComments(comments);
                    commentsAdapter.notifyDataSetChanged();
                },
                e -> Timber.wtf(e.getMessage()));
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
        if(place.getWebsite() != null) {
            website.setText(place.getWebsite().toString());
        }
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

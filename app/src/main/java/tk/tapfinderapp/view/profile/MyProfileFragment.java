package tk.tapfinderapp.view.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.f2prateek.rx.preferences.Preference;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.Constants;
import tk.tapfinderapp.R;
import tk.tapfinderapp.di.qualifier.UserImagePreference;
import tk.tapfinderapp.di.qualifier.UsernamePreference;
import tk.tapfinderapp.event.UserImageChangedEvent;
import tk.tapfinderapp.model.user.UserImageDto;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.view.BaseFragment;

public class MyProfileFragment extends BaseFragment {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Uri selectedImage;

    @Inject
    @UsernamePreference
    Preference<String> usernamePreference;

    @Inject
    @UserImagePreference
    Preference<String> userImagePreference;

    @Inject
    TapFinderApiService apiService;

    @Bind(R.id.username)
    TextView username;

    @Bind(R.id.profile_image)
    CircleImageView profileImage;

    @Inject
    EventBus eventBus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityComponent().inject(this);
        ButterKnife.bind(this, view);
        username.setText(usernamePreference.get());
        loadImage();
    }

    @OnClick(R.id.profile_image)
    public void onImageClick() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            selectedImage = data.getData();
            if (verifyStoragePermissions(getActivity())) {
                changeImage();
            }
        }
    }

    private void changeImage() {
        String encodedImage = encodeImage();
        UserImageDto image = new UserImageDto(encodedImage);
        apiService.changeImage(image)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(userDto -> {
                            userImagePreference.set(userDto.getImagePath());
                            loadImage();
                            eventBus.post(new UserImageChangedEvent());
                            Toast.makeText(getActivity(), R.string.profile_image_changed, Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            Toast.makeText(getActivity(), R.string.error_changing_profile_image, Toast.LENGTH_SHORT).show();
                            Timber.wtf(throwable, "Changing profile image");
                        });
    }

    private void loadImage() {
        Picasso.with(getActivity())
                .load(Constants.API_BASE_URI + userImagePreference.get())
                .placeholder(R.drawable.ic_person_white_48dp)
                .into(profileImage);
    }

    private String encodeImage() {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContext().getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Bitmap bm = BitmapFactory.decodeFile(picturePath);
        float x = bm.getWidth();
        bm = Bitmap.createScaledBitmap(bm, 500, (int) (bm.getHeight() / (x / 500)), false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    private boolean verifyStoragePermissions(Activity activity) {
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                changeImage();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected int getTitleResId() {
        return R.string.my_profile;
    }
}

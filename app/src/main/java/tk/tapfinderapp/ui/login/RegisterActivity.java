package tk.tapfinderapp.ui.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.user.UserRegisterDto;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.ui.base.BaseActivity;

public class RegisterActivity extends BaseActivity {

    private ProgressDialog progress;

    @Bind(R.id.input_username)
    TextView usernameView;

    @Bind(R.id.input_email)
    TextView emailView;

    @Bind(R.id.input_password)
    TextView passwordView;

    @Bind(R.id.username_layout)
    TextInputLayout usernameLayout;

    @Bind(R.id.email_layout)
    TextInputLayout emailLayout;

    @Bind(R.id.password_layout)
    TextInputLayout passwordLayout;

    @Inject
    TapFinderApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_register);
    }

    @OnClick(R.id.sign_up_button)
    public void signUp() {
        String username = usernameView.getText().toString();
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        tryRegister(username, email, password);
    }

    private void tryRegister(String username, String email, String password) {
        boolean valid = true;

        usernameLayout.setError(null);
        passwordLayout.setError(null);
        emailLayout.setError(null);

        valid = validateInput(username, email, password, valid);

        if (!valid) {
            return;
        }

        apiService.isUsernameAvailable(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(r -> {
                            if (r.code() == 200) {
                                usernameLayout.setError(getString(R.string.username_taken));
                            }
                        }
                )
                .flatMap(r -> apiService.isEmailAvailable(email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(r1 -> Pair.create(r, r1))
                )
                .subscribe(r -> {
                    if (r.second.code() == 200) {
                        emailLayout.setError(getString(R.string.error_email_in_use));
                    } else if (r.second.code() == 404 && r.first.code() == 404) {
                        registerUser(username, email, password);
                    }
                });
    }

    private boolean validateInput(String username, String email, String password, boolean valid) {
        if (username.isEmpty()) {
            usernameLayout.setError(getString(R.string.error_field_required));
            valid = false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(getString(R.string.error_invalid_email));
            valid = false;
        }
        if (password.isEmpty() || password.length() < 6) {
            passwordLayout.setError(getString(R.string.error_too_short_password));
            valid = false;
        }
        return valid;
    }

    private void registerUser(String username, String email, String password) {
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.please_wait));
        progress.show();
        UserRegisterDto dto = new UserRegisterDto(username, email, password);
        apiService.register(dto)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(r -> finish(), this::onRegisterError,
                        () -> progress.dismiss());
    }

    private void onRegisterError(Throwable throwable) {
        Toast.makeText(this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
        progress.dismiss();
    }

    @OnFocusChange(R.id.input_email)
    public void onEmailFocusChange(){
        emailLayout.setError(null);

        String email = emailView.getText().toString();
        if(TextUtils.isEmpty(email)) return;
        apiService.isEmailAvailable(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(r -> {
                    if(r.code() == 200)
                        emailLayout.setError(getString(R.string.error_email_in_use));
                });
    }

    @OnFocusChange(R.id.input_username)
    public void onUsernameFocusChange() {
        usernameLayout.setError(null);

        String username = usernameView.getText().toString();
        if(TextUtils.isEmpty(username)) return;
        apiService.isUsernameAvailable(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(r -> {
                    if(r.code() == 200) {
                        usernameLayout.setError(getString(R.string.username_taken));
                    }
                });
    }
}

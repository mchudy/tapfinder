package tk.pubfinderapp.view.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.f2prateek.rx.preferences.Preference;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import tk.pubfinderapp.di.qualifier.AccessTokenPreference;
import tk.pubfinderapp.di.qualifier.UsernamePreference;
import tk.pubfinderapp.model.AccessTokenModel;
import tk.pubfinderapp.model.UserRegisterExternalDto;
import tk.pubfinderapp.service.ApiService;
import tk.pubfinderapp.view.BaseActivity;
import tk.pubfinderapp.view.MainActivity;

public class LoginActivity extends BaseActivity {

    private static final String GRANT_TYPE = "password";
    private ProgressDialog progress;
    private CallbackManager callbackManager;

    @Bind(R.id.login)
    AutoCompleteTextView usernameView;

    @Bind(R.id.password)
    EditText passwordView;

    @Bind(R.id.username_layout)
    TextInputLayout usernameInputLayout;

    @Bind(R.id.password_layout)
    TextInputLayout passwordLayout;

    @Bind(R.id.facebook_login_button)
    LoginButton facebookLoginButton;

    @Inject
    @UsernamePreference
    Preference<String> usernamePreference;

    @Inject
    @AccessTokenPreference
    Preference<String> accessTokenPreference;

    @Inject
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        setContentView(R.layout.activity_login);
        String username = usernamePreference.get();
        if (!TextUtils.isEmpty(username)) {
            startMainActivity();
        }
        setupFacebookLogin();
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {
        if (validateInput()) {
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.please_wait));
            progress.show();
            apiService.getToken(usernameView.getText().toString(), passwordView.getText().toString(), GRANT_TYPE)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::addToken, this::onGetTokenError, this::onGetTokenCompleted);
        }
    }

    private void onGetTokenCompleted() {
        progress.dismiss();
        startMainActivity();
    }

    private void onGetTokenError(Throwable throwable) {
        Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
        progress.dismiss();
    }

    private void addToken(AccessTokenModel model) {
        String token = model.getAccessToken();
        if (!TextUtils.isEmpty(token)) {
            accessTokenPreference.set(token);
            usernamePreference.set(model.getUserName());
        } else {
            Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progress = new ProgressDialog(LoginActivity.this);
                progress.setMessage(getString(R.string.please_wait));
                progress.show();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {
                            JSONObject jsonObject = response.getJSONObject();
                            try {
                                //TODO: get username from user?
                                String username = jsonObject.getString("name").replaceAll("\\s*", "");
                                UserRegisterExternalDto dto = new UserRegisterExternalDto(
                                        username,
                                        "Facebook",
                                        loginResult.getAccessToken().getToken(),
                                        jsonObject.getString("email"));
                                tryLoginExternal(dto);
                            } catch (JSONException e) {
                                Timber.wtf(e.getMessage());
                                progress.dismiss();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Timber.i("Facebook login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Timber.wtf(error.getMessage());
                Toast.makeText(LoginActivity.this, "Could not login via Facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tryLoginExternal(UserRegisterExternalDto dto) {
        apiService.getTokenFromExternal("Facebook", dto.getExternalAccessToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                   if(result.code() == 200){
                       progress.dismiss();
                       addToken(result.body());
                       startMainActivity();
                   }
                   if(result.code() == 400) {
                       registerExternal(dto);
                   }
                }, e -> {
                    Timber.wtf(e.getMessage());
                    progress.dismiss();
                });
    }

    private void registerExternal(UserRegisterExternalDto dto) {
        apiService.registerExternal(dto)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    progress.dismiss();
                    if (result.code() == 200) {
                        Toast.makeText(LoginActivity.this, "Successfully registered with Facebook", Toast.LENGTH_SHORT).show();
                        addToken(result.body());
                        startMainActivity();
                    } else if(result.code() == 400) {
                        Timber.wtf(result.body().toString());
                    }
                }, e -> {
                    Timber.wtf("Could not register via Facebook");
                    progress.dismiss();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.sign_up_button)
    public void signUp() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean validateInput() {
        usernameInputLayout.setError(null);
        passwordLayout.setError(null);

        String email = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            usernameInputLayout.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        return !cancel;
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}


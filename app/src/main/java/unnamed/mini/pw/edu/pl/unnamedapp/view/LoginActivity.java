package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.SharedPreferences;
import android.database.Cursor;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.f2prateek.rx.preferences.Preference;

import org.w3c.dom.Text;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import unnamed.mini.pw.edu.pl.unnamedapp.App;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import unnamed.mini.pw.edu.pl.unnamedapp.di.qualifier.AccessTokenPreference;
import unnamed.mini.pw.edu.pl.unnamedapp.di.qualifier.UsernamePreference;
import unnamed.mini.pw.edu.pl.unnamedapp.model.AccessTokenModel;
import unnamed.mini.pw.edu.pl.unnamedapp.service.ApiService;

public class LoginActivity extends BaseActivity {

    private static final String GRANT_TYPE = "password";
    private ProgressDialog progress;

    @Bind(R.id.login)
    AutoCompleteTextView usernameView;

    @Bind(R.id.password)
    EditText passwordView;

    @Bind(R.id.username_layout)
    TextInputLayout usernameInputLayout;

    @Bind(R.id.password_layout)
    TextInputLayout passwordLayout;

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
        if(!TextUtils.isEmpty(username)){
            startMainActivity();
        }
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {
        if(validateInput()) {
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
        if(!TextUtils.isEmpty(token)) {
            accessTokenPreference.set(token);
            usernamePreference.set(usernameView.getText().toString());
        } else {
            Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
        }
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


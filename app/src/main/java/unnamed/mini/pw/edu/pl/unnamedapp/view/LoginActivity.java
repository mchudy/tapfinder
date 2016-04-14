package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.content.Intent;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.SharedPreferences;
import android.database.Cursor;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.OnClick;
import unnamed.mini.pw.edu.pl.unnamedapp.R;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.login)
    AutoCompleteTextView usernameView;

    @Bind(R.id.password)
    EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String username = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("username", null);
        if(!TextUtils.isEmpty(username)){
            startMainActivity();
        }
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {
        attemptLogin();
    }

    private void attemptLogin() {
        usernameView.setError(null);
        passwordView.setError(null);

        String email = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString("username", usernameView.getText().toString())
                    .commit();
            startMainActivity();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}


package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import unnamed.mini.pw.edu.pl.unnamedapp.model.UserRegisterDto;
import unnamed.mini.pw.edu.pl.unnamedapp.service.ApiService;

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
    ApiService apiService;

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

        if(validateInput(username, email, password)) {
            //TODO: check email and username availability before
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.please_wait));
            progress.show();
            UserRegisterDto dto = new UserRegisterDto(username, email, password);
            apiService.register(dto)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::onRegistered, this::onRegisterError,
                            () -> progress.dismiss());
        }
    }

    private void onRegisterError(Throwable throwable) {
        Toast.makeText(this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
        progress.dismiss();
    }

    private void onRegistered(ResponseBody responseBody) {
        finish();
    }

    private boolean validateInput(String username, String email, String password) {
        boolean valid = true;

        usernameLayout.setError(null);
        passwordLayout.setError(null);
        emailLayout.setError(null);

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
}

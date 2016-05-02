package unnamed.mini.pw.edu.pl.unnamedapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import unnamed.mini.pw.edu.pl.unnamedapp.R;
import unnamed.mini.pw.edu.pl.unnamedapp.service.ApiService;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.input_username)
    TextView usernameView;

    @Bind(R.id.input_email)
    TextView emailView;

    @Bind(R.id.input_password)
    TextView passwordView;

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

    }
}

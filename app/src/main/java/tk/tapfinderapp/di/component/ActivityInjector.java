package tk.tapfinderapp.di.component;

import tk.tapfinderapp.ui.login.LoginActivity;
import tk.tapfinderapp.ui.main.MainActivity;
import tk.tapfinderapp.ui.login.RegisterActivity;

public interface ActivityInjector {
    void inject(LoginActivity loginActivity);
    void inject(MainActivity mainActivity);
    void inject(RegisterActivity registerActivity);
}

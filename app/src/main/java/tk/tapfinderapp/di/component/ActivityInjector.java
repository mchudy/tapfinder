package tk.tapfinderapp.di.component;

import tk.tapfinderapp.view.login.LoginActivity;
import tk.tapfinderapp.view.MainActivity;
import tk.tapfinderapp.view.login.RegisterActivity;

public interface ActivityInjector {
    void inject(LoginActivity loginActivity);
    void inject(MainActivity mainActivity);
    void inject(RegisterActivity registerActivity);
}

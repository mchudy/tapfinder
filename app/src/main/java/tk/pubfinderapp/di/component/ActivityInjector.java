package tk.pubfinderapp.di.component;

import tk.pubfinderapp.view.login.LoginActivity;
import tk.pubfinderapp.view.MainActivity;
import tk.pubfinderapp.view.login.RegisterActivity;

public interface ActivityInjector {
    void inject(LoginActivity loginActivity);
    void inject(MainActivity mainActivity);
    void inject(RegisterActivity registerActivity);
}

package unnamed.mini.pw.edu.pl.unnamedapp.di.component;

import unnamed.mini.pw.edu.pl.unnamedapp.view.login.LoginActivity;
import unnamed.mini.pw.edu.pl.unnamedapp.view.MainActivity;
import unnamed.mini.pw.edu.pl.unnamedapp.view.login.RegisterActivity;

public interface ActivityInjector {
    void inject(LoginActivity loginActivity);
    void inject(MainActivity mainActivity);
    void inject(RegisterActivity registerActivity);
}

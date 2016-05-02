package unnamed.mini.pw.edu.pl.unnamedapp.di;

import unnamed.mini.pw.edu.pl.unnamedapp.view.LoginActivity;
import unnamed.mini.pw.edu.pl.unnamedapp.view.MainActivity;
import unnamed.mini.pw.edu.pl.unnamedapp.view.RegisterActivity;

public interface ActivityInjector {
    void inject(LoginActivity loginActivity);
    void inject(MainActivity mainActivity);
    void inject(RegisterActivity registerActivity);
}

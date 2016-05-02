package unnamed.mini.pw.edu.pl.unnamedapp.di;

import unnamed.mini.pw.edu.pl.unnamedapp.view.LoginActivity;
import unnamed.mini.pw.edu.pl.unnamedapp.view.MainActivity;

public interface ActivityInjector {
    void inject(LoginActivity loginActivity);
    void inject(MainActivity mainActivity);
}

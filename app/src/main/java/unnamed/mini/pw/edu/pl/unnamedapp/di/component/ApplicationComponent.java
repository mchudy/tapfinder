package unnamed.mini.pw.edu.pl.unnamedapp.di.component;


import javax.inject.Singleton;

import dagger.Component;
import unnamed.mini.pw.edu.pl.unnamedapp.App;
import unnamed.mini.pw.edu.pl.unnamedapp.di.module.ApiServiceModule;
import unnamed.mini.pw.edu.pl.unnamedapp.di.module.PreferencesModule;

@Singleton
@Component(modules = {ApiServiceModule.class, PreferencesModule.class})
public interface ApplicationComponent {

    class Initializer {
        public static ApplicationComponent initialize(App app) {
            return DaggerApplicationComponent
                    .builder()
                    .preferencesModule(new PreferencesModule(app.getApplicationContext()))
                    .apiServiceModule(new ApiServiceModule())
                    .build();
        }
    }

    void inject(App app);
    ActivityComponent activityComponent();
}

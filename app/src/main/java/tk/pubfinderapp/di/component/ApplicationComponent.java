package tk.pubfinderapp.di.component;


import javax.inject.Singleton;

import dagger.Component;
import tk.pubfinderapp.App;
import tk.pubfinderapp.di.module.ApiServiceModule;
import tk.pubfinderapp.di.module.PreferencesModule;

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

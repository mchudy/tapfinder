package tk.tapfinderapp.di.component;


import javax.inject.Singleton;

import dagger.Component;
import tk.tapfinderapp.TapFinderApp;
import tk.tapfinderapp.di.module.ApiServiceModule;
import tk.tapfinderapp.di.module.EventBusModule;
import tk.tapfinderapp.di.module.PreferencesModule;

@Singleton
@Component(modules = {
        ApiServiceModule.class,
        PreferencesModule.class,
        EventBusModule.class
})
public interface ApplicationComponent {

    class Initializer {
        public static ApplicationComponent initialize(TapFinderApp app) {
            return DaggerApplicationComponent
                    .builder()
                    .preferencesModule(new PreferencesModule(app.getApplicationContext()))
                    .apiServiceModule(new ApiServiceModule())
                    .eventBusModule(new EventBusModule())
                    .build();
        }
    }

    void inject(TapFinderApp app);
    ActivityComponent activityComponent();
}

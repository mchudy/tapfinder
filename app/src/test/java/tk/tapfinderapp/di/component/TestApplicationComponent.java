package tk.tapfinderapp.di.component;

import javax.inject.Singleton;

import dagger.Component;
import tk.tapfinderapp.TapFinderApp;
import tk.tapfinderapp.TestTapFinderApp;
import tk.tapfinderapp.di.module.EventBusModule;
import tk.tapfinderapp.di.module.PreferencesModule;
import tk.tapfinderapp.di.module.TestApiModule;

@Singleton
@Component(modules = {
        TestApiModule.class,
        PreferencesModule.class,
        EventBusModule.class
})
public interface TestApplicationComponent extends ApplicationComponent{

    class Initializer {
        public static ApplicationComponent initialize(TapFinderApp app) {
            return DaggerApplicationComponent
                    .builder()
                    .preferencesModule(new PreferencesModule(app.getApplicationContext()))
                    .apiServiceModule(new TestApiModule())
                    .eventBusModule(new EventBusModule())
                    .build();
        }
    }

    void inject(TestTapFinderApp app);
    ActivityComponent activityComponent();
}

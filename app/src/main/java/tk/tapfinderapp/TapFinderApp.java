package tk.tapfinderapp;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import timber.log.Timber;
import tk.tapfinderapp.di.component.ApplicationComponent;

public class TapFinderApp extends Application {

    ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = ApplicationComponent.Initializer.initialize(this);
        component.inject(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static ApplicationComponent component(Context context) {
        return ((TapFinderApp)context.getApplicationContext()).component;
    }
}

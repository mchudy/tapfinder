package tk.pubfinderapp.di.module;

import android.content.Context;
import static tk.pubfinderapp.Constants.*;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import dagger.Module;
import dagger.Provides;
import tk.pubfinderapp.di.qualifier.AccessTokenPreference;
import tk.pubfinderapp.di.qualifier.UsernamePreference;

@Module
public class PreferencesModule {

    private Context context;

    public PreferencesModule(Context context) {
        this.context = context;
    }

    @Provides
    RxSharedPreferences provideRxSharedPreferences() {
        return RxSharedPreferences.create(context.getSharedPreferences(SHARED_PREFERENCES,
                Context.MODE_PRIVATE));
    }

    @Provides
    @UsernamePreference
    Preference<String> provideUsernamePreference(RxSharedPreferences preferences) {
        return preferences.getString(USERNAME_PREFERENCE);
    }

    @Provides
    @AccessTokenPreference
    Preference<String> provideAccessTokenPreference(RxSharedPreferences preferences) {
        return preferences.getString(ACCESS_TOKEN_PREFERENCE);
    }
}

package unnamed.mini.pw.edu.pl.unnamedapp.di.module;

import android.content.Context;
import static unnamed.mini.pw.edu.pl.unnamedapp.Constants.*;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import dagger.Module;
import dagger.Provides;
import unnamed.mini.pw.edu.pl.unnamedapp.di.qualifier.AccessTokenPreference;
import unnamed.mini.pw.edu.pl.unnamedapp.di.qualifier.UsernamePreference;

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

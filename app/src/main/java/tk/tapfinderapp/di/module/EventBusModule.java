package tk.tapfinderapp.di.module;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class EventBusModule {

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return new EventBus();
    }
}

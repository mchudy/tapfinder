package tk.tapfinderapp.di.component;

import dagger.Subcomponent;
import tk.tapfinderapp.di.scope.ActivityScope;

@ActivityScope
@Subcomponent
public interface ActivityComponent extends ActivityInjector, FragmentInjector {

    class Initializer {
        public static ActivityComponent init(ApplicationComponent component){
            return component.activityComponent();
        }
    }
}

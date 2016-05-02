package unnamed.mini.pw.edu.pl.unnamedapp.di;

import dagger.Subcomponent;
import unnamed.mini.pw.edu.pl.unnamedapp.di.scope.ActivityScope;

@ActivityScope
@Subcomponent
public interface ActivityComponent extends ActivityInjector, FragmentInjector{

    class Initializer {
        public static ActivityComponent init(ApplicationComponent component){
            return component.activityComponent();
        }
    }
}

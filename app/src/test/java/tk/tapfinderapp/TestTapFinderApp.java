package tk.tapfinderapp;

import tk.tapfinderapp.di.component.TestApplicationComponent;

public class TestTapFinderApp extends TapFinderApp{

    @Override
    public void onCreate() {
        super.onCreate();
        component = TestApplicationComponent.Initializer.initialize(this);
        component.inject(this);
    }
}

package tk.tapfinderapp.ui.base;

public interface Presenter<V> {

    void attachView(V view);
    void detachView();
}
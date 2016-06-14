package tk.tapfinderapp.view;

public interface Presenter<V> {

    void attachView(V view);
    void detachView();
}
package tk.tapfinderapp.ui.base;

public class BasePresenter<T> implements Presenter<T> {

    protected T view;

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}

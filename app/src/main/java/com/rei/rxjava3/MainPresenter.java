package com.rei.rxjava3;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MainPresenter {
    private View view;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private PublishSubject<String> observeQuery;

    MainPresenter(View view) {
        this.view = view;
        observeQuery = PublishSubject.create();
        disposables.add(
                observeQuery.debounce(300, TimeUnit.MILLISECONDS).
                        filter(s -> s.length() > 3 || s.length() == 0).
                        distinctUntilChanged().
                        switchMap(s -> dummyApiCall(s)
                        ).
                        subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        subscribe(s -> {
                                    view.hideLoading();
                                    view.showResult(s);
                                }
                        ));
    }

    void searchQuery(String query) {
        observeQuery.onNext(query);
    }

    void dispose() {
        disposables.clear();
    }

    void submitAction(String param) {
        view.showLoading();
        Observable<String> call = dummyApiCall(param).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread());
        disposables.add(call.subscribe(s -> {
                    view.hideLoading();
                    view.showResult(s);
                }
        ));
    }

    Observable<String> dummyApiCall(String query) {
        return Observable.just(true).delay(3, TimeUnit.SECONDS).map(aBoolean -> {
            return query;
        });
    }

    interface View {
        void showResult(String result);

        void showLoading();

        void hideLoading();
    }
}

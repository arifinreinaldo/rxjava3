package com.rei.rxjava3;

import com.google.gson.Gson;
import com.rei.rxjava3.repo.Repository;
import com.rei.rxjava3.repo.response.ResponseError;
import com.rei.rxjava3.repo.response.ResponseItem;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import retrofit2.HttpException;

public class MainPresenter {
    private View view;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private PublishSubject<String> observeQuery;
    private Repository repo;
    private Gson gson;

    MainPresenter(View view, Repository repo) {
        this.repo = repo;
        this.view = view;
        gson = new Gson();
        observeQuery = PublishSubject.create();
        disposables.add(
                observeQuery.debounce(300, TimeUnit.MILLISECONDS).
                        filter(s -> s.length() > 3 || s.length() == 0).
                        distinctUntilChanged().
                        switchMap(s -> dummyApiCall(s)).
                        subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        subscribe(s -> {
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
        Observable<List<ResponseItem>> call = repo.getPerName(param).
                subscribeOn(Schedulers.io()).
                doOnSubscribe(disposable -> view.showLoading()).
                observeOn(AndroidSchedulers.mainThread()).
                doFinally(() -> view.hideLoading());

        disposables.add(call.subscribe(s -> {
                    view.hideLoading();
                    view.showResult(s.get(0).getName() + " " + s.get(0).getLevel());
                }, throwable -> parseError(throwable)
        ));
    }

    Observable<String> dummyApiCall(String query) {
        return Observable.just(true).
                delay(1, TimeUnit.SECONDS).
                map(aBoolean -> {
                    return "Result " + query;
                });
    }

    private void parseError(Throwable err) throws IOException {
        String errMessage = "";
        if (err instanceof HttpException) {
            errMessage = gson.fromJson(((HttpException) err).response().errorBody().string(), ResponseError.class).errorMsg;
        } else {
            errMessage = err.getMessage();
        }
        view.showError(errMessage);
    }

    interface View {
        void showResult(String result);

        void showLoading();

        void hideLoading();

        void showError(String msg);
    }
}

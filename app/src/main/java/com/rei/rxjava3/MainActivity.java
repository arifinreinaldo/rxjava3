package com.rei.rxjava3;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {
    private Button debounce;
    String TAG = "MAIN";
    private TextView editText;
    private SearchView searchView;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        debounce = findViewById(R.id.debounce);
        editText = findViewById(R.id.editText);
        searchView = findViewById(R.id.searchView);
        presenter = new MainPresenter(this);

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.searchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.searchQuery(newText);
                return false;
            }
        });
        debounce.setOnClickListener(view -> {
            presenter.submitAction("tes");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dispose();
    }

    @Override
    public void showResult(String result) {
        editText.setText(result);
    }

    @Override
    public void showLoading() {
        Toast.makeText(getBaseContext(), "Loading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {

        Toast.makeText(getBaseContext(), "Done Loading", Toast.LENGTH_SHORT).show();
    }
}
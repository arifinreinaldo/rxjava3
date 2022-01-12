package com.rei.rxjava3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {
    private Button submit;
    String TAG = "MAIN";
    private TextView editText;
    private SearchView searchView;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submit = findViewById(R.id.debounce);
        editText = findViewById(R.id.editText);
        searchView = findViewById(R.id.searchView);
        presenter = new MainPresenter(this, ((MainApplication) this.getApplication()).getRepo());
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
        submit.setOnClickListener(view -> {
            presenter.submitAction(searchView.getQuery().toString());
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

    @Override
    public void showError(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
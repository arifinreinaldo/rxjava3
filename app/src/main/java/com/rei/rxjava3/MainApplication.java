package com.rei.rxjava3;

import android.app.Application;

import com.rei.rxjava3.repo.ApiClient;
import com.rei.rxjava3.repo.Repository;

public class MainApplication extends Application {
    Repository repo;

    public Repository getRepo() {
        if (repo != null) {
            return repo;
        } else {
            repo = ApiClient.getClient().create(Repository.class);
            return repo;
        }
    }
}

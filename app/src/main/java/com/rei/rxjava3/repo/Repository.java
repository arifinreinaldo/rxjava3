package com.rei.rxjava3.repo;

import com.rei.rxjava3.repo.response.ResponseItem;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Repository {
    @GET("digimon/")
    Observable<List<ResponseItem>> getList();

    @GET("digimon/name/{name}")
    Observable<List<ResponseItem>> getPerName(@Path("name") String name);
}

package com.edward.directory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.edward.edward.rxjava2tutorial.R;
import com.edward.edward.Rxjava2Demo.Rxjava2Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DirectoryActivity extends AppCompatActivity {
    @BindView(R.id.directory_list)
    RecyclerView directory_list;
    @BindView(R.id.directory_demo)
    ImageView directory_demo;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        ButterKnife.bind(this);
        directory_list.setLayoutManager(new LinearLayoutManager(this));
        final DirectoryAdapter adapter = new DirectoryAdapter();
        directory_list.setAdapter(adapter);
        mDisposable = Observable
                .fromCallable(() -> new BufferedReader(new InputStreamReader(getAssets().open("directory.json"))))
                .map((Function<BufferedReader, List<DirectoryBean>>) bufferedReader -> new Gson().fromJson(bufferedReader, new TypeToken<ArrayList<DirectoryBean>>() {
                }.getType()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::notifyDataSetChanged);
    }

    @OnClick(R.id.directory_demo)
    public void onDemoClick() {
        startActivity(new Intent(this, Rxjava2Activity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mDisposable.isDisposed()) mDisposable.dispose();
    }
}

package com.cn.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;


public class MainActivity extends RxAppCompatActivity {

    private TextView mTvJd;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvJd = findViewById(R.id.tv_jd);
        ExecutorServices.init();
        init();
        mTvJd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, JumpActivity.class));
            }
        });
    }

    private void init() {
        sendData(1);


    }

    private void sendData(final int num) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 30000; i++) {
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                    }
                    emitter.onNext(i);
                }
            }
        })
                .compose(ExecutorServices.applyGlobalSchedulers(getLifecycleTransformer()))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (o instanceof Integer) {
                            Log.e("TAG", o + "");
                        }
                    }
                }).isDisposed();
    }

    public <T> LifecycleTransformer<T> getLifecycleTransformer() {
        return bindUntilEvent(ActivityEvent.DESTROY);
    }
}
package com.cn.myapplication;

import android.os.Bundle;
import android.util.Log;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public class JumpActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
        for (int i = 0; i < 300; i++) {
            sendData(i);
        }
    }

    private void sendData(final int num) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                }
                emitter.onNext(num);
            }
        })
                .compose(ExecutorServices.applyGlobalSchedulers(getLifecycleTransformer()))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (o instanceof Integer) {
                            Log.e("TAG", o + "_lalala");
                        }
                    }
                }).isDisposed();
    }

    public <T> LifecycleTransformer<T> getLifecycleTransformer() {
        return bindUntilEvent(ActivityEvent.DESTROY);
    }
}

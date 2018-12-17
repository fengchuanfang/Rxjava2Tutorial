package com.edward.rxjava.Rxjava2Demo;

import com.edward.javaecho.SystemOut;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * 功能描述：Rxjava2入门教程六：Single、Completable、Maybe——简化版的Observable
 *
 * @author (作者) edward（冯丰枫）
 * @link http://www.jianshu.com/u/f7176d6d53d2
 * 创建时间： 2017/9/3
 */
public class Rxjava2_6_Single_Completable_Maybe {

    public void singleDemo1() {
        Single
                .create(new SingleOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(SingleEmitter<Integer> e) throws Exception {
                        e.onSuccess(0);
                    }
                })
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        SystemOut.println(integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SystemOut.println(e);
                    }
                });
    }

    public void singleDemo2() {
        Single
                .create(new SingleOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(SingleEmitter<Integer> e) throws Exception {
                        e.onError(new Exception("测试异常"));
                    }
                })
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        SystemOut.println(integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SystemOut.println(e);
                    }
                });
    }


    public void completableDemo1() {
        Completable
                .create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(CompletableEmitter e) throws Exception {
                        e.onComplete();
                    }
                })
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        SystemOut.println("执行完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        SystemOut.println(e);
                    }
                });
    }

    public void completableDemo2() {
        Completable
                .create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(CompletableEmitter e) throws Exception {
                        e.onError(new Exception("测试异常"));
                    }
                })
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        SystemOut.println("执行完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        SystemOut.println(e);
                    }
                });
    }

    public void maybeDemo1() {
        Maybe
                .create(new MaybeOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(MaybeEmitter<Integer> e) throws Exception {
                        e.onSuccess(1);
                        e.onComplete();
                    }
                })
                .subscribe(new MaybeObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        SystemOut.println(integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SystemOut.println(e);
                    }

                    @Override
                    public void onComplete() {
                        SystemOut.println("执行完成");
                    }
                });
    }

    public void maybeDemo2() {
        Maybe
                .create(new MaybeOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(MaybeEmitter<Integer> e) throws Exception {
                        e.onSuccess(1);
                        e.onError(new Exception("测试异常"));
                    }
                })
                .subscribe(new MaybeObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        SystemOut.println(integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SystemOut.println(e);
                    }

                    @Override
                    public void onComplete() {
                        SystemOut.println("执行完成");
                    }
                });
    }

}

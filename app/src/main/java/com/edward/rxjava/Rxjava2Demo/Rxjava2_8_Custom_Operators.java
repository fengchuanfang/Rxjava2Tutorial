package com.edward.rxjava.Rxjava2Demo;

import com.edward.javaecho.SystemOut;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOperator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 功能描述：Rxjava2入门教程八：自定义操作符
 *
 * @author (作者) edward（冯丰枫）
 * @link http://www.jianshu.com/u/f7176d6d53d2
 * 创建时间： 2017/9/3
 */
public class Rxjava2_8_Custom_Operators {
    private Disposable mDisposable;

    public void demo1() {
        Observable
                .just(1, 2, 3, 4)
                .lift(new SQOperator())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        SystemOut.println(integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SystemOut.println(e);
                    }

                    @Override
                    public void onComplete() {
                        SystemOut.println("完成");
                    }
                });
    }

    public void demo2() {
        Observable.just("Hello", "Rxjava")
                .lift(new ToCharOperator())
                .map(new Function<Character, String>() {
                    @Override
                    public String apply(Character character) throws Exception {
                        SystemOut.println(character);
                        return character.toString();
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        SystemOut.println(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SystemOut.println(e);
                    }

                    @Override
                    public void onComplete() {
                        SystemOut.println("完成");
                    }
                });
    }

    public void demo3() {
        Observable.just(1, 2, 3, 4)
                .compose(new SQTransFormer())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        SystemOut.println(integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SystemOut.println(e);
                    }

                    @Override
                    public void onComplete() {
                        SystemOut.println("完成");
                    }
                });
    }

    public void demo4() {
        Observable
                .fromCallable(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        SystemOut.println("call:当前线程-->" + Thread.currentThread().getName());
                        return "Hello Rxjava";
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()//订阅关系取消时，解除上游对下游的引用
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        SystemOut.println("onSubscribe:当前线程-->" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(String s) {
                        SystemOut.println("onNext:当前线程-->" + Thread.currentThread().getName());
                        SystemOut.println(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SystemOut.println("onError:当前线程-->" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        SystemOut.println("onComplete:当前线程-->" + Thread.currentThread().getName());
                    }
                });
    }

    public void demo5() {
        Observable
                .fromCallable(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        SystemOut.println("call:当前线程-->" + Thread.currentThread().getName());
                        return "Hello Rxjava";
                    }
                })
                .compose(new SchedulersTransFormer<>())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        SystemOut.println("onSubscribe:当前线程-->" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(String s) {
                        SystemOut.println("onNext:当前线程-->" + Thread.currentThread().getName());
                        SystemOut.println(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SystemOut.println("onError:当前线程-->" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        SystemOut.println("onComplete:当前线程-->" + Thread.currentThread().getName());
                    }
                });
    }

    public void demo6() {
        Single
                .just(3)
                .lift(new SQSingleOperator())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        SystemOut.println("开始订阅");
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

    public static class SQOperator implements ObservableOperator<Integer, Integer> {

        @Override
        public Observer<? super Integer> apply(Observer<? super Integer> observer) throws Exception {
            return new Observer<Integer>() {
                private Disposable mDisposable;

                @Override
                public void onSubscribe(Disposable d) {
                    mDisposable = d;
                    observer.onSubscribe(d);
                }

                @Override
                public void onNext(Integer integer) {
                    if (!mDisposable.isDisposed()) observer.onNext(integer * integer);
                }

                @Override
                public void onError(Throwable e) {
                    if (!mDisposable.isDisposed()) observer.onError(e);
                }

                @Override
                public void onComplete() {
                    if (!mDisposable.isDisposed()) observer.onComplete();
                }
            };
        }
    }

    public static class ToCharOperator implements ObservableOperator<Character, String> {


        @Override
        public Observer<? super String> apply(Observer<? super Character> observer) throws Exception {
            return new Observer<String>() {
                private Disposable mDisposable;

                @Override
                public void onSubscribe(Disposable d) {
                    mDisposable = d;
                }

                @Override
                public void onNext(String s) {
                    for (Character character : s.toCharArray()) {
                        if (mDisposable.isDisposed()) break;
                        observer.onNext(character);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (!mDisposable.isDisposed()) observer.onError(e);
                }

                @Override
                public void onComplete() {
                    if (!mDisposable.isDisposed()) observer.onComplete();
                }
            };
        }
    }

    public static class SQTransFormer implements ObservableTransformer<Integer, Integer> {


        @Override
        public ObservableSource<Integer> apply(Observable<Integer> upstream) {
            return upstream.map(new Function<Integer, Integer>() {
                @Override
                public Integer apply(Integer integer) throws Exception {
                    return integer * integer;
                }
            });
        }
    }

    public static class SchedulersTransFormer<T> implements ObservableTransformer<T, T> {

        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onTerminateDetach();//订阅关系取消时，解除上游对下游的引用;
        }
    }

    public static class SQSingleOperator implements SingleOperator<Integer, Integer> {

        @Override
        public SingleObserver<? super Integer> apply(SingleObserver<? super Integer> observer) throws Exception {
            return new SingleObserver<Integer>() {
                private Disposable mDisposable;

                @Override
                public void onSubscribe(Disposable d) {
                    mDisposable = d;
                    observer.onSubscribe(d);
                }

                @Override
                public void onSuccess(Integer integer) {
                    observer.onSuccess(integer * integer);
                }

                @Override
                public void onError(Throwable e) {
                    observer.onError(e);
                }
            };
        }
    }


}

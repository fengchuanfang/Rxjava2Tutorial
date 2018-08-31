package com.edward.edward.Rxjava2Demo;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 功能描述：Rxjava2入门教程四：Scheduler线程调度器
 *
 * @author (作者) edward（冯丰枫）
 * @link http://www.jianshu.com/u/f7176d6d53d2
 * 创建时间： 2017/8/19
 */
public class Rxjava2_4_Scheduler {

    public void schedulerDemo1() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < 5; i++) {
                            System.out.println("发射线程:" + Thread.currentThread().getName() + "---->" + "发射:" + i);
                            Thread.sleep(1000);
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())   //设置可观察对象在Schedulers.io()的线程中发射数据
                .observeOn(AndroidSchedulers.mainThread())//设置观察者在AndroidSchedulers.mainThread()的线程中处理数据
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer i) throws Exception {
                        System.out.println("接收线程:" + Thread.currentThread().getName() + "---->" + "接收:" + i);
                    }
                });
    }

    public void schedulerDemo2() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < 5; i++) {
                            System.out.println("发射线程:" + Thread.currentThread().getName() + "---->" + "发射:" + i);
                            Thread.sleep(1000);
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())//设置可观察对象在Schedulers.io()的线程中发射数据
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer i) throws Exception {
                        System.out.println("处理线程:" + Thread.currentThread().getName() + "---->" + "处理:" + i);
                        return i;
                    }
                })
                .subscribeOn(Schedulers.newThread())//指定map操作符在Schedulers.newThread()的线程中处理数据
                .observeOn(AndroidSchedulers.mainThread())//设置观察者在AndroidSchedulers.mainThread()的线程中处理数据
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer i) throws Exception {
                        System.out.println("接收线程:" + Thread.currentThread().getName() + "---->" + "接收:" + i);
                    }
                });
    }

    public void schedulerDemo3() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < 2; i++) {
                            System.out.println("发射线程:" + Thread.currentThread().getName() + "---->" + "发射:" + i);
                            Thread.sleep(1000);
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())//设置可观察对象在Schedulers.io()的线程中发射数据
                .observeOn(Schedulers.newThread())//指定map操作符在Schedulers.newThread()的线程中处理数据
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer i) throws Exception {
                        System.out.println("处理线程:" + Thread.currentThread().getName() + "---->" + "处理:" + i);
                        return i;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//设置观察者在AndroidSchedulers.mainThread()的线程中处理数据
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer i) throws Exception {
                        System.out.println("接收线程:" + Thread.currentThread().getName() + "---->" + "接收:" + i);
                    }
                });
    }

    public void schedulerDemo4() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < 5; i++) {
                            System.out.println("发射线程:" + Thread.currentThread().getName() + "---->" + "发射:" + i);
                            Thread.sleep(1000);
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())//设置可观察对象在Schedulers.io()的线程中发射数据
                .observeOn(Schedulers.trampoline())//设置观察者在当前线程中j接收数据
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer i) throws Exception {
                        Thread.sleep(2000);//休息2s后再处理数据
                        System.out.println("接收线程:" + Thread.currentThread().getName() + "---->" + "接收:" + i);
                    }
                });
    }

    public void schedulerDemo5() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < 3; i++) {
                            System.out.println("发射线程:" + Thread.currentThread().getName() + "---->" + "发射:" + i);
                            Thread.sleep(1000);
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.single())//设置可观察对象在Schedulers.single()的线程中发射数据
                .observeOn(Schedulers.single())//指定map操作符在Schedulers.single()的线程中处理数据
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer i) throws Exception {
                        System.out.println("处理线程:" + Thread.currentThread().getName() + "---->" + "处理:" + i);
                        return i;
                    }
                })
                .observeOn(Schedulers.single())//设置观察者在Schedulers.single()的线程中j接收数据
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer i) throws Exception {
                        System.out.println("接收线程:" + Thread.currentThread().getName() + "---->" + "接收:" + i);
                    }
                });
    }

}

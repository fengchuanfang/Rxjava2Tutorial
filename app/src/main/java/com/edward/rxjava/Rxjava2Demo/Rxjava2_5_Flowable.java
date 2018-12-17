package com.edward.edward.Rxjava2Demo;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 功能描述：Rxjava2入门教程五：Flowable背压支持——几乎可以说是对Flowable最全面而详细的讲解
 *
 * @author (作者) edward（冯丰枫）
 * @link http://www.jianshu.com/u/f7176d6d53d2
 * 创建时间： 2017/8/19
 */
public class Rxjava2_5_Flowable {

    public void demo1() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        int i = 0;
                        while (true) {
                            i++;
                            e.onNext(i);
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Thread.sleep(5000);
                        System.out.println(integer);
                    }
                });
    }

    public void demo2() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        System.out.println("发射----> 1");
                        e.onNext(1);
                        System.out.println("发射----> 2");
                        e.onNext(2);
                        System.out.println("发射----> 3");
                        e.onNext(3);
                        System.out.println("发射----> 完成");
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER) //create方法中多了一个BackpressureStrategy类型的参数
                .subscribeOn(Schedulers.newThread())//为上下游分别指定各自的线程
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {   //onSubscribe回调的参数不是Disposable而是Subscription
                        s.request(Long.MAX_VALUE);            //注意此处，暂时先这么设置

                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("接收----> " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("接收----> 完成");
                    }
                });
    }

    public void demo3() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        for (int i = 1; i <= 129; i++) {
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);            //注意此处，暂时先这么设置
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ignore) {
                        }
                        System.out.println(integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("接收----> 完成");
                    }
                });
    }

    public void demo4() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + "开始发射数据" + System.currentTimeMillis());
                        for (int i = 1; i <= 500; i++) {
                            System.out.println(threadName + "发射---->" + i);
                            e.onNext(i);
                            try {
                                Thread.sleep(100);//每隔100毫秒发射一次数据
                            } catch (Exception ignore) {
                            }
                        }
                        System.out.println(threadName + "发射数据结束" + System.currentTimeMillis());
                        e.onComplete();
                    }
                }, BackpressureStrategy.DROP)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);            //注意此处，暂时先这么设置
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(300);//每隔300毫秒接收一次数据
                        } catch (InterruptedException ignore) {
                        }
                        System.out.println(Thread.currentThread().getName() + "接收---------->" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println(Thread.currentThread().getName() + "接收----> 完成");
                    }
                });
    }

    public void demo5() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + "开始发射数据" + System.currentTimeMillis());
                        for (int i = 1; i <= 500; i++) {
                            System.out.println(threadName + "发射---->" + i);
                            e.onNext(i);
                            try {
                                Thread.sleep(100);
                            } catch (Exception ignore) {
                            }
                        }
                        System.out.println(threadName + "发射数据结束" + System.currentTimeMillis());
                        e.onComplete();

                    }
                }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);            //注意此处，暂时先这么设置
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException ignore) {
                        }
                        System.out.println(Thread.currentThread().getName() + "接收---------->" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println(Thread.currentThread().getName() + "接收----> 完成");
                    }
                });
    }

    public void demo6() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        int i = 0;
                        while (true) {
                            i++;
                            e.onNext(i);
                        }
                    }
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Thread.sleep(5000);
                        System.out.println(integer);
                    }
                });
    }

    public void demo7() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < 500; i++) {
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                }, BackpressureStrategy.DROP)
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });
    }

    public void demo8() {
        Flowable.range(0, 500)
                .onBackpressureDrop()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });
    }

    public void demo9() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        System.out.println("发射----> 1");
                        e.onNext(1);
                        System.out.println("发射----> 2");
                        e.onNext(2);
                        System.out.println("发射----> 3");
                        e.onNext(3);
                        System.out.println("发射----> 完成");
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER) //create方法中多了一个BackpressureStrategy类型的参数
                .subscribeOn(Schedulers.newThread())//为上下游分别指定各自的线程
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        //去掉代码s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("接收----> " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("接收----> 完成");
                    }
                });
    }

    public void demo10() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        System.out.println("发射----> 1");
                        e.onNext(1);
                        System.out.println("发射----> 2");
                        e.onNext(2);
                        System.out.println("发射----> 3");
                        e.onNext(3);
                        System.out.println("发射----> 完成");
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER) //create方法中多了一个BackpressureStrategy类型的参数
                .subscribeOn(Schedulers.newThread())//为上下游分别指定各自的线程
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(2);//设置Subscriber的消费能力为2
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("接收----> " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("接收----> 完成");
                    }
                });
    }

    public void demo11() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        String threadName = Thread.currentThread().getName();
                        for (int i = 1; i <= 10; i++) {
                            System.out.println(threadName + "发射---->" + i);
                            e.onNext(i);
                        }
                        System.out.println(threadName + "发射数据结束" + System.currentTimeMillis());
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(3);//调用两次request
                        s.request(4);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(Thread.currentThread().getName() + "接收---------->" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println(Thread.currentThread().getName() + "接收----> 完成");
                    }
                });
    }

    public void demo12() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        for (int i = 1; i < 130; i++) {
                            System.out.println("发射---->" + i);
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("接收------>" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("接收------>完成");
                    }
                });
    }

    public void demo13() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        String threadName = Thread.currentThread().getName();
                        for (int i = 1; i <= 5; i++) {
                            System.out.println("当前未完成的请求数量-->" + e.requested());
                            System.out.println(threadName + "发射---->" + i);
                            e.onNext(i);
                        }
                        System.out.println(threadName + "发射数据结束" + System.currentTimeMillis());
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)//上下游运行在同一线程中
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(3);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(Thread.currentThread().getName() + "接收---------->" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println(Thread.currentThread().getName() + "接收----> 完成");
                    }
                });
    }

    public void demo14() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        String threadName = Thread.currentThread().getName();
                        for (int i = 1; i <= 5; i++) {
                            System.out.println("当前未完成的请求数量-->" + e.requested());
                            System.out.println(threadName + "发射---->" + i);
                            e.onNext(i);
                        }
                        System.out.println(threadName + "发射数据结束" + System.currentTimeMillis());
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())//添加两行代码，为上下游分配独立的线程
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(3);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(Thread.currentThread().getName() + "接收---------->" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println(Thread.currentThread().getName() + "接收----> 完成");
                    }
                });
    }

    public void demo15() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        String threadName = Thread.currentThread().getName();
                        for (int i = 1; i <= 5; i++) {
                            System.out.println("当前未完成的请求数量-->" + e.requested());
                            System.out.println(threadName + "发射---->" + i);
                            e.onNext(i);
                        }
                        System.out.println(threadName + "发射数据结束" + System.currentTimeMillis());
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())//添加两行代码，为上下游分配独立的线程
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(500);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(Thread.currentThread().getName() + "接收---------->" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println(Thread.currentThread().getName() + "接收----> 完成");
                    }
                });
    }

    public void demo16() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        String threadName = Thread.currentThread().getName();
                        for (int i = 1; i <= 150; i++) {
                            System.out.println("当前未完成的请求数量-->" + e.requested());
                            System.out.println(threadName + "发射---->" + i);
                            e.onNext(i);
                        }
                        System.out.println(threadName + "发射数据结束" + System.currentTimeMillis());
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(150);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(Thread.currentThread().getName() + "接收---------->" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println(Thread.currentThread().getName() + "接收----> 完成");
                    }
                });
    }

    public void demo17() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        int i = 0;
                        while (true) {
                            i++;
                            System.out.println("发射---->" + i);
                            e.onNext(i);
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(50);
                            System.out.println("接收------>" + integer);
                        } catch (InterruptedException ignore) {
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("接收------>完成");
                    }
                });
    }

    public void demo18() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        int i = 0;
                        while (true) {
                            if (e.requested() == 0) continue;//此处添加代码，让flowable按需发送数据
                            System.out.println("发射---->" + i);
                            i++;
                            e.onNext(i);
                        }
                    }
                }, BackpressureStrategy.MISSING)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    private Subscription mSubscription;

                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);            //设置初始请求数据量为1
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(50);
                            System.out.println("接收------>" + integer);
                            mSubscription.request(1);//每接收到一条数据增加一条请求量
                        } catch (InterruptedException ignore) {
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}

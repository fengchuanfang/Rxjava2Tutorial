package com.edward.edward.Rxjava2Demo;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * 功能描述：Rxjava2入门教程三：Operators操作符
 *
 * @author (作者) edward（冯丰枫）
 * @link http://www.jianshu.com/u/f7176d6d53d2
 * 创建时间： 2017/8/19
 */
public class Rxjava2_3_Operators {

    public void demo1_range() {
        Observable
                .range(0, 5)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });
    }

    public void demo2_filter() {
        Observable
                .range(0, 10)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 3 == 0;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });
    }

    public void demo3_distinct() {
        Observable.just(1, 1, 2, 3, 1, 2, 2, 4, 5)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });
    }

    public void demo4_distinct_filter() {
        Observable.just(1, 1, 2, 3, 1, 2, 2, 4, 5)
                .distinct()
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer % 2 == 0;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });
    }

    public void demo5_map() {
        Observable.range(0, 5)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(@NonNull Integer integer) throws Exception {
                        return integer + "^2 = " + integer * integer;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        System.out.println(s);
                    }
                });
    }

    public void demo6_flatMap() {
        Integer nums1[] = new Integer[]{1, 2, 3, 4};
        Integer nums2[] = new Integer[]{5, 6};
        Integer nums3[] = new Integer[]{7, 8, 9};
        Observable.just(nums1, nums2, nums3)
                .flatMap(new Function<Integer[], Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(@NonNull Integer[] integers) throws Exception {
                        return Observable.fromArray(integers);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });
    }

    public void demo7_mergeWith() {
        Integer nums1[] = new Integer[]{5, 6, 7, 8, 9};
        Observable.just(1, 2, 3, 4, 5)
                .mergeWith(Observable.fromArray(nums1))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });
    }

    public void demo8_concatWith() {
        Integer nums1[] = new Integer[]{5, 6, 7, 8, 9};
        Observable.just(1, 2, 3, 4, 5)
                .concatWith(Observable.fromArray(nums1))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });
    }

    public void demo9_zipWith() {
        String names[] = new String[]{"红娃", "橙娃", "黄娃", "绿娃", "青娃", "蓝娃", "紫娃"};
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .zipWith(Observable.fromArray(names), new BiFunction<Integer, String, String>() {
                    @Override
                    public String apply(@NonNull Integer integer, @NonNull String s) throws Exception {
                        return integer + s;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        System.out.println(s);
                    }
                });
    }

    public void demo10_link_all_operators() {
        Integer nums1[] = new Integer[]{5, 6, 7, 8, 9};
        Integer nums2[] = new Integer[]{3, 4, 5, 6};
        String names[] = new String[]{"红娃", "橙娃", "黄娃", "绿娃", "青娃", "蓝娃", "紫娃"};
        Observable.just(nums1)
                .flatMap(new Function<Integer[], Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(@NonNull Integer[] integers) throws Exception {
                        return Observable.fromArray(integers);
                    }
                })
                .mergeWith(Observable.fromArray(nums2))
                .concatWith(Observable.just(1, 2))
                .distinct()
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer < 5;
                    }
                })
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(@NonNull Integer integer) throws Exception {
                        return integer + ":";
                    }
                })
                .zipWith(Observable.fromArray(names), new BiFunction<String, String, String>() {
                    @Override
                    public String apply(@NonNull String s, @NonNull String s2) throws Exception {
                        return s + s2;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        System.out.println(s);
                    }
                });
    }
}

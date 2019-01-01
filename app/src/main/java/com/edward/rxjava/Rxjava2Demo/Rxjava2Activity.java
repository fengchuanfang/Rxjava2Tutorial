package com.edward.rxjava.Rxjava2Demo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.edward.javaecho.StringUtils;
import com.edward.javaecho.SystemOut;
import com.edward.rxjava.R;
import com.edward.rxjava.Rxjava2DemoBean;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

/**
 * 功能描述：Rxjava2入门教程Demo演示类
 *
 * @author (作者) edward（冯丰枫）
 * @link http://www.jianshu.com/u/f7176d6d53d2
 * 创建时间： 2018/12/17
 */
public class Rxjava2Activity extends AppCompatActivity {
    @BindView(R.id.rxjava_caption)
    Spinner rxjava_caption;
    @BindView(R.id.rxjava_demo_method)
    Spinner rxjava_demo_method;
    @BindView(R.id.rxjava_demo_result)
    TextView rxjava_demo_result;

    private Disposable mTitleDisposable;
    private Disposable mDemoMethodDisposable;
    private Disposable mInvokeMethodDisposable;
    private List<Rxjava2DemoBean> mRxjava2DemoBeans;
    private Class mClass;
    private List<String> methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava2);
        ButterKnife.bind(this);
        rxjava_caption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initMethodSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rxjava_demo_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                invokeMethodByName(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRxjava2DemoBeans = new ArrayList<>();
        initTitleSpinner();
        SystemOut.init(this, rxjava_demo_result);
        rxjava_demo_result.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                StringUtils.copy(rxjava_demo_result.getText(), Rxjava2Activity.this);
                return true;
            }
        });
    }

    /**
     * 根据配置文件，初始化Demo列表
     */
    private void initTitleSpinner() {
        if (mTitleDisposable != null && !mTitleDisposable.isDisposed()) mTitleDisposable.dispose();
        mTitleDisposable = Single
                .fromCallable(new Callable<BufferedReader>() {
                    @Override
                    public BufferedReader call() throws Exception {
                        return new BufferedReader(new InputStreamReader(getAssets().open("directory.json")));
                    }
                })
                .map(new Function<BufferedReader, List<Rxjava2DemoBean>>() {
                    @Override
                    public List<Rxjava2DemoBean> apply(BufferedReader bufferedReader) throws Exception {
                        return new Gson().fromJson(bufferedReader, new TypeToken<ArrayList<Rxjava2DemoBean>>() {
                        }.getType());
                    }
                })
                .flatMapObservable(new Function<List<Rxjava2DemoBean>, Observable<Rxjava2DemoBean>>() {
                    @Override
                    public Observable<Rxjava2DemoBean> apply(List<Rxjava2DemoBean> rxjava2DemoBeans) throws Exception {
                        return Observable.fromIterable(rxjava2DemoBeans);
                    }
                })
                .filter(new Predicate<Rxjava2DemoBean>() {
                    @Override
                    public boolean test(Rxjava2DemoBean rxjava2DemoBean) throws Exception {
                        return !TextUtils.isEmpty(rxjava2DemoBean.getDemo_class());
                    }
                })
                .doOnNext(new Consumer<Rxjava2DemoBean>() {
                    @Override
                    public void accept(Rxjava2DemoBean rxjava2DemoBean) throws Exception {
                        mRxjava2DemoBeans.add(rxjava2DemoBean);
                    }
                })
                .map(new Function<Rxjava2DemoBean, String>() {
                    @Override
                    public String apply(Rxjava2DemoBean rxjava2DemoBean) throws Exception {
                        return rxjava2DemoBean.getTitle();
                    }
                })
                .toList()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRxjava2DemoBeans.clear();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        rxjava_caption.setAdapter(new ArrayAdapter<String>(Rxjava2Activity.this, R.layout.simple_spinner_item, R.id.spinner_text, strings));
                    }
                });
    }

    /**
     * 根据类的全路径名，获取类中可执行的方法
     */
    private void initMethodSpinner(int index) {
        if (mDemoMethodDisposable != null && !mDemoMethodDisposable.isDisposed()) mDemoMethodDisposable.dispose();
        mDemoMethodDisposable =
                Single.just(mRxjava2DemoBeans.get(index))
                        .map(new Function<Rxjava2DemoBean, String>() {
                            @Override
                            public String apply(Rxjava2DemoBean rxjava2DemoBean) throws Exception {
                                return "com.edward.rxjava.Rxjava2Demo." + rxjava2DemoBean.getDemo_class();//拼接Demo类的全路径名
                            }
                        })
                        .map(new Function<String, Class>() {
                            @Override
                            public Class apply(String className) throws Exception {
                                return Class.forName(className);//反射获取Demo类
                            }
                        })
                        .doOnSuccess(new Consumer<Class>() {
                            @Override
                            public void accept(Class aClass) throws Exception {
                                mClass = aClass;
                            }
                        })
                        .map(new Function<Class, Method[]>() {
                            @Override
                            public Method[] apply(Class aClass) throws Exception {
                                return aClass.getDeclaredMethods();//获取Deme类中声明的所有方法
                            }
                        })
                        .flatMapObservable(new Function<Method[], Observable<Method>>() {
                            @Override
                            public Observable<Method> apply(Method[] methods) throws Exception {
                                return Observable.fromArray(methods);
                            }
                        })
                        .map(new Function<Method, String>() {
                            @Override
                            public String apply(Method method) throws Exception {
                                return method.getName();
                            }
                        })
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {//根据方法名过滤掉构造方法
                                return !s.contains("access$super");
                            }
                        })
                        .toList()
                        .doOnSuccess(new Consumer<List<String>>() {
                            @Override
                            public void accept(List<String> strings) throws Exception {
                                methods = strings;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<String>>() {
                            @Override
                            public void accept(List<String> strings) throws Exception {
                                rxjava_demo_method.setAdapter(new ArrayAdapter<String>(Rxjava2Activity.this, R.layout.simple_spinner_item, R.id.spinner_text, strings));
                            }
                        });

    }


    /**
     * 根据方法名，通过反射执行方法，并将结果通过TextView展示出来
     */
    private void invokeMethodByName(int index) {
        if (mClass == null || methods == null || methods.size() == 0) return;
        String methodName = methods.get(index);
        if (mInvokeMethodDisposable != null && !mInvokeMethodDisposable.isDisposed()) mInvokeMethodDisposable.dispose();
        mInvokeMethodDisposable = Observable
                .fromArray(mClass.getDeclaredMethods())
                .filter(new Predicate<Method>() {
                    @Override
                    public boolean test(Method method) throws Exception {
                        return method.getName().equals(methodName);
                    }
                })
                .groupBy(new Function<Method, Integer>() {  //根据方法所需参数的个数进行分组
                    @Override
                    public Integer apply(Method method) throws Exception {
                        return method.getParameterTypes().length;
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        SystemOut.reset();
                    }
                })
                .subscribe(new Consumer<GroupedObservable<Integer, Method>>() {
                    @Override
                    public void accept(GroupedObservable<Integer, Method> observables) throws Exception {
                        switch (observables.getKey()) {
                            case 0:
                                observables.subscribe(new Consumer<Method>() {
                                    @Override
                                    public void accept(Method method) throws Exception {
                                        SystemOut.println("执行结果为-->\r\n");
                                        method.invoke(mClass.newInstance());
                                    }
                                });
                                break;
                            case 1:
                                observables.subscribe(new Consumer<Method>() {
                                    @Override
                                    public void accept(Method method) throws Exception {
                                        if (method.getParameterTypes()[0] == List.class) {
                                            List<String> params = Arrays.asList("1", "2", "3", "4", "5", "6", "7");
                                            SystemOut.println("传入参数为-->" + params.toString());
                                            SystemOut.println("执行结果为-->\r\n");
                                            method.invoke(mClass.newInstance(), params);
                                        }
                                    }
                                });
                                break;
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SystemOut.clear();
        if (mTitleDisposable != null && !mTitleDisposable.isDisposed()) mTitleDisposable.dispose();
        if (mDemoMethodDisposable != null && !mDemoMethodDisposable.isDisposed()) mDemoMethodDisposable.dispose();
        if (mInvokeMethodDisposable != null && !mInvokeMethodDisposable.isDisposed()) mInvokeMethodDisposable.dispose();
    }
}

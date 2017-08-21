package com.edward.edward.rxjava2tutorial;

import com.edward.edward.Rxjava2Demo.Rxjava2_Flowable;
import com.edward.edward.Rxjava2Demo.Rxjava2_Observable;
import com.edward.edward.Rxjava2Demo.Rxjava2_Operators;
import com.edward.edward.Rxjava2Demo.Rxjava2_Scheduler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Rxjava2Activity extends AppCompatActivity {

    /**
     * Rxjava2入门教程二：Observable与Observer响应式编程在Rxjava2中的典型实现
     */
    private Rxjava2_Observable mObservable;

    /**
     * Rxjava2入门教程三：Operators操作符
     */
    private Rxjava2_Operators mOperators;

    /**
     * Rxjava2入门教程四：Scheduler线程调度器
     */
    private Rxjava2_Scheduler mScheduler;

    /**
     * Rxjava2入门教程五：Flowable背压支持——几乎可以说是对Flowable最全面而详细的讲解
     */
    private Rxjava2_Flowable mFlowable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava2);
        mObservable = new Rxjava2_Observable();
        mOperators = new Rxjava2_Operators();
        mScheduler = new Rxjava2_Scheduler();
        mFlowable = new Rxjava2_Flowable();
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mObservable.demo8();
            }
        });
    }
}

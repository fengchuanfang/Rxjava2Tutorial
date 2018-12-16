package com.edward.edward.Rxjava2Demo;

import com.edward.edward.Rxjava2Demo.Rxjava2_5_Flowable;
import com.edward.edward.Rxjava2Demo.Rxjava2_2_Observable;
import com.edward.edward.Rxjava2Demo.Rxjava2_3_Operators;
import com.edward.edward.Rxjava2Demo.Rxjava2_4_Scheduler;
import com.edward.edward.Rxjava2Demo.Rxjava2_6_Single_Completable_Maybe;
import com.edward.edward.Rxjava2Demo.Rxjava2_8_Custom_Operators;
import com.edward.edward.rxjava2tutorial.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Rxjava2Activity extends AppCompatActivity {

    /**
     * Rxjava2入门教程二：Observable与Observer响应式编程在Rxjava2中的典型实现
     */
    private Rxjava2_2_Observable mObservable;

    /**
     * Rxjava2入门教程三：Operators操作符
     */
    private Rxjava2_3_Operators mOperators;

    /**
     * Rxjava2入门教程四：Scheduler线程调度器
     */
    private Rxjava2_4_Scheduler mScheduler;

    /**
     * Rxjava2入门教程五：Flowable背压支持——几乎可以说是对Flowable最全面而详细的讲解
     */
    private Rxjava2_5_Flowable mFlowable;

    /**
     * Rxjava2入门教程六：Single、Completable、Maybe——简化版的Observable
     */
    private Rxjava2_6_Single_Completable_Maybe mSingleCompletableMaybe;

    /**
     * Rxjava2入门教程八：自定义操作符
     */
    private Rxjava2_8_Custom_Operators mCustomOperators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava2);
        mObservable = new Rxjava2_2_Observable();
        mOperators = new Rxjava2_3_Operators();
        mScheduler = new Rxjava2_4_Scheduler();
        mFlowable = new Rxjava2_5_Flowable();
        mSingleCompletableMaybe = new Rxjava2_6_Single_Completable_Maybe();
        mCustomOperators = new Rxjava2_8_Custom_Operators();
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomOperators.demo1();
            }
        });
    }
}

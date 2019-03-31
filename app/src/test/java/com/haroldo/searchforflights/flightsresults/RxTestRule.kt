package com.haroldo.searchforflights.flightsresults

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


class RxTestRule(private val scheduler: Scheduler = Schedulers.trampoline()) : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {

                overrideSchedulers(scheduler)
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }

    private fun overrideSchedulers(scheduler: Scheduler) {
        RxJavaPlugins.setInitIoSchedulerHandler { scheduler }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler }
    }

}
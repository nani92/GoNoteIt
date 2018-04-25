package eu.napcode.gonoteit.rx;

import io.reactivex.Scheduler;

public interface RxSchedulers {

    Scheduler io();

    Scheduler androidMainThread();
}

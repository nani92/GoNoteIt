package eu.napcode.gonoteit.rx;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AppSchedulers implements RxSchedulers {

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    public Scheduler androidMainThread() {
        return AndroidSchedulers.mainThread();
    }
}

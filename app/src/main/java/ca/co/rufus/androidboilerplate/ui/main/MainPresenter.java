package ca.co.rufus.androidboilerplate.ui.main;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import ca.co.rufus.androidboilerplate.data.model.RepoOwnerJoin;
import rx.Subscriber;
import rx.Subscription;
import timber.log.Timber;
import ca.co.rufus.androidboilerplate.BoilerplateApplication;
import ca.co.rufus.androidboilerplate.R;
import ca.co.rufus.androidboilerplate.data.DataManager;
import ca.co.rufus.androidboilerplate.ui.base.BasePresenter;
import ca.co.rufus.androidboilerplate.util.SchedulerAppliers;

public class MainPresenter extends BasePresenter<MainMvpView> {

    @Inject protected DataManager mDataManager;
    private Subscription mSubscription;

    public MainPresenter(Context context) {
        super(context);
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
        BoilerplateApplication.get(getContext()).getComponent().inject(this);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadRepos() {
        checkViewAttached();
        mSubscription = mDataManager.getRepository()
                .compose(SchedulerAppliers.<List<RepoOwnerJoin>>defaultSchedulers(getContext()))
                .subscribe(new Subscriber<List<RepoOwnerJoin>>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        String errorString = getContext().getString(R.string.error_loading_ribots);
                        getMvpView().showError(errorString);
                    }

                    @Override
                    public void onNext(List<RepoOwnerJoin> joins) {
                        if (joins.isEmpty()) {
                            getMvpView().showReposEmpty();
                        } else {
                            getMvpView().showRepos(joins);
                        }
                    }
                });
    }

}

package com.kotlinmockwebserver.example.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.kotlinmockwebserver.example.app.GithubApp;
import com.kotlinmockwebserver.example.mvp.views.RepositoryLikesView;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Date: 26.01.2016
 * Time: 16:32
 *
 * @author Yuri Shmakov
 */
@InjectViewState
public class RepositoryLikesPresenter extends BasePresenter<RepositoryLikesView> {
	public static final String TAG = "RepositoryLikesPresenter";

	@Inject
	Bus mBus;

	private List<Integer> mInProgress = new ArrayList<>();
	private List<Integer> mLikedIds = new ArrayList<>();

	public RepositoryLikesPresenter() {
		GithubApp.getAppComponent().inject(this);

		mBus.register(this);
	}

	public void toggleLike(int id) {
		if (mInProgress.contains(id)) {
			return;
		}

		mInProgress.add(id);

		getViewState().updateLikes(mInProgress, mLikedIds);

		final Observable<Boolean> toggleObservable = Observable.create(subscriber -> {
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			subscriber.onNext(!mLikedIds.contains(id));
		});

	 	Subscription subscription = toggleObservable
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(isLiked -> {
					onComplete(id, isLiked);
				}, throwable -> {
					onFail(id);
				});
		unsubscribeOnDestroy(subscription);
	}

	private void onComplete(int id, Boolean isLiked) {
		if (!mInProgress.contains(id)) {
			return;
		}

		mInProgress.remove(Integer.valueOf(id));
		if (isLiked) {
			mLikedIds.add(id);
		} else {
			mLikedIds.remove(Integer.valueOf(id));
		}

		getViewState().updateLikes(mInProgress, mLikedIds);
	}

	private void onFail(int id) {
		if (!mInProgress.contains(id)) {
			return;
		}

		mInProgress.remove(Integer.valueOf(id));
		getViewState().updateLikes(mInProgress, mLikedIds);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mBus.unregister(this);
	}
}

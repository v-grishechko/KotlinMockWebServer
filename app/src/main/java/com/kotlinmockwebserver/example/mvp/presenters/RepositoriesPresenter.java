package com.kotlinmockwebserver.example.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.kotlinmockwebserver.example.app.GithubApi;
import com.kotlinmockwebserver.example.app.GithubApp;
import com.kotlinmockwebserver.example.mvp.GithubService;
import com.kotlinmockwebserver.example.mvp.models.Repository;
import com.kotlinmockwebserver.example.mvp.views.RepositoriesView;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Date: 22.01.2016
 * Time: 14:39
 *
 * @author Yuri Shmakov
 */
@InjectViewState
public class RepositoriesPresenter extends BasePresenter<RepositoriesView> {

	@Inject
	GithubService mGithubService;

	private boolean mIsInLoading;

	public RepositoriesPresenter() {
		GithubApp.getAppComponent().inject(this);
	}

	@Override protected void onFirstViewAttach() {
		super.onFirstViewAttach();
		loadRepositories(false);
	}

	public void loadNextRepositories(int currentCount) {
		int page = currentCount / GithubApi.PAGE_SIZE + 1;

		loadData(page, true, false);
	}

	public void loadRepositories(boolean isRefreshing) {
		loadData(1, false, isRefreshing);
	}

	private void loadData(int page, boolean isPageLoading, boolean isRefreshing) {
		if (mIsInLoading) {
			return;
		}
		mIsInLoading = true;

		getViewState().hideError();
		getViewState().onStartLoading();

		showProgress(isPageLoading, isRefreshing);

		final Observable<List<Repository>> observable = mGithubService.getUserRepos("JakeWharton", page, GithubApi.PAGE_SIZE);

		Subscription subscription = observable.observeOn(AndroidSchedulers.mainThread())
				.subscribe(repositories -> {
					onLoadingFinish(isPageLoading, isRefreshing);
					onLoadingSuccess(isPageLoading, repositories);
				}, error -> {
					onLoadingFinish(isPageLoading, isRefreshing);
					onLoadingFailed(error);
				});
		unsubscribeOnDestroy(subscription);
	}

	private void onLoadingFinish(boolean isPageLoading, boolean isRefreshing) {
		mIsInLoading = false;

		getViewState().onFinishLoading();

		hideProgress(isPageLoading, isRefreshing);
	}

	private void onLoadingSuccess(boolean isPageLoading, List<Repository> repositories) {
		boolean maybeMore = repositories.size() >= GithubApi.PAGE_SIZE;
		if (isPageLoading) {
			getViewState().addRepositories(repositories, maybeMore);
		} else {
			getViewState().setRepositories(repositories, maybeMore);
		}
	}

	private void onLoadingFailed(Throwable error) {
		getViewState().showError(error.toString());
	}

	private void showProgress(boolean isPageLoading, boolean isRefreshing) {
		if (isPageLoading) {
			return;
		}

		if (isRefreshing) {
			getViewState().showRefreshing();
		} else {
			getViewState().showListProgress();
		}
	}

	private void hideProgress(boolean isPageLoading, boolean isRefreshing) {
		if (isPageLoading) {
			return;
		}

		if (isRefreshing) {
			getViewState().hideRefreshing();
		} else {
			getViewState().hideListProgress();
		}
	}

	public void closeError() {
		getViewState().hideError();
	}
}
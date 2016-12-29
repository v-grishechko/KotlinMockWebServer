package com.kotlinmockwebserver.example.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kotlinmockwebserver.example.mvp.models.Repository;
import com.kotlinmockwebserver.example.mvp.views.RepositoryView;

import java.util.List;

/**
 * Date: 27.01.2016
 * Time: 21:12
 *
 * @author Yuri Shmakov
 */
@InjectViewState
public class RepositoryPresenter extends MvpPresenter<RepositoryView> {
	private boolean mIsInitialized = false;
	private Repository mRepository;
	private List<Integer> mInProgress;
	private List<Integer> mLikedIds;

	public RepositoryPresenter() {
		super();
	}

	public void setRepository(Repository repository) {
		if (mIsInitialized) {
			return;
		}
		mIsInitialized = true;

		mRepository = repository;

		getViewState().showRepository(repository);

		updateLikes(mInProgress, mLikedIds);
	}

	public void updateLikes(List<Integer> inProgress, List<Integer> likedIds) {
		mInProgress = inProgress;
		mLikedIds = likedIds;

		if (mRepository == null || mInProgress == null || mLikedIds == null) {
			return;
		}

		boolean isInProgress = inProgress.contains(mRepository.getId());
		boolean isLiked = likedIds.contains(mRepository.getId());

		getViewState().updateLike(isInProgress, isLiked);
	}
}

package com.kotlinmockwebserver.example.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;
import com.kotlinmockwebserver.example.R;
import com.kotlinmockwebserver.example.mvp.common.MvpAppCompatFragment;
import com.kotlinmockwebserver.example.mvp.models.Repository;
import com.kotlinmockwebserver.example.mvp.presenters.RepositoryLikesPresenter;
import com.kotlinmockwebserver.example.mvp.presenters.RepositoryPresenter;
import com.kotlinmockwebserver.example.mvp.views.RepositoryLikesView;
import com.kotlinmockwebserver.example.mvp.views.RepositoryView;
import com.kotlinmockwebserver.example.ui.views.RepositoryWidget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 27.01.2016
 * Time: 20:17
 *
 * @author Yuri Shmakov
 */
public class DetailsFragment extends MvpAppCompatFragment implements RepositoryView, RepositoryLikesView {
	public static final String ARGS_REPOSITORY = "argsRepository";

	@InjectPresenter(type = PresenterType.GLOBAL)
	RepositoryPresenter mRepositoryPresenter;

	private Repository mRepository;

	@ProvidePresenterTag(presenterClass = RepositoryPresenter.class, type = PresenterType.GLOBAL)
	String provideRepositoryPresenterTag() {
		mRepository = (Repository) getArguments().get(ARGS_REPOSITORY);
		return String.valueOf(mRepository.getId());
	}

	@ProvidePresenter(type = PresenterType.GLOBAL)
	RepositoryPresenter provideRepositoryPresenter() {
		RepositoryPresenter repositoryPresenter = new RepositoryPresenter();
		repositoryPresenter.setRepository(mRepository);
		return repositoryPresenter;
	}

	@InjectPresenter(type = PresenterType.GLOBAL, tag = RepositoryLikesPresenter.TAG)
	RepositoryLikesPresenter mRepositoryLikesPresenter;

	@BindView(R.id.fragment_repository_details_text_view_title)
	RepositoryWidget mTitleTextView;
	@BindView(R.id.fragment_repository_details_image_button_like)
	ImageButton mLikeImageButton;

	public static DetailsFragment getInstance(Repository repository) {
		DetailsFragment fragment = new DetailsFragment();

		Bundle args = new Bundle();
		args.putSerializable(ARGS_REPOSITORY, repository);

		fragment.setArguments(args);

		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_repository_details, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ButterKnife.bind(this, view);

		mLikeImageButton.setOnClickListener(likeImageButton -> mRepositoryLikesPresenter.toggleLike(mRepository.getId()));
	}

	public void setRepository(Repository repository) {
		mRepositoryPresenter.setRepository(repository);
	}

	@Override
	public void updateLikes(List<Integer> inProgress, List<Integer> likedIds) {
		mRepositoryPresenter.updateLikes(inProgress, likedIds);
	}

	@Override
	public void showRepository(Repository repository) {
		mRepository = repository;

		mTitleTextView.setRepository(getMvpDelegate(), repository);
	}

	@Override
	public void updateLike(boolean isInProgress, boolean isLiked) {
		mLikeImageButton.setEnabled(!isInProgress);
		mLikeImageButton.setSelected(isLiked);
	}
}

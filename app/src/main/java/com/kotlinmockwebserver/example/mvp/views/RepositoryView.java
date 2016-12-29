package com.kotlinmockwebserver.example.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.kotlinmockwebserver.example.mvp.models.Repository;

/**
 * Date: 27.01.2016
 * Time: 21:13
 *
 * @author Yuri Shmakov
 */
public interface RepositoryView extends MvpView {
	void showRepository(Repository repository);

	void updateLike(boolean isInProgress, boolean isLiked);
}

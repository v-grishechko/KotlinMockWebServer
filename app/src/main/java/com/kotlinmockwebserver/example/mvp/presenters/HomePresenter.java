package com.kotlinmockwebserver.example.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kotlinmockwebserver.example.mvp.models.Repository;
import com.kotlinmockwebserver.example.mvp.views.HomeView;

/**
 * Date: 27.01.2016
 * Time: 19:59
 *
 * @author Yuri Shmakov
 */
@InjectViewState
public class HomePresenter extends MvpPresenter<HomeView> {
	public void onRepositorySelection(int position, Repository repository) {
		getViewState().showDetailsContainer();

		getViewState().showDetails(position, repository);
	}
}

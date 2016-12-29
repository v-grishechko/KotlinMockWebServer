package com.kotlinmockwebserver.example.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.kotlinmockwebserver.example.mvp.models.Repository;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 27.01.2016
 * Time: 20:00
 *
 * @author Yuri Shmakov
 */
public interface HomeView extends MvpView {
	@StateStrategyType(SkipStrategy.class)
	void showDetails(int position, Repository repository);

	void showDetailsContainer();
}

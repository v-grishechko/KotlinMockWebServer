package com.kotlinmockwebserver.example.di;

import android.content.Context;

import com.kotlinmockwebserver.example.di.modules.BusModule;
import com.kotlinmockwebserver.example.di.modules.ContextModule;
import com.kotlinmockwebserver.example.di.modules.GithubModule;
import com.kotlinmockwebserver.example.mvp.GithubService;
import com.kotlinmockwebserver.example.mvp.presenters.RepositoriesPresenter;
import com.kotlinmockwebserver.example.mvp.presenters.RepositoryLikesPresenter;
import com.kotlinmockwebserver.example.mvp.presenters.SignInPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Date: 8/18/2016
 * Time: 14:49
 *
 * @author Artur Artikov
 */
@Singleton
@Component(modules = {ContextModule.class, BusModule.class, GithubModule.class})
public interface AppComponent {
	Context getContext();
	GithubService getAuthService();
	Bus getBus();

	void inject(SignInPresenter presenter);
	void inject(RepositoriesPresenter repositoriesPresenter);
	void inject(RepositoryLikesPresenter repositoryLikesPresenter);
}

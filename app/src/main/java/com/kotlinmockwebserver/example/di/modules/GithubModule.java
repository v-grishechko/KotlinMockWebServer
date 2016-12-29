package com.kotlinmockwebserver.example.di.modules;


import com.kotlinmockwebserver.example.app.GithubApi;
import com.kotlinmockwebserver.example.mvp.GithubService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Date: 8/26/2016
 * Time: 11:58
 *
 * @author Artur Artikov
 */

@Module(includes = {ApiModule.class})
public class GithubModule {
	@Provides
	@Singleton
	public GithubService provideGithubService(GithubApi authApi) {
		return new GithubService(authApi);
	}
}
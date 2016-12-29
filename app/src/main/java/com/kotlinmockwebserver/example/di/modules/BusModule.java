package com.kotlinmockwebserver.example.di.modules;

import com.kotlinmockwebserver.example.app.GithubApi;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Date: 20.09.2016
 * Time: 20:22
 *
 * @author Yuri Shmakov
 */
@Module
public class BusModule {
	@Provides
	@Singleton
	public Bus provideBus(GithubApi authApi) {
		return new Bus();
	}
}

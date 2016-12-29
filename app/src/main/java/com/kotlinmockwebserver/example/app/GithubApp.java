package com.kotlinmockwebserver.example.app;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.kotlinmockwebserver.example.di.AppComponent;
import com.kotlinmockwebserver.example.di.DaggerAppComponent;
import com.kotlinmockwebserver.example.di.modules.ContextModule;

/**
 * Date: 18.01.2016
 * Time: 11:22
 *
 * @author Yuri Shmakov
 */
public class GithubApp extends Application {
	private static AppComponent sAppComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		sAppComponent = DaggerAppComponent.builder()
				.contextModule(new ContextModule(this))
				.build();

	}

	public static AppComponent getAppComponent() {
		return sAppComponent;
	}


	@VisibleForTesting
	public static void setAppComponent(@NonNull AppComponent appComponent) {
		sAppComponent = appComponent;
	}
}

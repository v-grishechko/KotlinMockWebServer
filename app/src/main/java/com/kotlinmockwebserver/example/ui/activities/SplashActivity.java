package com.kotlinmockwebserver.example.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.kotlinmockwebserver.example.mvp.common.MvpAppCompatActivity;
import com.kotlinmockwebserver.example.mvp.presenters.SplashPresenter;
import com.kotlinmockwebserver.example.mvp.views.SplashView;

public class SplashActivity extends MvpAppCompatActivity implements SplashView {
	@InjectPresenter
	SplashPresenter mSplashPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// By default view attaches to presenter in onStart() method,
		// but we attach it manually for earlier check authorization state.
		getMvpDelegate().onAttach();

		mSplashPresenter.checkAuthorized();
	}

	@Override
	public void setAuthorized(boolean isAuthorized) {
		startActivityForResult(new Intent(this, isAuthorized ? HomeActivity.class : SignInActivity.class), 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		finish();
	}
}

package com.kotlinmockwebserver.example.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kotlinmockwebserver.example.mvp.common.AuthUtils;
import com.kotlinmockwebserver.example.mvp.views.SignOutView;

/**
 * Date: 18.01.2016
 * Time: 16:03
 *
 * @author Yuri Shmakov
 */
@InjectViewState
public class SignOutPresenter extends MvpPresenter<SignOutView> {
	public void signOut() {
		AuthUtils.setToken("");

		getViewState().signOut();
	}
}

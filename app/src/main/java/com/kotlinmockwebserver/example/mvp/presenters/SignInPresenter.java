package com.kotlinmockwebserver.example.mvp.presenters;

import android.text.TextUtils;
import android.util.Base64;

import com.arellomobile.mvp.InjectViewState;
import com.kotlinmockwebserver.example.R;
import com.kotlinmockwebserver.example.app.GithubApp;
import com.kotlinmockwebserver.example.mvp.GithubService;
import com.kotlinmockwebserver.example.mvp.common.AuthUtils;
import com.kotlinmockwebserver.example.mvp.models.User;
import com.kotlinmockwebserver.example.mvp.views.SignInView;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Date: 15.01.2016
 * Time: 18:33
 *
 * @author Yuri Shmakov
 */
@InjectViewState
public class SignInPresenter extends BasePresenter<SignInView> {

    @Inject
    GithubService mGithubService;

    public SignInPresenter() {
        GithubApp.getAppComponent().inject(this);
    }

    public void signIn(String email, String password) {

        Integer emailError = null;
        Integer passwordError = null;

        getViewState().showError(null, null);

        if (TextUtils.isEmpty(email)) {
            emailError = R.string.error_field_required;
        }

        if (TextUtils.isEmpty(password)) {
            passwordError = R.string.error_invalid_password;
        }

        if (emailError != null || passwordError != null) {
            getViewState().showError(emailError, passwordError);
            return;
        }

        getViewState().showProgress();

        String credentials = String.format("%s:%s", email, password);

         final String token = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Observable<User> userObservable = mGithubService.signIn(token)
                .doOnNext(user -> AuthUtils.setToken(token));

        Subscription subscription = userObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    getViewState().hideProgress();
                    getViewState().successSignIn();
                }, exception -> {
                    getViewState().hideProgress();
                    getViewState().showError(exception.getMessage());
                });
        unsubscribeOnDestroy(subscription);
    }

    public void onErrorCancel() {
        getViewState().hideError();
    }
}

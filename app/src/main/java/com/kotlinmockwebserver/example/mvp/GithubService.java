package com.kotlinmockwebserver.example.mvp;

import com.kotlinmockwebserver.example.app.GithubApi;
import com.kotlinmockwebserver.example.mvp.models.Repository;
import com.kotlinmockwebserver.example.mvp.models.User;

import java.util.List;

import rx.Observable;

/**
 * Date: 20.09.2016
 * Time: 20:14
 *
 * @author Yuri Shmakov
 */

public class GithubService {

	private GithubApi mGithubApi;

	public GithubService(GithubApi githubApi) {
		mGithubApi = githubApi;
	}


	public Observable<User> signIn(String token) {
		return mGithubApi.signIn(token);
	}

	public Observable<List<Repository>> getUserRepos(String user, int page, Integer pageSize) {
		return mGithubApi.getUserRepos(user, page, pageSize);
	}
}

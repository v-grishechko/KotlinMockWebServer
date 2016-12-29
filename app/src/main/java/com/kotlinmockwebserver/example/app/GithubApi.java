package com.kotlinmockwebserver.example.app;

import com.kotlinmockwebserver.example.mvp.models.Repository;
import com.kotlinmockwebserver.example.mvp.models.User;
import com.kotlinmockwebserver.example.mvp.models.gson.SearchResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Date: 18.01.2016
 * Time: 12:07
 *
 * @author Yuri Shmakov
 */
public interface GithubApi {
	Integer PAGE_SIZE = 50;

	@GET("/user")
	Observable<User> signIn(@Header("Authorization") String token);

	@GET("/search/repositories?sort=stars&order=desc")
	Observable<SearchResult> search(@Query("q") String query);

	@GET("/users/{login}/repos")
	Observable<List<Repository>> getUserRepos(@Path("login") String login, @Query("page") int page, @Query("per_page") int pageSize);
}

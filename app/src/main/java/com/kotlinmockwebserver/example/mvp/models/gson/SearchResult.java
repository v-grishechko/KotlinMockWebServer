package com.kotlinmockwebserver.example.mvp.models.gson;

import com.kotlinmockwebserver.example.mvp.models.Repository;

import java.util.List;

/**
 * Date: 18.01.2016
 * Time: 12:13
 *
 * @author Yuri Shmakov
 */
public class SearchResult {
	private int mTotalCount;
	private boolean mIncompleteResults;
	private List<Repository> mItems;

	public List<Repository> getRepositories() {
		return mItems;
	}
}
package com.kotlinmockwebserver.example.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.kotlinmockwebserver.example.R;
import com.kotlinmockwebserver.example.mvp.common.MvpAppCompatActivity;
import com.kotlinmockwebserver.example.mvp.models.Repository;
import com.kotlinmockwebserver.example.mvp.presenters.HomePresenter;
import com.kotlinmockwebserver.example.mvp.presenters.RepositoriesPresenter;
import com.kotlinmockwebserver.example.mvp.presenters.SignOutPresenter;
import com.kotlinmockwebserver.example.mvp.views.HomeView;
import com.kotlinmockwebserver.example.mvp.views.RepositoriesView;
import com.kotlinmockwebserver.example.mvp.views.SignOutView;
import com.kotlinmockwebserver.example.ui.adapters.RepositoriesAdapter;
import com.kotlinmockwebserver.example.ui.fragments.DetailsFragment;
import com.kotlinmockwebserver.example.ui.views.FrameSwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends MvpAppCompatActivity implements SignOutView, RepositoriesView, HomeView, RepositoriesAdapter.OnScrollToBottomListener {
	@InjectPresenter
	SignOutPresenter mSignOutPresenter;
	@InjectPresenter
	RepositoriesPresenter mRepositoriesPresenter;
	@InjectPresenter
	HomePresenter mHomePresenter;

	@BindView(R.id.activity_home_toolbar)
	Toolbar mToolbar;
	@BindView(R.id.activity_home_swipe_refresh_layout)
	FrameSwipeRefreshLayout mSwipeRefreshLayout;
	@BindView(R.id.activity_home_progress_bar_repositories)
	ProgressBar mRepositoriesProgressBar;
	@BindView(R.id.activity_home_list_view_repositories)
	ListView mRepositoriesListView;
	@BindView(R.id.activity_home_text_view_no_repositories)
	TextView mNoRepositoriesTextView;
	@BindView(R.id.activity_home_frame_layout_details)
	FrameLayout mDetailsFragmeLayout;

	private AlertDialog mErrorDialog;
	private RepositoriesAdapter mRepositoriesAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		ButterKnife.bind(this);

		setSupportActionBar(mToolbar);

		mSwipeRefreshLayout.setListViewChild(mRepositoriesListView);
		mSwipeRefreshLayout.setOnRefreshListener(() -> mRepositoriesPresenter.loadRepositories(true));

		mRepositoriesAdapter = new RepositoriesAdapter(getMvpDelegate(), this);
		mRepositoriesListView.setAdapter(mRepositoriesAdapter);
		mRepositoriesListView.setOnItemClickListener((parent, view, position, id) -> {
			if (mRepositoriesAdapter.getItemViewType(position) != RepositoriesAdapter.REPOSITORY_VIEW_TYPE) {
				return;
			}

			mHomePresenter.onRepositorySelection(position, mRepositoriesAdapter.getItem(position));
		});

		mErrorDialog = new AlertDialog.Builder(this)
				.setTitle(R.string.app_name)
				.setOnCancelListener(dialog -> mRepositoriesPresenter.closeError())
				.create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_home_sign_out) {
			mSignOutPresenter.signOut();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void signOut() {
		final Intent intent = new Intent(this, SplashActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(intent);
	}

	@Override
	public void onStartLoading() {
		mSwipeRefreshLayout.setEnabled(false);
	}

	@Override
	public void onFinishLoading() {
		mSwipeRefreshLayout.setEnabled(true);
	}

	@Override
	public void showRefreshing() {
		mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
	}

	@Override
	public void hideRefreshing() {
		mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
	}

	@Override
	public void showListProgress() {
		mRepositoriesListView.setVisibility(View.GONE);
		mNoRepositoriesTextView.setVisibility(View.GONE);
		mRepositoriesProgressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideListProgress() {
		mRepositoriesListView.setVisibility(View.VISIBLE);
		mRepositoriesProgressBar.setVisibility(View.GONE);
	}

	@Override
	public void showError(String message) {
		mErrorDialog.setMessage(message);
		mErrorDialog.show();
	}


	@Override
	public void hideError() {
		mErrorDialog.hide();
	}

	@Override
	public void setRepositories(List<Repository> repositories, boolean maybeMore) {
		mRepositoriesListView.setEmptyView(mNoRepositoriesTextView);
		mRepositoriesAdapter.setRepositories(repositories, maybeMore);
	}

	@Override
	public void addRepositories(List<Repository> repositories, boolean maybeMore) {
		mRepositoriesListView.setEmptyView(mNoRepositoriesTextView);
		mRepositoriesAdapter.addRepositories(repositories, maybeMore);
	}

	@Override
	public void showDetailsContainer() {
		if (mDetailsFragmeLayout.getVisibility() == View.GONE) {
			mDetailsFragmeLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void showDetails(int position, Repository repository) {
		DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.activity_home_frame_layout_details);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.activity_home_frame_layout_details, DetailsFragment.getInstance(repository));

		if (detailsFragment != null) {
			transaction.addToBackStack(null);
		}

		transaction.commit();
	}

	@Override
	public void onScrollToBottom() {
		mRepositoriesPresenter.loadNextRepositories(mRepositoriesAdapter.getRepositoriesCount());
	}
}
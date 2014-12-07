package com.etiennelawlor.issues.fragments;

/**
 * Created by etiennelawlor on 12/6/14.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.etiennelawlor.issues.R;
import com.etiennelawlor.issues.adapters.IssuesAdapter;
import com.etiennelawlor.issues.models.Issue;
import com.etiennelawlor.issues.utils.HttpUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class IssuesFragment extends ListFragment implements AbsListView.OnScrollListener {

    // region Constants
    private static final String ISSUES_URL = "https://api.github.com/repos/rails/rails/issues?state=open&sort=updated&direction=desc";
    // endregion

    // region Member Variables
    private ListView mListView;
    private LinearLayout mEmptyLinearLayout;
    private IssuesAdapter mIssuesAdapter;
    private int mPageNumber = 1;
    private int mPrevFirstVisibleItem = 0;
    private boolean mIsLoading = false;
    private View mLoadingFooterView;
    // endregion

    // region Constructors
    public IssuesFragment() {
    }
    // endregion

    // region Lifecycle Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_issues, container, false);
        mLoadingFooterView = inflater.inflate(R.layout.loading_footer, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindUIElements(view);

        mIssuesAdapter = new IssuesAdapter(getActivity());
        setListAdapter(mIssuesAdapter);
        mListView.setOnScrollListener(this);
//        mListView.setEmptyView(mEmptyLinearLayout);

        String issuesUrl = String.format("%s&page=%d", ISSUES_URL, mPageNumber);
        new DownloadTask().execute(issuesUrl);
    }
    // endregion

    // region Helper Methods
    private void bindUIElements(View view){
        mListView = (ListView) view.findViewById(android.R.id.list);
        mEmptyLinearLayout = (LinearLayout) view.findViewById(android.R.id.empty);
    }
    // endregion

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Issue issue = (Issue) getListAdapter().getItem(position);

        Bundle bundle = new Bundle();
        bundle.putInt("issue_number", issue.getNumber());
        CommentsDialogFragment dialogFragment = CommentsDialogFragment.newInstance(bundle);
        dialogFragment.show(getFragmentManager(),"");
    }

    // region AbsListView.OnScrollListener Methods
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!mIsLoading && totalItemCount > 0 && (firstVisibleItem + visibleItemCount == totalItemCount) && firstVisibleItem>mPrevFirstVisibleItem+1) {
            mPrevFirstVisibleItem = firstVisibleItem;

            loadMoreItems();
        }
    }
    // endregion

    // region Helper Methods
    private void loadMoreItems(){
        mIsLoading = true;

        mPageNumber += 1;

        String issuesUrl = String.format("%s&page=%d", ISSUES_URL, mPageNumber);
        new DownloadTask().execute(issuesUrl);
    }
    // endregion

    // region Inner Classes

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mListView.addFooterView(mLoadingFooterView);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return HttpUtility.loadFromNetwork(urls[0]);
            } catch (IOException e) {
                return getString(R.string.connection_error);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(isAdded() && isResumed()){
                mIsLoading = false;
                mListView.removeFooterView(mLoadingFooterView);

                if(result.equals(getString(R.string.connection_error)))
                    return;

                JSONArray issues;
                try {
                    issues = new JSONArray(result);

                    if(issues != null){
                        for(int i = 0 ; i < issues.length(); i++){
                            JSONObject jsonObject = (JSONObject)issues.get(i);

                            String title = (String) jsonObject.get("title");
                            String body = (String) jsonObject.get("body");
                            int number = (int) jsonObject.get("number");

                            Issue issue = new Issue(title, body, number);
                            mIssuesAdapter.add(issue);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        // endregion
    }

    // endregion
}

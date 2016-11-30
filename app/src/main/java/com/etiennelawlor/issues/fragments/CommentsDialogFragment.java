package com.etiennelawlor.issues.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.etiennelawlor.issues.R;
import com.etiennelawlor.issues.adapters.CommentsAdapter;
import com.etiennelawlor.issues.models.Comment;
import com.etiennelawlor.issues.utilities.HttpUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by etiennelawlor on 12/7/14.
 */

public class CommentsDialogFragment extends DialogFragment implements AbsListView.OnScrollListener {

    //region Member Variables
    private int issueNumber;
    private ListView listView;
    private FrameLayout emptyFrameLayout;
    private TextView emptyTextView;
    private ProgressBar progressBar;
    private int pageNumber = 1;
    private int prevFirstVisibleItem = 0;
    private boolean isLoading = false;
    private CommentsAdapter commentsAdapter;
    private View loadingFooterView;
    //endregion

    // region Constructors
    public static CommentsDialogFragment newInstance(Bundle extras) {
        CommentsDialogFragment fragment = new CommentsDialogFragment();
        fragment.setArguments(extras);
        fragment.setRetainInstance(true);

        return fragment;
    }

    public CommentsDialogFragment() {}
    // endregion

    // region Lifecycle Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle != null) {
            issueNumber = (Integer) bundle.get("issue_number");
        }

//        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_comments, container);
        loadingFooterView = inflater.inflate(R.layout.loading_footer, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle bundle) {
        getDialog().setCanceledOnTouchOutside(true);
        setCancelable(true);

        bindUIElements(view);

        commentsAdapter = new CommentsAdapter(getActivity());
        listView.setAdapter(commentsAdapter);
        listView.setOnScrollListener(this);

        String commentsUrl = String.format("https://api.github.com/repos/rails/rails/issues/%d/comments?page=%d", issueNumber, pageNumber);

        new DownloadTask().execute(commentsUrl);

    }
    // endregion

    // region AbsListView.OnScrollListener Methods
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!isLoading && totalItemCount > 0 && (firstVisibleItem + visibleItemCount == totalItemCount) && firstVisibleItem>prevFirstVisibleItem+1) {
            prevFirstVisibleItem = firstVisibleItem;

            loadMoreItems();
        }
    }
    // endregion

    // region Helper Methods
    private void bindUIElements(View view) {
        listView = (ListView) view.findViewById(android.R.id.list);
        emptyFrameLayout = (FrameLayout) view.findViewById(android.R.id.empty);
        progressBar = (ProgressBar) view.findViewById(R.id.pb);
        emptyTextView = (TextView) view.findViewById(R.id.empty_tv);
    }

    private void loadMoreItems(){
        isLoading = true;

        pageNumber += 1;

        listView.addFooterView(loadingFooterView);

        String commentsUrl = String.format("https://api.github.com/repos/rails/rails/issues/%d/comments?page=%d", issueNumber, pageNumber);

        new DownloadTask().execute(commentsUrl);
    }
    // endregion

    // region Inner Classes

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            emptyTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
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
                isLoading = false;
                listView.removeFooterView(loadingFooterView);

                progressBar.setVisibility(View.GONE);

                if(result.equals(getString(R.string.connection_error)))
                    return;

                JSONArray comments;
                try {
                    comments = new JSONArray(result);
                    if(comments != null){
                        if(comments.length() == 0 && commentsAdapter.isEmpty()){
                            emptyTextView.setVisibility(View.VISIBLE);
                        } else {
                            emptyTextView.setVisibility(View.GONE);

                            for(int i = 0 ; i < comments.length(); i++){
                                JSONObject jsonObject = (JSONObject) comments.get(i);

                                String body = (String) jsonObject.get("body");
                                JSONObject userJsonObject = (JSONObject) jsonObject.get("user");
                                if(userJsonObject != null){
                                    String userName = (String) userJsonObject.get("login");

                                    Comment comment = new Comment(userName, body);
                                    commentsAdapter.add(comment);
                                }
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // endregion
}
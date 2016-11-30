package com.etiennelawlor.issues.fragments;

/**
 * Created by etiennelawlor on 12/6/14.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.etiennelawlor.issues.R;
import com.etiennelawlor.issues.adapters.IssuesAdapter;
import com.etiennelawlor.issues.models.Issue;
import com.etiennelawlor.issues.models.User;
import com.etiennelawlor.issues.utilities.HttpUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class IssuesFragment extends Fragment implements IssuesAdapter.OnItemClickListener {

    // region Constants
    public static final int PAGE_SIZE = 30;
    private static final String ISSUES_URL = "https://api.github.com/repos/rails/rails/issues?state=open&sort=updated&direction=desc";
    // endregion

    // region Views
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout emptyLinearLayout;
    private TextView emptyTextView;
    private LinearLayout errorLinearLayout;
    private TextView errorTextView;
    private Button reloadButton;
    private Toolbar toolbar;
    // endregion

    // region Member Variables
    private IssuesAdapter issuesAdapter;
    private int pageNumber = 1;
    private boolean isLoading = false;
    private LinearLayoutManager layoutManager;
    private boolean isLastPage = false;
    // endregion

    // region Listeners
    private View.OnClickListener reloadOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            errorLinearLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            String issuesUrl = String.format("%s&page=%d", ISSUES_URL, pageNumber);
            new FirstFetchDownloadTask().execute(issuesUrl);
        }
    };

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    loadMoreItems();
                }
            }
        }
    };
    // endregion

    // region Constructors
    public IssuesFragment() {
    }
    // endregion

    // region Factory Methods
    public static IssuesFragment newInstance(Bundle extras) {
        IssuesFragment fragment = new IssuesFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public static IssuesFragment newInstance() {
        IssuesFragment fragment = new IssuesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    // endregion

    // region Lifecycle Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_issues, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindUIElements(view);
        setUpListeners();

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("rails / rails");
        }

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        issuesAdapter = new IssuesAdapter();
        issuesAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(issuesAdapter);

        // Pagination
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        String issuesUrl = String.format("%s&page=%d", ISSUES_URL, pageNumber);
        new FirstFetchDownloadTask().execute(issuesUrl);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeListeners();
    }

    // endregion

    // region IssuesAdapter.OnItemClickListener Methods

    @Override
    public void onItemClick(int position, View view) {
        Issue issue = issuesAdapter.getItem(position);
        if(issue != null){
            Bundle bundle = new Bundle();
            bundle.putInt("issue_number", issue.getNumber());
            CommentsDialogFragment dialogFragment = CommentsDialogFragment.newInstance(bundle);
            dialogFragment.show(getFragmentManager(),"");
        }
    }

    // endregion

    // region Helper Methods
    private void bindUIElements(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        progressBar = (ProgressBar) view.findViewById(R.id.pb);
        emptyLinearLayout = (LinearLayout) view.findViewById(android.R.id.empty);
        errorLinearLayout = (LinearLayout) view.findViewById(R.id.error_ll);
        errorTextView = (TextView) view.findViewById(R.id.error_tv);
        reloadButton = (Button) view.findViewById(R.id.reload_btn);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    }

    private void setUpListeners(){
        reloadButton.setOnClickListener(reloadOnClickListener);
    }

    private void removeListeners(){
        reloadButton.setOnClickListener(null);
    }
    // endregion

    // region Helper Methods
    private void loadMoreItems(){
        isLoading = true;

        pageNumber += 1;

        String issuesUrl = String.format("%s&page=%d", ISSUES_URL, pageNumber);
        new NextFetchDownloadTask().execute(issuesUrl);
    }

    private List<Issue> parseJson(String json){
        List<Issue> issues = new ArrayList<>();

        JSONArray issuesJSONArray;
        try {
            issuesJSONArray = new JSONArray(json);

            if(issuesJSONArray != null){
                int numOfIssues = issuesJSONArray.length();

                for(int i = 0 ; i < numOfIssues; i++){
                    JSONObject jsonObject = (JSONObject)issuesJSONArray.get(i);

                    String title = (String) jsonObject.get("title");
                    String body = (String) jsonObject.get("body");
                    int number = (int) jsonObject.get("number");
                    String createdAt = (String) jsonObject.get("created_at");
                    String updatedAt = (String) jsonObject.get("updated_at");

                    JSONObject userJSONObject = (JSONObject) jsonObject.get("user");
                    String login = (String) userJSONObject.get("login");

                    User user = new User();
                    user.setLogin(login);

                    Issue issue = new Issue();
                    issue.setTitle(title);
                    issue.setNumber(number);
                    issue.setCreatedAt(createdAt);
                    issue.setUpdatedAt(updatedAt);
                    issue.setUser(user);

                    issues.add(issue);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return issues;
    }
    // endregion

    // region Inner Classes

    private class FirstFetchDownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                progressBar.setVisibility(View.GONE);

                if(result.equals(getString(R.string.connection_error))) {
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);

                    errorTextView.setText("Can't load data.\nCheck your network connection.");
                    errorLinearLayout.setVisibility(View.VISIBLE);
                    return;
                }

                List<Issue> issues = parseJson(result);
                issuesAdapter.addAll(issues);

                if (issues.size() >= PAGE_SIZE) {
                    issuesAdapter.addFooter();
                } else {
                    isLastPage = true;
                }

                if (issuesAdapter.isEmpty()) {
                    emptyLinearLayout.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    private class NextFetchDownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                issuesAdapter.removeFooter();

                if(result.equals(getString(R.string.connection_error)))
                    return;

                List<Issue> issues = parseJson(result);
                issuesAdapter.addAll(issues);

                if (issues.size() >= PAGE_SIZE) {
                    issuesAdapter.addFooter();
                } else {
                    isLastPage = true;
                }
            }

        }
    }

    // endregion
}

package com.etiennelawlor.issues.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etiennelawlor.issues.R;
import com.etiennelawlor.issues.models.Issue;
import com.etiennelawlor.issues.models.User;
import com.etiennelawlor.issues.utilities.DateUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by etiennelawlor on 5/23/15.
 */

public class IssuesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // region Constants
    public static final int HEADER = 0;
    public static final int ITEM = 1;
    public static final int FOOTER = 2;
    // endregion

    // region Member Variables
    private List<Issue> issues;
    private OnItemClickListener onItemClickListener;
    private OnReloadClickListener onReloadClickListener;
    private boolean isFooterAdded = false;
    private FooterViewHolder footerViewHolder;
    // endregion

    // region Listeners
    // endregion

    // region Interfaces
    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public interface OnReloadClickListener {
        void onReloadClick();
    }
    // endregion

    // region Constructors
    public IssuesAdapter() {
        issues = new ArrayList<>();
    }
    // endregion

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case HEADER:
                break;
            case ITEM:
                viewHolder = createIssueViewHolder(parent);
                break;
            case FOOTER:
                viewHolder = createFooterViewHolder(parent);
                break;
            default:
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case HEADER:
                break;
            case ITEM:
                bindIssueViewHolder(viewHolder, position);
                break;
            case FOOTER:
                bindFooterViewHolder(viewHolder);
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == issues.size()-1 && isFooterAdded) ? FOOTER : ITEM;
    }

    // region Helper Methods
    private RecyclerView.ViewHolder createIssueViewHolder(ViewGroup parent) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_row, parent, false);

        final IssueViewHolder holder = new IssueViewHolder(v);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPos = holder.getAdapterPosition();
                if(adapterPos != RecyclerView.NO_POSITION){
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(adapterPos, holder.itemView);
                    }
                }
            }
        });

        return holder;
    }

    private RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_footer, parent, false);

        final FooterViewHolder holder = new FooterViewHolder(v);
        holder.reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onReloadClickListener != null){
                    onReloadClickListener.onReloadClick();
                }
            }
        });

        return holder;
    }

    private void bindIssueViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final IssueViewHolder holder = (IssueViewHolder) viewHolder;

        final Issue issue = issues.get(position);
        if (issue != null) {
            setUpTitle(holder.titleTextView, issue);
            setUpSubtitle(holder.subtitleTextView, issue);
        }
    }

    private void bindFooterViewHolder(RecyclerView.ViewHolder viewHolder) {
        FooterViewHolder holder = (FooterViewHolder) viewHolder;
        footerViewHolder = holder;
    }

    public void add(Issue item) {
        issues.add(item);
        notifyItemInserted(issues.size()-1);
    }

    public void addAll(List<Issue> videos) {
        for (Issue video : videos) {
            add(video);
        }
    }

    public void remove(Issue item) {
        int position = issues.indexOf(item);
        if (position > -1) {
            issues.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isFooterAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addFooter(){
        isFooterAdded = true;
        add(new Issue());
    }

    public void removeFooter() {
        isFooterAdded = false;

        int position = issues.size() - 1;
        Issue item = getItem(position);

        if (item != null) {
            issues.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateFooter(FooterType footerType){
        switch (footerType) {
            case LOAD_MORE:
                if(footerViewHolder!= null){
                    footerViewHolder.errorRelativeLayout.setVisibility(View.GONE);
                    footerViewHolder.loadingFrameLayout.setVisibility(View.VISIBLE);
                }
                break;
            case ERROR:
                if(footerViewHolder!= null){
                    footerViewHolder.loadingFrameLayout.setVisibility(View.GONE);
                    footerViewHolder.errorRelativeLayout.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    public Issue getItem(int position) {
        return issues.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnReloadClickListener(OnReloadClickListener onReloadClickListener) {
        this.onReloadClickListener = onReloadClickListener;
    }

    private void setUpTitle(TextView tv, Issue issue) {
        String title = issue.getTitle();
        if (!TextUtils.isEmpty(title)) {
            tv.setText(title);
        }
    }

    private void setUpSubtitle(TextView tv, Issue issue) {
        int number = issue.getNumber();
        String createdAt = issue.getCreatedAt();
        String formatedCreatedAt = DateUtility.getFormattedMagicDate(createdAt);
        String updatedAt = issue.getUpdatedAt();
        String formatedUpdatedAt = DateUtility.getFormattedMagicDate(updatedAt);

        User user = issue.getUser();
        String login = user.getLogin();
        tv.setText(String.format("#%d opened %s by %s updated %s", number, formatedCreatedAt, login, formatedUpdatedAt));
    }
    // endregion

    // region Inner Classes

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    public static class IssueViewHolder extends RecyclerView.ViewHolder {
        // region Views
        TextView titleTextView;
        TextView subtitleTextView;
        // endregion

        // region Constructors
        public IssueViewHolder(View view) {
            super(view);

            titleTextView = (TextView) view.findViewById(R.id.title_tv);
            subtitleTextView = (TextView) view.findViewById(R.id.subtitle_tv);
        }
        // endregion
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        // region Views
        FrameLayout loadingFrameLayout;
        RelativeLayout errorRelativeLayout;
        ProgressBar progressBar;
        Button reloadButton;
        // endregion

        // region Constructors
        public FooterViewHolder(View view) {
            super(view);

            loadingFrameLayout = (FrameLayout) view.findViewById(R.id.loading_fl);
            errorRelativeLayout = (RelativeLayout) view.findViewById(R.id.error_rl);
            progressBar = (ProgressBar) view.findViewById(R.id.pb);
            reloadButton = (Button) view.findViewById(R.id.reload_btn);
        }
        // endregion
    }


    public enum FooterType {
        LOAD_MORE,
        ERROR
    }

    // endregion

}
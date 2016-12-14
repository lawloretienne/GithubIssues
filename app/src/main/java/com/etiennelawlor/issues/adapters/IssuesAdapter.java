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

/**
 * Created by etiennelawlor on 5/23/15.
 */

public class IssuesAdapter extends BaseAdapter<Issue> {

    // region Member Variables
    private FooterViewHolder footerViewHolder;
    // endregion

    // region Constructors
    public IssuesAdapter() {
        super();
    }
    // endregion

    @Override
    public int getItemViewType(int position) {
        return (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }

    @Override
    protected RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent) {
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

    @Override
    protected RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent) {
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

    @Override
    protected void bindHeaderViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    protected void bindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final IssueViewHolder holder = (IssueViewHolder) viewHolder;

        final Issue issue = getItem(position);
        if (issue != null) {
            setUpTitle(holder.titleTextView, issue);
            setUpSubtitle(holder.subtitleTextView, issue);
        }
    }

    @Override
    protected void bindFooterViewHolder(RecyclerView.ViewHolder viewHolder) {
        FooterViewHolder holder = (FooterViewHolder) viewHolder;
        footerViewHolder = holder;
    }

    @Override
    protected void displayLoadMoreFooter() {
        if(footerViewHolder!= null){
            footerViewHolder.errorRelativeLayout.setVisibility(View.GONE);
            footerViewHolder.loadingFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void displayErrorFooter() {
        if(footerViewHolder!= null){
            footerViewHolder.loadingFrameLayout.setVisibility(View.GONE);
            footerViewHolder.errorRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void addFooter() {
        isFooterAdded = true;
        add(new Issue());
    }

    // region Helper Methods
    private void setUpTitle(TextView tv, Issue issue) {
        String title = issue.getTitle();
        if (!TextUtils.isEmpty(title)) {
            tv.setText(title);
        }
    }

    private void setUpSubtitle(TextView tv, Issue issue) {
        int number = issue.getNumber();
        String createdAt = issue.getCreatedAt();
        String formatedCreatedAt = DateUtility.getFormattedDateAndTime(DateUtility.getCalendar(createdAt, PATTERN), DateUtility.FORMAT_RELATIVE);
        String updatedAt = issue.getUpdatedAt();
        String formatedUpdatedAt = DateUtility.getFormattedDateAndTime(DateUtility.getCalendar(updatedAt, PATTERN), DateUtility.FORMAT_RELATIVE);

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

    // endregion

}
package com.etiennelawlor.issues.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.etiennelawlor.issues.R;
import com.etiennelawlor.issues.models.Issue;

/**
 * Created by etiennelawlor on 12/6/14.
 */
public class IssuesAdapter extends ArrayAdapter<Issue> {

    // region Member Variables
    private LayoutInflater mInflater;
    // endregion

    // region Constructors
    public IssuesAdapter(Context context) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
    }
    // endregion


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.issue_row, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title_tv);
            viewHolder.bodyTextView = (TextView) convertView.findViewById(R.id.body_tv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Issue issue = getItem(position);
        if(issue != null){
            viewHolder.titleTextView.setText(issue.getTitle());
            viewHolder.bodyTextView.setText(issue.getBody());
            viewHolder.number = issue.getNumber();
        }

        return convertView;
    }

    // region Inner Classes
    static class ViewHolder {
        TextView titleTextView;
        TextView bodyTextView;
        int number;
    }
    // endregion
}

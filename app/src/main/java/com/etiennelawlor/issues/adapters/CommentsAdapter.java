package com.etiennelawlor.issues.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.etiennelawlor.issues.R;
import com.etiennelawlor.issues.models.Comment;

/**
 * Created by etiennelawlor on 12/6/14.
 */
public class CommentsAdapter extends ArrayAdapter<Comment> {

    // region Member Variables
    private LayoutInflater mInflater;
    // endregion

    // region Constructors
    public CommentsAdapter(Context context) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
    }
    // endregion

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.comment_row, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.userNameTextView = (TextView) convertView.findViewById(R.id.username_tv);
            viewHolder.bodyTextView = (TextView) convertView.findViewById(R.id.body_tv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Comment comment = getItem(position);
        if(comment != null){
            viewHolder.userNameTextView.setText(comment.getUserName());
            viewHolder.bodyTextView.setText(comment.getBody().trim());
        }

        return convertView;
    }

    // region Inner Classes
    static class ViewHolder {
        TextView userNameTextView;
        TextView bodyTextView;
    }
    // endregion
}

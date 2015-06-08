package com.maxkalavera.ecoar.productinfo;

import java.util.List;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

class DeleteElementListener implements View.OnClickListener,  DialogInterface.OnClickListener {
	CommentariesListFragment commentariesListFragment = null;
	CommentariesPrefaceListFragment commentariesPrefaceListFragment = null;
	int position;
	
	public DeleteElementListener(CommentariesListFragment commentariesListFragment) {
		this.commentariesListFragment = commentariesListFragment;
	}
	
	public DeleteElementListener(CommentariesPrefaceListFragment commentariesPrefaceListFragment) {
		this.commentariesPrefaceListFragment = commentariesPrefaceListFragment;
	}
	
	public void onClick(View view) {
		this.position = (Integer) view.getTag();
		if (this.commentariesListFragment != null) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.commentariesListFragment.getActivity());
        	builder.setMessage(R.string.productinfo_commentaries_item_delete_message)
               .setPositiveButton(R.string.productinfo_commentaries_item_delete_positive, this)
               .setNegativeButton(R.string.productinfo_commentaries_item_delete_negative, null);
        	builder.show();
		} else if (this.commentariesPrefaceListFragment != null) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(this.commentariesPrefaceListFragment.getActivity());
        	builder.setMessage(R.string.productinfo_commentaries_item_delete_message)
               .setPositiveButton(R.string.productinfo_commentaries_item_delete_positive, this)
               .setNegativeButton(R.string.productinfo_commentaries_item_delete_negative, null);
        	builder.show();
		}
	}
	
    public void onClick(DialogInterface dialog, int id) {
    	if (this.commentariesListFragment != null) {
    		this.commentariesListFragment.deleteComment(this.position);
    	} else if (this.commentariesPrefaceListFragment != null) {
    		this.commentariesPrefaceListFragment.deleteComment(this.position);
    	}
    }
}

public class CommentariesListFragmentAdapter extends ArrayAdapter<List<CommentModel>> {

	private String username;
	private DeleteElementListener deleteCommentListener;
	/************************************************************
	 *
	 ************************************************************/
	CommentariesListFragmentAdapter(CommentariesListFragment commentariesListFragment,
			List<CommentModel> commentariesList,
			String username) {
		super(commentariesListFragment.getActivity(), R.layout.productinfo_commentaries_item, (List)commentariesList);
		this.username = username;
		this.deleteCommentListener = new DeleteElementListener(commentariesListFragment);
	}
	
	CommentariesListFragmentAdapter(CommentariesPrefaceListFragment commentariesPrefaceListFragment,
			List<CommentModel> commentariesList,
			String username) {
		super(commentariesPrefaceListFragment.getActivity(), R.layout.productinfo_commentaries_item, (List)commentariesList);
		this.username = username;
		this.deleteCommentListener = new DeleteElementListener(commentariesPrefaceListFragment);
	}
	
	/************************************************************
	 *
	 ************************************************************/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			convertView = inflater.inflate(R.layout.productinfo_commentaries_item, null);
		}
		
		CommentModel commentary = (CommentModel) this.getItem(position);
		
		TextView username = (TextView) convertView.findViewById(R.id.productinfo_commentaries_item_username);
		username.setText(commentary.username);
		
		TextView postingDate = (TextView) convertView.findViewById(R.id.productinfo_commentaries_item_postingdate);
		postingDate.setText(commentary.getDateAsToShow());
		
		TextView text = (TextView) convertView.findViewById(R.id.productinfo_commentaries_item_text);
		text.setText(commentary.body);
		
		if (commentary.username.equals(this.username)) {
			Button deleteButton = (Button) convertView.findViewById(R.id.productinfo_commentaries_item_delete);
			deleteButton.setTag( Integer.valueOf(position));
			deleteButton.setOnClickListener(this.deleteCommentListener);
		}
		
		
		
		return convertView;
	}
}

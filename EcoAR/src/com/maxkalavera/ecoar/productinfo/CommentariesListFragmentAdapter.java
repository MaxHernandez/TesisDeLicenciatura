package com.maxkalavera.ecoar.productinfo;

import java.util.List;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

class DeleteElementListener implements View.OnClickListener {
	CommentariesListFragment commentariesListFragment;
	
	public DeleteElementListener(CommentariesListFragment commentariesListFragment) {
		this.commentariesListFragment = commentariesListFragment;
	}
	
	public void onClick(View view) {
		Integer position = (Integer) view.getTag();
		
		this.commentariesListFragment.deleteComment(position);
	}
}

public class CommentariesListFragmentAdapter extends ArrayAdapter<List<CommentModel>> {

	private String username;
	
	/************************************************************
	 *
	 ************************************************************/
	CommentariesListFragmentAdapter(CommentariesListFragment commentariesListFragment,
			List<CommentModel> commentariesList,
			String username) {
		super(commentariesListFragment.getActivity(), R.layout.productinfo_commentaries_item, (List)commentariesList);
		this.username = username;
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
		TextView postingDate = (TextView) convertView.findViewById(R.id.productinfo_commentaries_item_postingdate);
		TextView text = (TextView) convertView.findViewById(R.id.productinfo_commentaries_item_text);
		
		username.setText(commentary.username);
		postingDate.setText(commentary.getDateAsToShow());
		text.setText(commentary.body);
		
		if (commentary.username.equals(this.username)) {
			Button deleteButton = (Button) convertView.findViewById(R.id.productinfo_commentaries_item_delete);
			deleteButton.setTag( Integer.valueOf(position));
		}
		
		return convertView;
	}
}

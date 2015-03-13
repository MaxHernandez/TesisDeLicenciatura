package com.maxkalavera.utils.databasemodels;

import java.util.Calendar;

import android.text.format.Time;

public class CommentaryModel {
	public String text;
	public String userID;
	public String postingDate;
	
	public CommentaryModel () {
		this.postingDate = java.text.DateFormat.
				getDateTimeInstance().
				format(Calendar.getInstance().
						getTime());
	}
}

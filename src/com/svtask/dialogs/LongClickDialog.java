package com.svtask.dialogs;

import com.svtask2.R;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

public class LongClickDialog {
	
	private Dialog dialog;
	private Context context;
	private TextView tvWord;
	private int itemId;
	
	public Button button_cancel;
	public Button button_edit;
	public Button button_delete;
	
	public LongClickDialog(Context context) {
		this.context = context;
		
		dialog = new Dialog(this.context);
		dialog.setContentView(R.layout.long_click_dialog);	
		dialog.setTitle(context.getResources().getString(R.string.long_click_dialog_title));
		dialog.setCancelable(false);		
		button_delete = (Button) dialog.findViewById(R.id.button_delete_long_click_dialog);
		button_cancel = (Button) dialog.findViewById(R.id.button_cancel_long_click_dialog);
		button_edit = (Button) dialog.findViewById(R.id.button_edit_long_click_dialog);
		tvWord = (TextView) dialog.findViewById(R.id.textView_word_long_click_dialog);		
	}
	
	public void showDialog() {
		dialog.show();
	}
	
	public void closeDialog() {
		dialog.dismiss();
	}
	
	public void setDialogWord(String word) {
		tvWord.setText(word);
	}
	
	public void setItemId(int id) {
		itemId = id;
	}
	
	public int getItemId() {
		return itemId;
	}
}

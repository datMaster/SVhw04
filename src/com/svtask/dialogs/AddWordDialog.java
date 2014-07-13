package com.svtask.dialogs;

import com.svtask.settings.Constants;
import com.svtask2.R;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddWordDialog {
	
	private Dialog dialog;
	private Context context;
	private EditText newWord;
	private boolean editFlag;
	private int itemId;
	
	public Button button_add;
	public Button button_cancel;
	
	public AddWordDialog(Context context) {
		this.context = context;
		dialog = new Dialog(this.context);
		dialog.setContentView(R.layout.add_dialog);	
		dialog.setCancelable(false);
		dialog.setTitle(context.getResources().getString(R.string.new_word));
		button_add = (Button) dialog.findViewById(R.id.button_add);
		button_cancel = (Button) dialog.findViewById(R.id.button_cancel);
		newWord = (EditText) dialog.findViewById(R.id.editText_new_word);
	}
	
	public void showDialog() {		
		dialog.show();
							
	}
	
	public void setEditFlag(boolean flag) {
		editFlag = flag;
	}
	
	public boolean getEditFlag() {
		return editFlag;
	}
	
	public String getWord() {
		if(newWord.getText().toString() == null) {
			Toast.makeText(context, "Word is null", Toast.LENGTH_LONG).show();
			return null;
		}		
		return newWord.getText().toString();
	}
	
	public void closeDialog() {
		dialog.dismiss();
		clearInput();
	}
	
	private void clearInput() {
		newWord.setText("");
	}
	
	public void setLabels(int select) {		
		switch (select) {
		case Constants.DIALOG_ADD:
			dialog.setTitle(context.getResources().getString(R.string.new_word));
			button_add.setText(context.getResources().getString(R.string.button_add));
			break;

		case Constants.DIALOG_EDIT:
			dialog.setTitle(context.getResources().getString(R.string.dialog_edit_title));
			button_add.setText(context.getResources().getString(R.string.button_edit));
			break;
		}				
	}
	
	public void setWord(String word) {
		newWord.setText(word);
	}
	
	public void setItemId(int id) {
		itemId = id;
	}
	
	public int getItemId() {
		return itemId;
	}
		
}

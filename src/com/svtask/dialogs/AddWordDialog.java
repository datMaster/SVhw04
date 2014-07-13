package com.svtask.dialogs;

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
		
}

package com.svtask.dialogs;

import com.svtask.adapters.SettingsItemsAdapter;
import com.svtask.settings.Constants;

import android.content.Context;

public class DialogWorker {
	
	private LongClickDialog longClickDialog;
	private AddWordDialog addDialog;
	private Context context;
	private SettingsItemsAdapter adapter;
	private int itemId;
	
	public DialogWorker(Context context, SettingsItemsAdapter adapter) {
		this.context = context;
		this.adapter = adapter;		
		initialization();
	}
	
	private void initialization() {
		longClickDialog = new LongClickDialog(context);
		addDialog = new AddWordDialog(context);
		
		longClickDialog.button_cancel.setOnClickListener(adapter);
		longClickDialog.button_delete.setOnClickListener(adapter);
		longClickDialog.button_edit.setOnClickListener(adapter);
		
		addDialog.button_add.setOnClickListener(adapter);
		addDialog.button_cancel.setOnClickListener(adapter);
				
	}
	
	public void showLongClickDialog(String word, int id) {
		longClickDialog.setDialogWord(word);
		itemId = id;
		longClickDialog.showDialog();
	}
	
	public void showAddDialog() {
		addDialog.setLabels(Constants.DIALOG_ADD);
		addDialog.setEditFlag(false);
		addDialog.showDialog();
	}
	
	public void showEditDialog() {
		addDialog.setLabels(Constants.DIALOG_EDIT);
		addDialog.setWord(longClickDialog.getWord());
		addDialog.setEditFlag(true);
		addDialog.showDialog();
	}
	
	public void closeAddDialog() {
		addDialog.closeDialog();
	}
	
	public void closeLongClickDialog() {
		longClickDialog.closeDialog();
	}
	
	public int getRemoveItemId() {
		return itemId;
	}
	
	public String getNewWord() {
		return addDialog.getWord();
	}
	
	public boolean getEditFlag() {
		return addDialog.getEditFlag();
	}
	
	public int getItemId() {
		return itemId;
	}	
}

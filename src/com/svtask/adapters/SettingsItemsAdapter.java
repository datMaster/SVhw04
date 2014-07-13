package com.svtask.adapters;

import java.text.BreakIterator;
import java.util.ArrayList;

import com.svtask.db.DBitem;
import com.svtask.db.DBworker;
import com.svtask.dialogs.AddWordDialog;
import com.svtask.dialogs.LongClickDialog;
import com.svtask.utils.SettingsViewHolder;
import com.svtask2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsItemsAdapter extends BaseAdapter implements OnClickListener, OnLongClickListener {	

	private LayoutInflater inflater;
//	private CharSequence[] words = null;
	private ArrayList<SettingsViewHolder> holderList;
	private ArrayList<Boolean> chBoxStatusList;
	private int selectedCount = 0;
	private int selectAllCount = 0;		
	private MenuItem selectAllMenuItem;		
	private Context context;
	
	private DBworker dbWorker;
	private ArrayList<DBitem> dbItems;
	private AddWordDialog addDialog;
	private LongClickDialog longDialog;
	
	public SettingsItemsAdapter(Context context) {

		this.context =  context;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dbWorker = new DBworker(this.context);
		dbItems = dbWorker.getAllConvertedRecords();
		
		addDialog = new AddWordDialog(this.context);
		addDialog.button_add.setOnClickListener(this);
		addDialog.button_cancel.setOnClickListener(this);
		
		longDialog = new LongClickDialog(this.context);
		longDialog.button_cancel.setOnClickListener(this);
		longDialog.button_edit.setOnClickListener(this);
		longDialog.button_delete.setOnClickListener(this);

//		words = this.context.getResources().getTextArray(R.array.words);
		holderList = new ArrayList<SettingsViewHolder>();
		chBoxStatusList = new ArrayList<Boolean>();	
		int wordsCount = dbItems.size();
		selectAllCount = wordsCount + (((wordsCount - 1) * wordsCount) / 2);
		
		for (int i = 0; i < dbItems.size(); i++) {			
			holderList.add(new SettingsViewHolder());
			chBoxStatusList.add(false);
		}				
	}

	public int getSelectCount() {
		return selectedCount;
	}
	
	@Override
	public int getCount() {
		return holderList.size();
	}

	@Override
	public Object getItem(int position) {
		return holderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
				
		if (convertView == null)
			convertView = inflater.inflate(R.layout.settings_list_item, null);
					
		SettingsViewHolder holder = holderList.get(position);		
				
		holder.tvWord = (TextView)convertView.findViewById(R.id.textView_item);		
		holder.tvWord.setText(dbItems.get(position).word);
		holder.tvWord.setOnLongClickListener(this);
		holder.tvWord.setTag(position);
		holder.chBox = (CheckBox)convertView.findViewById(R.id.checkBox_active_word);
		holder.chBox.setChecked(dbItems.get(position).status);
		holder.chBox.setTag(position);		
		holder.chBox.setOnClickListener(this);	
		
		return convertView;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.button_add:
			if(addDialog.getWord() != null) {
				if(addDialog.getEditFlag()) {
					if(addDialog.getWord() != null)
						dbItems.get(addDialog.getItemId()).word = addDialog.getWord();					
				}
				else {
					DBitem newItem = new DBitem();
					newItem.word = addDialog.getWord();
					newItem.status = false;							
					dbWorker.addRecord(newItem);
					dbWorker.updateCheckedStatus(dbItems);
					dbItems = dbWorker.getAllConvertedRecords();
					holderList.add(new SettingsViewHolder());
				}
				this.notifyDataSetChanged();
			}
			
			addDialog.closeDialog();
			break;
		case R.id.button_cancel:
			addDialog.closeDialog();
			break;
		case R.id.checkBox_active_word: {
			int id = Integer.parseInt(v.getTag().toString());
			SettingsViewHolder holder = holderList.get(id);
			dbItems.get(id).status = holder.chBox.isChecked();
			if (holder.chBox.isChecked()) {
				selectedCount += (id + 1);
			} else {
				selectedCount -= (id + 1);
			}
			checkSelected();
		}
			break;
		case R.id.button_cancel_long_click_dialog : 
			longDialog.closeDialog();
			break;
		case R.id.button_edit_long_click_dialog : 
			longDialog.closeDialog();
			addDialog.setTitle(context.getResources().getString(R.string.dialog_edit_title));
			addDialog.setWord(dbItems.get(longDialog.getItemId()).word);
			addDialog.setEditFlag(true);
			addDialog.setItemId(longDialog.getItemId());
			addDialog.showDialog();
			break;
		case R.id.button_delete_long_click_dialog : 
			dbWorker.updateCheckedStatus(dbItems);
			dbWorker.removeRecord(longDialog.getItemId());
			holderList.remove(longDialog.getItemId());
			dbItems = dbWorker.getAllConvertedRecords();			
			this.notifyDataSetChanged();
			longDialog.closeDialog();
			break;
		}							
	}
	
	public void checkSelected() {
		if (selectedCount == selectAllCount) {
			selectAllMenuItem.setTitle(context.getResources().getString(R.string.unselect_all));
		} 
		else {
			selectAllMenuItem.setTitle(context.getResources().getString(R.string.select_all));
		}
			
	}
	
	private void unselectAll() {
		for (int i = 0; i < getCount(); i++) {
			SettingsViewHolder holder = holderList.get(i);
			if (holder.chBox != null) {
				holder.chBox.setChecked(false);
			}
			dbItems.get(i).status = false;
		}
		selectedCount = 0;
	}
	
	public void selectAll() {
		if (selectAllCount == selectedCount) {
			unselectAll();
		} else {
			for (int i = 0; i < getCount(); i++) {
				SettingsViewHolder holder = holderList.get(i);
				if (holder.chBox != null) {
					holder.chBox.setChecked(true);
				}
				dbItems.get(i).status = true;
			}
			selectedCount = selectAllCount;
		}
		checkSelected();
	}
	
	public void setMenu(Menu menu) {
		selectAllMenuItem = menu.findItem(R.id.select_all);
	}
	
	public void saveWordsToDB() {
		dbWorker.updateCheckedStatus(dbItems);
	}	
	
	public void addWord() {
		addDialog.setTitle(context.getResources().getString(R.string.new_word));
		addDialog.showDialog();
		addDialog.setEditFlag(false);
	}

	@Override
	public boolean onLongClick(View v) {		
		int position = Integer.parseInt(v.getTag().toString());
		longDialog.setItemId(position);
		longDialog.setDialogWord(dbItems.get(position).word);
		longDialog.showDialog();
		return false;
	}
}

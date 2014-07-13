package com.svtask.adapters;

import java.util.ArrayList;

import com.svtask.db.DBitem;
import com.svtask.db.DBworker;
import com.svtask.utils.SettingsViewHolder;
import com.svtask.utils.SharedPreferencesWorker;
import com.svtask2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SettingsItemsAdapter extends BaseAdapter implements OnClickListener {	

	private LayoutInflater inflater;
	private CharSequence[] words = null;
	private ArrayList<SettingsViewHolder> holderList;
	private ArrayList<Boolean> chBoxStatusList;
	private int selectedCount = 0;
	private int selectAllCount = 0;		
	private MenuItem selectAllMenuItem;	
	private SharedPreferencesWorker sharePreferences;
	private Context context;
	
	private DBworker dbWorker;
	private ArrayList<DBitem> dbItems;
	
	public SettingsItemsAdapter(Context context, SharedPreferencesWorker sharePreferences) {

		this.context =  context;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dbWorker = new DBworker(this.context);
		dbItems = dbWorker.getAllConvertedRecords();
		
		this.sharePreferences = sharePreferences;
		words = this.context.getResources().getTextArray(R.array.words);
		holderList = new ArrayList<SettingsViewHolder>();
		chBoxStatusList = new ArrayList<Boolean>();	
		int wordsCount = dbItems.size();
		selectAllCount = wordsCount + (((wordsCount - 1) * wordsCount) / 2);
		
		for (int i = 0; i < words.length; i++) {			
			holderList.add(new SettingsViewHolder());
			chBoxStatusList.add(false);
		}				
		loadSharedPreferences();
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
		holder.tvWord.setText(dbItems.get(position).word);// words[position]);
		
		holder.chBox = (CheckBox)convertView.findViewById(R.id.checkBox_active_word);
		holder.chBox.setChecked(dbItems.get(position).status); //chBoxStatusList.get(position));
		holder.chBox.setTag(position);		
		holder.chBox.setOnClickListener(this);	
		
		return convertView;
	}

	@Override
	public void onClick(View v) {
		int id = Integer.parseInt(v.getTag().toString());		
		SettingsViewHolder holder = holderList.get(id);		
//		chBoxStatusList.set(id, holder.chBox.isChecked());
		dbItems.get(id).status = holder.chBox.isChecked();
		if(holder.chBox.isChecked()) {			
			selectedCount += (id + 1);
		}
		else {
			selectedCount -= (id + 1);
		}
		checkSelected();				
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
//			chBoxStatusList.set(i, false);
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
//				chBoxStatusList.set(i, true);
				dbItems.get(i).status = true;
			}
			selectedCount = selectAllCount;
		}
		checkSelected();
	}
	
	public void setMenu(Menu menu) {
		selectAllMenuItem = menu.findItem(R.id.select_all);
	}
	
	public void saveSharedPreferences() {
//		sharePreferences.saveSharedPreferences(chBoxStatusList);
		dbWorker.updateCheckedStatus(dbItems);
	}
	
	public void loadSharedPreferences() {
		
//		ArrayList<Integer> allowedIndexes = sharePreferences.getAllowedWords();						
//		for(int index = 0; index < allowedIndexes.size(); index ++) {			
//			chBoxStatusList.set(allowedIndexes.get(index), true);
//			selectedCount += allowedIndexes.get(index) + 1;
//		}	
	}
}

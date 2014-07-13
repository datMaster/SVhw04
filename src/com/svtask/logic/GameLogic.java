package com.svtask.logic;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;

import com.svtask.db.DBworker;
import com.svtask.main.MainViewHolder;
import com.svtask.settings.Constants;
import com.svtask.utils.SharedPreferencesWorker;
import com.svtask2.R;

public class GameLogic {
	
	private Activity activity;
	private MainViewHolder viewHolder;
	private Random rand;	
	private CountDownTimer timer;
	private Boolean isTimerStarted = false;
	private DBworker dbWorker;
	private ArrayList<String> allowedWords;
	
	private int score;
	private int lives;	
	
	public GameLogic (Activity activity, SharedPreferencesWorker sharedPrefences, MainViewHolder viewHolder) {
		this.viewHolder = viewHolder;
		this.activity = activity;
		this.rand = new Random();
		this.dbWorker = new DBworker(activity);
		initializeSettings();
		initGameLogic();
	}
	
	private void initializeSettings() {
		timer = new CountDownTimer(Constants.TIMER_IN_FUTURE, Constants.TIMER_INTERVAL) {
			
			@Override
			public void onTick(long millisUntilFinished) {					
				if(isTimerStarted) {
					viewHolder.setTimerTex(((Integer)((int)millisUntilFinished / Constants.TIMER_INTERVAL)).toString());
				}
			}
			
			@Override
			public void onFinish() {					
				if(isTimerStarted) {						
					dead();
				}
			}
		};
		
		viewHolder.etInputSetTextChangeListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!isTimerStarted && (s.length() > 0)) {
					timer.start();
					isTimerStarted = true;
				}				
				viewHolder.tvEnteredUpdate();
				String need = viewHolder.getTvRepeatString();
				if(need.length() == count) {						
					if(need.equals(s.toString())) {
						nextWord(Constants.LIVING);
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {				

			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	
	public void initGameLogic() {
		reset();			
		if(checkAllowedWords() == true)
			nextNeedString();			
	}
	
	private boolean checkAllowedWords() {
		updateAllowedWords();
		if(allowedWords.size() == 0) {
			viewHolder.tvPleaseInputLabelHide();
			viewHolder.tvRepeatUpdate(activity.getString(R.string.help_string));
			viewHolder.etIputDisable();
			return false;
		}
		else {
			viewHolder.tvPleaseInputLabelShow();
			viewHolder.etIputEnable();	
			return true;
		}
	}
	
	private void updateAllowedWords() {
		allowedWords = dbWorker.getAllowedWords();		
	}
	
	private void reset() {
		score = Constants.SCORE_INIT;
		lives = Constants.LIVES;
		updateLives();
		updateScore();
		viewHolder.clearInputs();							
	}
	
	private void updateLives() {
		viewHolder.updateLives(activity.getString(R.string.lives) + lives);
	}
	
	private void updateScore() {
		viewHolder.updateScores(activity.getString(R.string.score) + score);
	}
	
	private void dead() {			
		if(lives < 1) {							
			viewHolder.clearInputs();
			stopTimer();
			showAlert();
		}
		else {
			lives --;
			updateLives();
			nextWord(Constants.DEADED);
		}
	}
	
	private void nextWord(Boolean deadStatus) {
		stopTimer();
		if(deadStatus == Constants.LIVING) {
			score ++;
			updateScore();							
		}
		viewHolder.clearInputs();
		nextNeedString();				
		startTimer();
	}
	
	private void nextNeedString() {				
		int id = 0;
		if (allowedWords.size() > 1)
			id = rand.nextInt(allowedWords.size() - 1);			
		viewHolder.tvRepeatUpdate(allowedWords.get(id));//words[allowedWordsIndexes.get(id)].toString());
	}
	
	private void stopTimer() {
		timer.cancel();
		isTimerStarted = false;
	}
	
	private void startTimer() {
		timer.start();
		isTimerStarted = true;
	}
	
	private void showAlert() {
		hideSoftKeyboard(activity.getApplicationContext());
		new AlertDialog.Builder(activity)
	    .setTitle(activity.getString(R.string.gameover))
	    .setMessage(activity.getString(R.string.you_got) 
	    		+ " " + score + " " 
	    		+ activity.getString(R.string.you_scores))
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            initGameLogic();
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_info)
	    .show();
	}
	
	private void hideSoftKeyboard(Context context) {
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(viewHolder.getInputWindowToken(), 0);
	}
	
	public void resume() {
		if(checkAllowedWords())
			nextNeedString();
	}
	
	public void stop() {
		if (isTimerStarted) {
			stopTimer();
			reset();
		}
	}
}

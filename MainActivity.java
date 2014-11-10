package com.example.pandorabotsapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	String client_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		client_name = prefs.getString("client_name", "");
		// set client name if there is none
		if (client_name == "") {
    		PandorabotsAPI pApi = new PandorabotsAPI(MagicParameters.hostname, MagicParameters.username, MagicParameters.userkey, "");
    		client_name = pApi.debugBot(/* insert bot name here */,"init", false, false, false, true);
    		SharedPreferences.Editor editor = prefs.edit();
    		editor.putString("client_name", client_name);
    		editor.commit();
		}
		setAskButton();
	}
	
	public void setAskButton() {
		Button askButton = (Button) findViewById(R.id.askButton);
		askButton.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				EditText editText = (EditText) findViewById(R.id.userInput);
				String message = editText.getText().toString();
				editText.setText("");
				askHumanMessage(message);
			}
		});
	}
	
	// initiates the chat with the bot
	public void askHumanMessage(String message) {
		DoRequest botChat = new DoRequest(this, client_name);
		botChat.execute(message);
		
	}
	
	/// process the bots response to id any non natural language response text
	public void processBotResponse(String result) {
	        result = removeTags(result);
	        showBotResponse(result);
	}
	
	/// display the bots response in the text view
	public void showBotResponse(String message) {
		TextView textView = (TextView) findViewById(R.id.botResponse);
		textView.setText(message);
	}
	
	private String removeTags(String string) {
		Pattern REMOVE_TAGS = Pattern.compile("<.+?>");
		
		if (string == null || string.length() == 0) {
			return string;
		}
		Matcher m = REMOVE_TAGS.matcher(string);
		return m.replaceAll("");
	}
	
}

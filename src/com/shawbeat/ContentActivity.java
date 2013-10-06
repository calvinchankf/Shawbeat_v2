package com.shawbeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ContentActivity extends ActionBarActivity {
	
	private ShareActionProvider mShareActionProvider;
	
	private String theTitle;
	private String theContent;
	private TextView ContentTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);
		
		ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        
        Intent intent=this.getIntent();
	    Bundle bundle = intent.getExtras();
	    theContent = bundle.getString("content");
	    theTitle = bundle.getString("title");
	    actionBar.setTitle(theTitle);
	    
	    ContentTxt=(TextView)findViewById(R.id.txt);
	    ContentTxt.setText(theContent);
	    ContentTxt.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            finish();
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.content, menu);
		
		// Locate MenuItem with ShareActionProvider
	    MenuItem item = menu.findItem(R.id.share);
		
	    mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareIntent(getDefaultShareIntent());
	    
		return true;
	}
	/** Returns a share intent */
    private Intent getDefaultShareIntent(){    	
    	
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, theTitle);
        intent.putExtra(Intent.EXTRA_TEXT, theContent);
        return intent;
    }

}

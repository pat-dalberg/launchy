/*
Copyright 2013 Michael DiGiovanni glass@mikedg.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
//A good 80% of this app is from the Android SDK home app sample
package com.mikedg.android.glass.launchy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    private AppHelper mAppHelper;
    
    private ArrayList<ApplicationInfo> applications = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppHelper = new AppHelper(this);
        mAppHelper.loadApplications(true);
        applications = mAppHelper.getApplications();
        mAppHelper.registerIntentReceivers();

        // setupTestReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme("package");
        registerReceiver(mPackageBroadcastReciever, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppHelper.onDestroy();
        unregisterReceiver(mPackageBroadcastReciever);
    }

    BroadcastReceiver mPackageBroadcastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mAppHelper.loadApplications(false);
        }
    };
    
    /**
     * Handle the tap event from the touchpad.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
      switch (keyCode) {
      // Handle tap events.
      case KeyEvent.KEYCODE_DPAD_CENTER:
      case KeyEvent.KEYCODE_ENTER:
    	  showAppsMenu();
        return true;
      default:
        return super.onKeyDown(keyCode, event);
      }
    }    
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	
    	menu.clear();
    	for(int i = 0;i < applications.size();i++){
    		menu.add(applications.get(i).title);
    	}   	
    	return true;
    }
    
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	String title = (String) item.getTitle();
    	for(ApplicationInfo app : applications){
    		if(title.contentEquals(app.title)){
    			startActivity(app.intent);
    		}
    	}   	
    	return true;
    }
    
    private void showAppsMenu(){
    	this.openOptionsMenu();
    }
    

}

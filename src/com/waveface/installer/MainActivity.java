package com.waveface.installer;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.waveface.installer.storage.InstallerEntity;
import com.waveface.installer.storage.InstallerResponse;
import com.waveface.installer.util.HttpInvoker;

public class MainActivity extends Activity {
	private static final String TAG = "Installer";
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView lv = (ListView)this.findViewById(R.id.list);
        Log.d(TAG, "Start invoke URL:"+Constant.SERVER_URL+"installer.json");
        String jsonOutput = HttpInvoker.getStringFromUrl(Constant.SERVER_URL+"installer.json");
		Log.d(TAG, "Start invoke URL output:"+jsonOutput);
		final InstallerEntity[] installers = new Gson().fromJson(jsonOutput, InstallerResponse.class).installers;
		
		
		InstallerAdapter adapter = new InstallerAdapter(this);
		adapter.setInstallers(new ArrayList<InstallerEntity>(Arrays.asList(installers)));
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {

				startUpdate(installers[position]);
			}
		});

    }
	private void startUpdate(InstallerEntity installer) {
		Intent intent = new Intent(this,UpdateActivity.class);
		intent.putExtra("version", installer.version);
		intent.putExtra("APK_URL", Constant.SERVER_URL+installer.apk);
		startActivity(intent);
	}

}
package com.getmemoryinfo.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.getmemoryinfo.R;
import com.getmemoryinfo.adapter.AppAdapter;
import com.getmemoryinfo.entity.AppEntity;
import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

	private TextView versionText;
	private ListView listView;
	private AppAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		versionText = (TextView) findViewById(R.id.main_text);
		versionText.setText("当前系统版本：" + android.os.Build.VERSION.RELEASE);
		
		listView = (ListView)findViewById(R.id.main_listview);
		Log.i("Smaster", "list.size===" + getAndroidProcess(this));
		adapter = new AppAdapter(this, getAndroidProcess(this));
		listView.setAdapter(adapter);
	}

	private List<AppEntity> getAndroidProcess(Context context) {
		List<AppEntity> resule = new ArrayList<AppEntity>();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		com.getmemoryinfo.AppUtils proutils = new com.getmemoryinfo.AppUtils(context);
		List<AndroidAppProcess> listInfo = ProcessManager.getRunningAppProcesses();
		if(listInfo.isEmpty() || listInfo.size() == 0){
			return null;
		}
		for (AndroidAppProcess info : listInfo) {
			ApplicationInfo app = proutils.getApplicationInfo(info.name);
			// 过滤自己当前的应用
			if (app == null || context.getPackageName().equals(app.packageName)) {
				continue;
			}
			// 过滤系统的应用
			if ((app.flags & app.FLAG_SYSTEM) > 0) {
				continue;
			}
			AppEntity ent = new AppEntity();
			ent.setAppIcon(app.loadIcon(pm));//应用的图标
			ent.setAppName(app.loadLabel(pm).toString());//应用的名称
			ent.setPackageName(app.packageName);//应用的包名
			// 计算应用所占内存大小
			int[] myMempid = new int[] { info.pid };
			Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);

			/*double memSize = memoryInfo[0].dalvikPrivateDirty / 1024.0;
			int temp = (int) (memSize * 100);
			memSize = temp / 100.0;*/

			double memSize = memoryInfo[0].getTotalPss() / 1024;
			ent.setMemorySize(memSize);//应用所占内存的大小
			
			resule.add(ent);
		}
		return resule;
	}
}

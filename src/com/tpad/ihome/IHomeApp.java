package com.tpad.ihome;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint; 
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.tpad.ihome.business.BaseActivity;

public class IHomeApp extends Application implements CrashListener
{
	static
	{
		// System.loadLibrary("unios");
//		System.loadLibrary("ihome");
	}

	// drawable : prefix_clsname_func_[state]
	// xml : ctlname_clsname_func

	// ////////////////////////////////////////////////////////////////////
	// (((((((((((((((((((((((((( BaseAct Fields ))))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	public static final int WIDTH_COMMUNICATION_PREVIEW = 640;
	public static final int HEIGHT_COMMUNICATION_PREVIEW = 480;

	public static final String IHOME_ATTR_WIDTH = "attr-screenWidth";
	public static final String IHOME_ATTR_HEIGHT = "attr-screenHeight";
	public static final String IHOME_ATTR_BAR_HEIGHT = "attr-barHeight";
	public static final String IHOME_ATTR_DENSITY = "attr-density";
	public static final String IHOME_ATTR_DENSITY_DPI = "attr-densityDpi";

	// To store some of the properties
	Map<String, Object> ihome_maps = new HashMap<String, Object>();

	Map<String, Object> ihome_tags = new HashMap<String, Object>();

	Map<String, BaseActivity> ihome_acts = new HashMap<String, BaseActivity>();

	// ////////////////////////////////////////////////////////////////////
	// (((((((((((((((((((((((((( Override Method )))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	CrashHandler mCrashHandler;

	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.e("", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Application onCreate!!!<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		// mCrashHandler = CrashHandler.getInstance();
		// mCrashHandler.init(this);
		// mCrashHandler.setReportServer(true);
		// mCrashHandler.setCrashListener(this);
		init();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCatch(final String error)
	{
		final String showMessage = getString(R.string.error_unknow_exit);

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				Looper.prepare();
				Toast.makeText(IHomeApp.this, showMessage, Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}).start();
	}

	@Override
	public void onNeedClose()
	{
		File[] files = HelperUtils.getContextFiles(this, CrashHandler.CRASH_REPORTER_PREFIX, CrashHandler.CRASH_REPORTER_EXTENSION);
		for (File file : files)
		{
			Log.e("", String.format(">>>>>>>>>> delete %s", file.getName()));
			file.delete();
		}

		forceClose();
	}

	@Override
	public void onNeedCloseWithoutDelete()
	{
		Log.e("", ">>>>>>>>>> onNeedCloseWithoutDelete !");
		forceClose();
	}

	// ////////////////////////////////////////////////////////////////////
	// (((((((((((((((((((((((((( Private Method ))))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	private void init()
	{
		DisplayMetrics dm = getDM();
		ihome_maps.put(IHOME_ATTR_HEIGHT, dm.heightPixels);
		ihome_maps.put(IHOME_ATTR_WIDTH, dm.widthPixels);
		ihome_maps.put(IHOME_ATTR_DENSITY, dm.density);
		ihome_maps.put(IHOME_ATTR_DENSITY_DPI, dm.densityDpi);
		ihome_maps.put(IHOME_ATTR_BAR_HEIGHT, getBarHeight());
	}

	private int getBarHeight()
	{
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 38;

		try
		{
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = getResources().getDimensionPixelSize(x);
		} catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return sbar;
	}

	private DisplayMetrics getDM()
	{
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	private void forceClose()
	{
		List<Map.Entry<String, BaseActivity>> acts = new ArrayList<Map.Entry<String, BaseActivity>>(ihome_acts.entrySet());

		int size = acts.size();
		for (int i = 0; i < size; i++)
		{
			BaseActivity act = acts.get(i).getValue();
			Log.e("", String.format(">>>>>>>>>>>>> %s will close!!!", act.getClass().getName()));
			act.forceClose(i == (size - 1));
		}

		android.os.Process.killProcess(android.os.Process.myPid());
		ActivityManager activityMgr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityMgr.killBackgroundProcesses(getPackageName());
		System.exit(0);
	}

	// ////////////////////////////////////////////////////////////////////
	// (((((((((((((((((((((((((( Public Method ))))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	/** The width of screen */
	public int getScreenWidth()
	{
		return (Integer) ihome_maps.get(IHOME_ATTR_WIDTH);
	}

	/** The height of screen */
	public int getScreenHeight()
	{
		return (Integer) ihome_maps.get(IHOME_ATTR_HEIGHT);
	}

	/** The height of status-bar */
	public int getStatusbarHeight()
	{
		return (Integer) ihome_maps.get(IHOME_ATTR_BAR_HEIGHT);
	}

	/** To add the tag width tag-name */
	public void addTag(String tagName, Object tag)
	{
		ihome_tags.put(tagName, tag);
	}

	/** To get the tag by tag-name */
	public Object getTag(String tagName)
	{
		return ihome_tags.get(tagName);
	}

	/** To remove the tag by tag-name */
	public Object removeTag(String tagName)
	{
		return ihome_tags.remove(tagName);
	}

	public void join(String name, BaseActivity activity)
	{
		ihome_acts.put(name, activity);
	}

	public void exit(String name)
	{
		ihome_acts.remove(name);
	}

	public boolean check_camera()
	{
		return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	public boolean check_mic()
	{
		return getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
	}

	public boolean check_camera_led()
	{
		return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}

	@SuppressLint("InlinedApi")
	public boolean check_ble()
	{
		return getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
	}

	// ////////////////////////////////////////////////////////////////////
	// ((((((((((((((((((((((((((( Inner Classes ))))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	public class IHomeAttr
	{
		public int screenWidth;
		public int screenHeight;
		public int statusbar_height;

		public float density;
		public int densityDpi;
	}
}

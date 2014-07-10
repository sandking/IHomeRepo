package com.tpad.ihome.serv;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

public abstract class IService extends Service
{
	static
	{
		System.loadLibrary("razem");   
	}

	public final static boolean _DBG_ = true;
	public static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

	private NetStateChangedListener net_changed_listener;
	private ConnectivityManager mConnectivityManager; 
      
	private FileHelper mFileHelper;
   
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate()
	{
		super.onCreate();

		mFileHelper = new FileHelper();
		mFileHelper.exec();

		printf("Service Created !!!");

		mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

		Notification note = new Notification(0, null, System.currentTimeMillis());
		note.flags |= Notification.FLAG_NO_CLEAR;
		startForeground(42, note);

		register_netlistener();

	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		printf("Service Destroyed !!!");

		unregister_netlistener();
	}

	protected abstract boolean onNetConnected(NetworkInfo infor);

	protected abstract boolean onNetDisconnected();

	void register_netlistener()
	{
		if (net_changed_listener == null)
		{
			net_changed_listener = new NetStateChangedListener();
			IntentFilter filter = new IntentFilter();
			filter.addAction(ACTION_CONNECTIVITY_CHANGE);
			filter.setPriority(Integer.MAX_VALUE);
			registerReceiver(net_changed_listener, filter);
		}
	}

	void unregister_netlistener()
	{
		if (net_changed_listener != null)
		{
			unregisterReceiver(net_changed_listener);
			net_changed_listener = null;
		}
	}

	public NetworkInfo getActiveNetwork()
	{
		return mConnectivityManager.getActiveNetworkInfo();
	}

	void printf(String msg, Object... args)
	{
		if (_DBG_)
		{
			String str = String.format("! >>>>>>>>> %s", String.format(msg, args));

			Log.e(getClass().getSimpleName(), str);

			mFileHelper.request_save_log(str);
		}
	}

	void checkVM(String str)
	{
		printf("%s  { PID : %d  ,  TID : %d  ,  UID : %d }", str, Process.myPid(), Process.myTid(), Process.myUid());
	}

	private final class NetStateChangedListener extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();

			if (TextUtils.equals(action, ACTION_CONNECTIVITY_CHANGE))
			{
				NetworkInfo network_infor = getActiveNetwork();

				if (network_infor == null ? onNetDisconnected() : onNetConnected(network_infor))
					return;
			}
		}
	}


}

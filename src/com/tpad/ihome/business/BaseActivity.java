package com.tpad.ihome.business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.tpad.ihome.IHomeApp;
import com.tpad.ihome.serv.IHomeService;
import com.tpad.ihome.utils.FileReaderHelper;

public abstract class BaseActivity extends Activity implements Callback
{
	// ////////////////////////////////////////////////////////////////////
	// (((((((((((((((((((((((((( Constant Fields ))))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	public final static boolean _DBG_ = true;

//	public final static boolean _TEST_CALL_OUT_ = false;
//	public final static boolean _TEST_CALL_IN_ = false;

	// public final static boolean _TEST_CALL_IN_ = !_TEST_CALL_OUT_;

	public static int count = 0;

	public static boolean is_service_start = false;

	// ////////////////////////////////////////////////////////////////////
	// (((((((((((((((((((((((((( BaseAct Fields ))))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	private IHomeApp ihomeApp = null;

	protected Intent serviceIntent = null;
	protected Handler mHandler = null;

	// ////////////////////////////////////////////////////////////////////
	// (((((((((((((((((((((((((( Override Method )))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		ihomeApp = (IHomeApp) getApplication();
		ihomeApp.join(this.getClass().getName(), this);

		serviceIntent = new Intent();
		serviceIntent.setClass(this, IHomeService.class);

		mHandler = new Handler(this);

		if (!is_service_start)
		{
			start_service();
			is_service_start = true;
		}
	}

	@Override
	public void finish()
	{
		super.finish();
		ihomeApp.exit(this.getClass().getName());
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		return false;
	}

	// ////////////////////////////////////////////////////////////////////
	// ((((((((((((((((((( Private & Protected Method )))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	protected void start_service()
	{
		start_service(new Bundle());
	}

	protected void start_service(Bundle bundle)
	{
		serviceIntent.putExtras(bundle);
		startService(serviceIntent);
	}

	protected void stop_service()
	{
		stopService(serviceIntent);
	}

	protected void post_ui(Runnable runn)
	{
		mHandler.post(runn);
	}

	protected void post_delay(Runnable runn, long delay)
	{
		mHandler.postDelayed(runn, delay);
	}

	protected void remove_run(Runnable runn)
	{
		mHandler.removeCallbacks(runn);
	}

	protected void send_message(Message msg, long delayMillis)
	{
		mHandler.sendMessageDelayed(msg, delayMillis);
	}

	protected void send_message(Message msg)
	{
		mHandler.sendMessage(msg);
	}

	protected byte[] readFromRaw(int rid)
	{
		return FileReaderHelper.readFromRaw(this, rid);
	}

	// ////////////////////////////////////////////////////////////////////
	// (((((((((((((((((((((((((( Public Method ))))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	/**
	 * Force close the current activity.  
	 * 
	 * @param flag
	 *            if true - {@code System.exit(0)}.
	 */
	public void forceClose(boolean flag)
	{
		finish();
		if (flag)
			System.exit(0);
	}

	/**
	 * Get the width of screen.
	 * 
	 * @return width.
	 */
	public int getScreenWidth()
	{
		return ihomeApp.getScreenWidth();
	}

	/**
	 * Get the height of screen.
	 * 
	 * @return height.
	 */
	public int getScreenHeight()
	{
		return ihomeApp.getScreenHeight();
	}

	/**
	 * Get the height of status-bar.
	 * 
	 * @return height.
	 */
	public int getStatusbarHeight()
	{
		return ihomeApp.getStatusbarHeight();
	}

	/**
	 * To add the tag with tag-name.
	 * 
	 * @param tagName
	 *            tagName
	 * @param tag
	 *            tag.
	 */
	public void addTag(String tagName, Object tag)
	{
		ihomeApp.addTag(tagName, tag);
	}

	/**
	 * To get the tag by tag-name.
	 * 
	 * @param tagName
	 *            tagName.
	 * @return the specified tag.
	 */
	public Object getTag(String tagName)
	{
		return ihomeApp.getTag(tagName);
	}

	/**
	 * To remove the tag by tag-name.
	 * 
	 * @param tagName
	 *            tagName.
	 * @return the removed tag.
	 */
	public Object removeTag(String tagName)
	{
		return ihomeApp.removeTag(tagName);
	}

	public void playToast(String str, Object... args)
	{
		Toast.makeText(this, String.format(str, args), Toast.LENGTH_SHORT).show();
	}

	public void goto_act(Bundle bundle, Class<?> cls)
	{
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), cls);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public Bundle get_bundle()
	{
		Intent intent = getIntent();

		return intent == null ? null : intent.getExtras();
	}

	// ////////////////////////////////////////////////////////////////////
	// ((((((((((((((((((((((((((( Inner Classes ))))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	public void printf(String msg, Object... args)
	{
		if (_DBG_)
			Log.e(getClass().getSimpleName(), String.format(msg, args));
	}

	public void checkVM(String str)
	{
		printf("%s  { PID : %d  ,  TID : %s - %d  ,  UID : %d }", str, Process.myPid(), Thread.currentThread().getName(), Thread.currentThread().getId(), Process.myUid());
	}
}

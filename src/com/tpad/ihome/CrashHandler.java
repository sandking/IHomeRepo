package com.tpad.ihome;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Properties;

import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

public class CrashHandler implements UncaughtExceptionHandler, TransportListener
{
	public static final String TAG = "IHomeCrashHandler";   

	/**  
	 * Whether the debug mode.
	 */     
	public static final boolean DEBUG = false;
    
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private static CrashHandler INSTANCE;
	private Context mContext;

	/******************** Error Stack Info ********************/
	private Properties mDeviceCrashInfo = new Properties();
	private static final String VERSION_NAME = "versionName";   
	private static final String VERSION_CODE = "versionCode";
	private static final String STACK_TRACE = "STACK_TRACE";

	/******************** Error File bak **********************/
	public static final String CRASH_REPORTER_PREFIX = "crash_";
	public static final String CRASH_REPORTER_EXTENSION = ".cr";

	private static final String EMAIL_BUG_TITLE_PREFIX = "BUG-";
	private static final String EMAIL_BUG_CONTENT = "BUG";
	private static final String EMAIL_BUG_RECEIVER = "sk.jinxing@gmail.com";

	/******************** Report Server **********************/
	private JavaMailWithAttachment mJavaMailWithAttachment;
	private CrashListener mCrashListener;
	private boolean isReport;

	private CrashHandler()
	{
	}

	public static CrashHandler getInstance()
	{
		if (INSTANCE == null) 
		{
			INSTANCE = new CrashHandler();
		}
		return INSTANCE;
	}

	public void setReportServer(boolean report)
	{
		isReport = report;
	}

	public void setCrashListener(CrashListener l)
	{
		mCrashListener = l;
	}

	/**
	 * init catch handler
	 * 
	 * @param ctx
	 */
	public void init(Context ctx)
	{
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);

		mJavaMailWithAttachment = new JavaMailWithAttachment(false);
		mJavaMailWithAttachment.addTransportListener(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex)
	{
		if (!handleException(ex) && mDefaultHandler != null)
		{
			mDefaultHandler.uncaughtException(thread, ex);
		}
	}

	/**
	 * handle all the un-do error.
	 * 
	 * @param ex
	 * @return if true - had handle the error.
	 */
	private boolean handleException(Throwable ex)
	{
		if (ex == null) return true;

		mCrashListener.onCatch(ex.getLocalizedMessage());

		collectCrashDeviceInfo(mContext);
		saveCrashInfoToFile(ex);

		if (isReport) sendCrashReportsToServer(mContext);   

		return true;
	}

	/**
	 * send the error info manually.
	 */
	public void sendPreviousReportsToServer()
	{
		sendCrashReportsToServer(mContext);
	}

	/**
	 * send the error info to server
	 *   
	 * @param ctx
	 */
	private void sendCrashReportsToServer(Context ctx)
	{    
		File[] crFiles = getCrashReportFiles(ctx);

		if (crFiles != null && crFiles.length > 0)
		{
			postReport(crFiles);
		}        
	}   

	private boolean postReport(File[] files)
	{
		report(files);
		return isReport;
	}

	private void report(final File[] files)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				mJavaMailWithAttachment.doSendHtmlEmail(EMAIL_BUG_TITLE_PREFIX + HelperUtils.obtainCurrTime("yyyy:MM:DD HH:mm:ss"), EMAIL_BUG_CONTENT, EMAIL_BUG_RECEIVER, files);
			}
		}).start();
	} 

	/**
	 * get the error-info files.
	 * 
	 * @param ctx
	 * @return
	 */   
	private File[] getCrashReportFiles(Context ctx)
	{
		return HelperUtils.getContextFiles(ctx, CRASH_REPORTER_PREFIX, CRASH_REPORTER_EXTENSION);
	}

	/**
	 * save the error info to file.
	 * 
	 * @param ex
	 * @return
	 */
	private String saveCrashInfoToFile(Throwable ex)
	{
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);

		Throwable cause = ex.getCause();
		while (cause != null)
		{
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}

		String result = info.toString();
		printWriter.close();
		mDeviceCrashInfo.put(STACK_TRACE, result);
		String fileName = "";
		try
		{
			long timestamp = System.currentTimeMillis();
			fileName = CRASH_REPORTER_PREFIX + timestamp + CRASH_REPORTER_EXTENSION;
			
			FileOutputStream trace = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
			
			mDeviceCrashInfo.store(trace, "");
			trace.flush();
			trace.close();
			return fileName;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e(TAG, "an error occured while writing report file..." + fileName, e);
		}
		return null;
	}

	/**
	 * collect the device info.
	 * 
	 * @param ctx
	 */
	public void collectCrashDeviceInfo(Context ctx)
	{
		try
		{
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null)
			{
				mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
				mDeviceCrashInfo.put(VERSION_CODE, String.valueOf(pi.versionCode));
			}
		}
		catch (NameNotFoundException e)
		{
			Log.e(TAG, "Error while collect package info", e);
		}

		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields)
		{
			try
			{
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(), field.get(null).toString());
				if (DEBUG)
				{
					Log.d(TAG, field.getName() + " : " + field.get(null));
				}
			}
			catch (Exception e)
			{
				Log.e(TAG, "Error while collect crash info", e);
			}
		}
	}

	@Override
	public void messageDelivered(TransportEvent arg0)
	{
		Log.e("", ">>>>>>>>>>> messageDelivered:" + arg0);
		mCrashListener.onNeedClose();
	}

	@Override
	public void messageNotDelivered(TransportEvent arg0)
	{
		Log.e("", ">>>>>>>>>>> messageNotDelivered:" + arg0);
		mCrashListener.onNeedCloseWithoutDelete();
	}

	@Override
	public void messagePartiallyDelivered(TransportEvent arg0)
	{
		Log.e("", ">>>>>>>>>>> messagePartiallyDelivered:" + arg0);
	}
}
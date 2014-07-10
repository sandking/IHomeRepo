package com.tpad.ihome.serv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.tpad.ihome.entity.AccountInfor;

@SuppressLint("SdCardPath")
public final class FileHelper
{
	private final static int FLAG_SAVE_LOGGER = 0x10;
	private final static int FLAG_SAVE_ACCOUNT = 0x20;

	private final static String DIR_IHOME_ROOT = "/mnt/sdcard/ihome";
	private final static String DIR_CONFIG = "config";
	private final static String DIR_LOG = "log";

	private final static String FILE_LOG = "iservice.log";
	private final static String FILE_ACCOUNTER = "accounter.cfg";

	private TCardWriterThread mWriterThread = null;
	private Handler mWriterHandler = null;

	public void exec()
	{
		mWriterThread = new TCardWriterThread("TCardWriter-Thread");
		mWriterThread.start();
	}

	public void quit()
	{
		if (mWriterThread != null)
		{
			mWriterThread.quit();
			mWriterThread = null;
		}

		mWriterHandler = null;
	}

	public void request_save_log(String str)
	{
		if (mWriterHandler != null)
			mWriterHandler.sendMessage(mWriterHandler.obtainMessage(FLAG_SAVE_LOGGER, str));
		else
			save_log(str);
	}

	public void request_save_account(AccountInfor infor)
	{
		if (mWriterHandler != null)
		{
			mWriterHandler.sendMessage(mWriterHandler.obtainMessage(FLAG_SAVE_ACCOUNT, infor));
		}
	}

	private void save_account(AccountInfor infor)
	{

	}

	private void save_log(String str)
	{
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(String.format("%s/%s/%s", DIR_IHOME_ROOT, DIR_LOG, FILE_LOG), true);

			String msg = String.format("[%s]   %s \r\n", obtainTime(), str);

			fos.write(msg.getBytes());

			fos.flush();

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (fos != null)
					fos.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	private String obtainTime()
	{  
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return sDateFormat.format(new java.util.Date());
	}

	private void printf(String msg, Object... args)
	{
		String str = String.format("! >>>>>>>>> %s", String.format(msg, args));

		Log.e(getClass().getSimpleName(), str);
	}

	private final class TCardWriterThread extends HandlerThread implements Callback
	{
		public TCardWriterThread(String name)
		{
			super(name);
		}

		private void initFiles()
		{
			mkdir(String.format("%s", DIR_IHOME_ROOT));
			mkdir(String.format("%s/%s", DIR_IHOME_ROOT, DIR_CONFIG));
			mkdir(String.format("%s/%s", DIR_IHOME_ROOT, DIR_LOG));
		}

		private void mkdir(String path)
		{
			File file = new File(path);

			if (!file.exists())
			{
				printf("File - %s mkdir", file.getPath());
				file.mkdir();
			}
		}   

		@Override
		protected void onLooperPrepared()
		{
			super.onLooperPrepared();

			initFiles();

			mWriterHandler = new Handler(getLooper(), this);
		}

		@Override
		public boolean handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case FLAG_SAVE_LOGGER:
				save_log(msg.obj.toString());
				break;
			case FLAG_SAVE_ACCOUNT:
				final AccountInfor infor = (AccountInfor) msg.obj;
				save_account(infor);
				break;
			}
			return false;
		}
	}
}

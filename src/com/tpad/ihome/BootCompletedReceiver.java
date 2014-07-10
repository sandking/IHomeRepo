package com.tpad.ihome;

import com.tpad.ihome.serv.IHomeService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		{
			Intent execService = new Intent();
			
			execService.setClass(context, IHomeService.class);
			
			context.startService(execService);
		}
	}
}

package com.tpad.ihome;

public interface CrashListener
{
	void onCatch(String error);
	
	void onNeedClose();
	 
	void onNeedCloseWithoutDelete();
}
package com.tpad.ihome.serv;

interface IMemberChangedCallback
{
	void onMemberAcquired(int accountid, String id, String title, int callstatus, inout byte[] icon);
	
	void onMemberOnlineStateChanged(int account_id,int online_offline);
	
	void onMemberChanged(int account_id,int changed_bits);
}
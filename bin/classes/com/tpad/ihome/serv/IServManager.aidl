package com.tpad.ihome.serv;

import com.tpad.ihome.serv.ILoginCallback;
import com.tpad.ihome.serv.IPhoneStateCallback;
import com.tpad.ihome.serv.IMemberChangedCallback;
import com.tpad.ihome.serv.IEventCallback;

interface IServManager 
{
	void rzLogin(String addr, int account, String passwd);
	
	int rzGetLoginState();
	
	void rzLogout();
	
	void rzSetILoginCallback(ILoginCallback callback);
	
	void rzSetIPhoneStateCallback(IPhoneStateCallback callback);
	
	void rzSetIMemberChangedCallback(IMemberChangedCallback callback);
	
	void rzSetIEventCallback(IEventCallback callback);
	
	void rzSetInfor(int set_bits, String id, String title, int call_status,inout byte[] icon_buf); 
	
	void rzGetMemberList();

	void rzGetMemberInfo(int account , int update_bits);

	void rzCall(int targetid);
	
	void rzAnswer();
	
	void rzReject();
}  
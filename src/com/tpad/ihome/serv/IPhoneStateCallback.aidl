package com.tpad.ihome.serv;

interface IPhoneStateCallback
{
	void onCallSuccess(String ip , int port,String key,int link_dir,int udp_socket);
	
	void onCallFailed(int state);
}
package com.tpad.ihome.serv;

interface ILoginCallback
{
	void onLoginSuccess();
	
	void onLoginFailed(int state);
}
package com.tpad.ihome.entity;

public enum LoginResult
{
	_RAZEM_LOGIN_RESULT_SUCCESS("登录成功!"),	
	_RAZEM_LOGIN_RESULT_SOCKET_ERROR("网络连接错误"),
	_RAZEM_LOGIN_RESULT_SERVER_NOT_ONLINE("网络连接错误"),
	_RAZEM_LOGIN_RESULT_SOCKET_COMM_FAIL("网络连接错误"),
	_RAZEM_LOGIN_RESULT_GIVEUP("网络连接错误"),
	_RAZEM_LOGIN_RESULT_ACCOUNT_INVALID("账号或者密码无效"),
	_RAZEM_LOGIN_RESULT_ACCOUNT_DUPLICATE("账号已经登录"),
	_RAZEM_LOGIN_RESULT_ACCOUNT_BLOCKED("账号被禁用");

	private final String desc;
	
	private LoginResult(String d)    
	{
		desc = d;
	}
	
	public String getDesc()
	{
		return desc;
	}
}

package com.tpad.ihome.entity;

public class AccountInfor
{
	private String ip_addr;
	private int account;
	private String pwd;

	public AccountInfor(String ip_addr, int account, String pwd)
	{
		this.ip_addr = ip_addr;
		this.account = account;
		this.pwd = pwd;
	}

	public String getIp_addr()
	{
		return ip_addr;
	}

	public void setIp_addr(String ip_addr)
	{
		this.ip_addr = ip_addr;
	}

	public int getAccount()
	{
		return account;
	}

	public void setAccount(int account)
	{
		this.account = account;
	}

	public String getPwd()
	{
		return pwd;
	}

	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}

}

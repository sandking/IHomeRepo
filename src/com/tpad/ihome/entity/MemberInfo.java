package com.tpad.ihome.entity;

public class MemberInfo
{
	private int accountId;
	private String nickName;
	private String title;
	private int callstatus;
	private byte[] icon;

	public MemberInfo(int accountId, String nickName, String title, int callstatus, byte[] icon)
	{
		this.accountId = accountId;
		this.nickName = nickName;
		this.title = title;
		this.callstatus = callstatus;
		this.icon = icon;
	}
    
	public int getAccountId()
	{
		return accountId;
	}

	public void setAccountId(int accountId)
	{
		this.accountId = accountId;
	}

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public int getCallstatus()
	{
		return callstatus;
	}

	public void setCallstatus(int callstatus)
	{
		this.callstatus = callstatus;
	}

	public byte[] getIcon()
	{
		return icon;
	}

	public void setIcon(byte[] icon)
	{
		this.icon = icon;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof MemberInfo)
		{
			MemberInfo info = (MemberInfo) o;
			if (this.accountId == info.accountId)
				return true;
		}
		return false;
	}
}

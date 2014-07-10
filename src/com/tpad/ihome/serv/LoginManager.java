package com.tpad.ihome.serv;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.tpad.ihome.entity.AccountInfor;
import com.tpad.ihome.entity.LoginResult;
import com.tpad.ihome.inter.SVConnect;

public class LoginManager implements Callback
{
	// type of runnable to work
	private final static int FLAG_LOGIN_FIRST = 0x10;
	private final static int FLAG_LOGIN_RETRY = 0x20;

	private final static long TIME_BASE_FOR_RELOGIN = 3 * 1000;
	private final static long TIME_SPACE_FOR_RELOGIN = TIME_BASE_FOR_RELOGIN;

	private final int MAX_REQUEST_RETRY_COUNT = 3;

	private LoginState state_login; // 当前登录状态
	private boolean is_manual; // 手动登录
	private int count_retry; // 当前重新登录次数

	protected RequestLoginThread mRequestLoginThread;
	protected Handler mRequestHandler;
	protected LoginCallback mLoginCallback;

	private final byte[] lock_login = new byte[0];
	private final byte[] lock_manual = new byte[0];

	public LoginManager()
	{
		count_retry = 0;
		state_login = LoginState.OFFLINE;
	}

	public void exec()
	{
		mRequestLoginThread = new RequestLoginThread("RequestLogin-Thread");
		mRequestLoginThread.start();
	}

	public void setManual(boolean manual)
	{
		synchronized (lock_manual)
		{
			is_manual = manual;
		}
	}

	public void setLoginCallback(LoginCallback callback)
	{
		mLoginCallback = callback;
	}

	public void setLoginState(LoginState state)
	{
		state_login = state;
	}

	public LoginState getLoginState()
	{
		return state_login;
	}

	public void request_login(AccountInfor infor)
	{
		synchronized (lock_login)
		{
			// remove task [retry login] .
			if (state_login == LoginState.TRYING)
			{
				printf("Request give up !  LoginState : %s !", state_login);
				return;
			}

			if (mRequestHandler.hasMessages(FLAG_LOGIN_RETRY))
			{
				printf("Remove retry task . Login by manual !");
				mRequestHandler.removeMessages(FLAG_LOGIN_RETRY);
			}

			mRequestHandler.sendMessage(mRequestHandler.obtainMessage(FLAG_LOGIN_FIRST, infor));
		}
	}

	private void request_retry_login(AccountInfor infor)
	{
		if (count_retry > MAX_REQUEST_RETRY_COUNT)
		{
			printf("Retry give up !  retry count  > %d !", MAX_REQUEST_RETRY_COUNT);
			count_retry = 0;
			return;
		}

		if (state_login == LoginState.ONLINE)
		{
			printf("Retry give up !  LoginState : %s !", state_login);
			count_retry = 0;
			return;
		}

		if (state_login == LoginState.TRYING)
		{
			printf("Retry give up !  LoginState : %s !", state_login);
			return;
		}

		final long delayTime = retry_delay(count_retry);

		printf("Request retry login - after %s !!", delayTime);

		mRequestHandler.sendMessageDelayed(mRequestHandler.obtainMessage(FLAG_LOGIN_RETRY, infor), delayTime);
	}

	private long retry_delay(int relogin)
	{
		return TIME_BASE_FOR_RELOGIN + relogin * TIME_SPACE_FOR_RELOGIN;
	}

	private int login(AccountInfor infor)
	{
		if (infor == null)
			return -1;

		state_login = LoginState.TRYING;

		printf("Login [%s] -> [%s %d %s]", Thread.currentThread().getName(), infor.getIp_addr(), infor.getAccount(), infor.getPwd());

		return SVConnect.login(infor.getIp_addr(), infor.getAccount(), infor.getPwd());
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		final AccountInfor infor = (AccountInfor) msg.obj;

		int login_ret = login(infor);

		switch (msg.what)
		{
		case FLAG_LOGIN_FIRST:
			count_retry = 0;
			break;
		case FLAG_LOGIN_RETRY:
			count_retry++;
			break;
		}

		if (login_ret == -1)
			return false;

		if (login_ret >= LoginResult.values().length)
		{
			printf("LoginRet > results length !!");
			return false;
		}

		LoginResult event = LoginResult.values()[login_ret];

		switch (event)
		{  
		case _RAZEM_LOGIN_RESULT_SUCCESS:
			state_login = LoginState.ONLINE;

			printf("Login Success !!!");
			if (mLoginCallback != null)
				mLoginCallback.onLoginSuccess();
			break;
		default:
			state_login = LoginState.OFFLINE;
			printf("Login Failed - %s !!!", event);
			if (is_manual) 
			{
				if (mLoginCallback != null)
					mLoginCallback.onLoginFailed(event.ordinal());
			} else
				request_retry_login(infor);
			break;
		}
		return false;
	}

	void printf(String msg, Object... args)
	{
		String str = String.format("! >>>>>>>>> %s", String.format(msg, args));
		Log.e(getClass().getSimpleName(), str);
	}

	public static enum LoginState
	{
		OFFLINE, ONLINE, TRYING;
	}

	public final class RequestLoginThread extends HandlerThread
	{
		public RequestLoginThread(String name)
		{
			super(name);
		}

		@Override
		protected void onLooperPrepared()
		{
			super.onLooperPrepared();
			mRequestHandler = new Handler(getLooper(), LoginManager.this);
		}
	}

	public static interface LoginCallback
	{
		void onLoginSuccess();

		void onLoginFailed(int login_ret);
	}
}

package com.tpad.ihome.serv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.tpad.ihome.business.ResponseAct;
import com.tpad.ihome.entity.AccountInfor;
import com.tpad.ihome.inter.RZCallStateListener;
import com.tpad.ihome.inter.RZEventCallback;
import com.tpad.ihome.inter.RZInitCompleteListener;
import com.tpad.ihome.inter.RZMemberChangedListener;
import com.tpad.ihome.inter.RZ_EVENT;
import com.tpad.ihome.inter.SVConnect;
import com.tpad.ihome.serv.LoginManager.LoginCallback;
import com.tpad.ihome.serv.LoginManager.LoginState;

public class IHomeService extends IService implements RZInitCompleteListener, RZCallStateListener, RZMemberChangedListener, RZEventCallback
{
	private IPhoneStateCallback mPhoneCallback;
	private IMemberChangedCallback mMemberCallback;
	private ILoginCallback mLoginCallback;
	private IEventCallback mEventCallback;

	private ServManager servBinder;
	private LoginManager mLoginManager;
	private AccountInfor mAccountInfor;

	private final byte[] lock_bind = new byte[0];

	final LoginCallback callback = new LoginCallback()
	{
		@Override
		public void onLoginSuccess()
		{
			try
			{
				if (mLoginCallback != null)
					mLoginCallback.onLoginSuccess();
			} catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void onLoginFailed(int login_ret)
		{
			try
			{
				if (mLoginCallback != null)
					mLoginCallback.onLoginFailed(login_ret);
			} catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onCreate()
	{
		super.onCreate();

		servBinder = new ServManager();

		mLoginManager = new LoginManager();
		mLoginManager.setLoginCallback(callback);
		mLoginManager.exec();

		final Runnable init_runn = new Runnable()
		{
			@Override
			public void run()   
			{
				SVConnect.init(IHomeService.this);
			}
		};

		new Thread(init_runn, "SVConnect-Thread").start();
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		printf("Connect bind service , Initialize !!!");

		return servBinder;
	}

	@Override
	public void onRebind(Intent intent)
	{
		super.onRebind(intent);

		printf("Connect rebind service , Initialize !!!");
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		mLoginManager.setManual(false);

		mEventCallback = null;
		mLoginCallback = null;
		mMemberCallback = null;
		mPhoneCallback = null;

		printf("Disconnect bind service , Release callbacks !!!");

		return true;
	}

	@Override
	public void onInitCompleted()
	{
		printf("onInitCompleted !!!");
		
		SVConnect.setCacheRootPath(getCacheDir().getAbsolutePath());
		SVConnect.setCallStateListener(this);
		SVConnect.setMemberChangedListener(this);
		SVConnect.setEventCallback(this);

		// printf("cache path : %s", getCacheDir().getAbsolutePath());

//		try
//		{
//			File file = new File(String.format("%s/%s", getCacheDir().getAbsolutePath(), "hello.config"));
//
//			if (file.exists())
//			{
//				printf("hello exist");
//				return;
//			}  
//			
//			
//			printf("%s",getExternalCacheDir().getAbsolutePath());
//			
//			printf("hello not exist");
//
//			FileOutputStream fos = new FileOutputStream(file);
//			fos.write(new byte[] { 0x01 });
//			fos.flush();
//			fos.close();
//
//		} catch (FileNotFoundException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally
//		{
//		}
	}

	@Override
	protected boolean onNetConnected(NetworkInfo infor)
	{  
		printf("onNetConnected !!!");
		if (mAccountInfor != null)

			mLoginManager.request_login(mAccountInfor);
		return true;
	}
  
	@Override
	protected boolean onNetDisconnected()
	{
		printf("onNetDisconnected !!!");

		return true;
	}

	@Override
	public void onReceiveEvent(int event)
	{
		RZ_EVENT rz_event = RZ_EVENT.values()[event];

		switch (rz_event)
		{
		case SERVER_SHUTDOWN:
			mLoginManager.setLoginState(LoginState.OFFLINE);

			if (mAccountInfor != null)
				mLoginManager.request_login(mAccountInfor);
			break;
		default:
			break;
		}

		try
		{
			if (mEventCallback != null)
				mEventCallback.onReceiveEvent(event);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onCallIncomming(int targetid)
	{
		printf("onCallInComming - %d", targetid);

		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("target", targetid);
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(), ResponseAct.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(intent);
	}

	@Override
	public void onCallSuccess(String ip, int port, String key, int link_dir , int udp_socket)
	{
		printf("onCallSuccess - %s - %d", ip, port);
		try
		{
			if (mPhoneCallback != null)
				mPhoneCallback.onCallSuccess(ip, port, key, link_dir,udp_socket);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onCallFailed(int state)
	{
		printf("onCallFailed - %d", state);
		try
		{
			if (mPhoneCallback != null)
				mPhoneCallback.onCallFailed(state);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onMemberAcquired(final int accountid, final String id, final String title, final int callstatus, final byte[] icon)
	{
		try
		{
			if (mMemberCallback != null)
				mMemberCallback.onMemberAcquired(accountid, id, title, callstatus, icon == null ? new byte[0] : icon);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onMemberChanged(int account_id, int changed_bits)
	{
		try
		{
			if (mMemberCallback != null)
				mMemberCallback.onMemberChanged(account_id, changed_bits);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onMemberOnlineStateChanged(int account_id, int online_offline)
	{
		try
		{
			if (mMemberCallback != null)
				mMemberCallback.onMemberOnlineStateChanged(account_id, online_offline);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	private final class ServManager extends IServManager.Stub
	{
		@Override
		public void rzLogin(String addr, int account, String passwd) throws RemoteException
		{
			mAccountInfor = new AccountInfor(addr, account, passwd);
			mLoginManager.request_login(mAccountInfor);
		}

		@Override
		public int rzGetLoginState() throws RemoteException
		{
			return mLoginManager.getLoginState().ordinal();
		}

		@Override
		public void rzLogout() throws RemoteException
		{
			mLoginManager.setLoginState(LoginState.OFFLINE);
			SVConnect.logout();
		}

		@Override
		public void rzSetIEventCallback(IEventCallback callback) throws RemoteException
		{
			mEventCallback = callback;
		}

		@Override
		public void rzSetILoginCallback(ILoginCallback callback) throws RemoteException
		{
			mLoginManager.setManual(true);
			mLoginCallback = callback;
		}

		@Override
		public void rzSetIPhoneStateCallback(IPhoneStateCallback callback) throws RemoteException
		{
			mPhoneCallback = callback;
		}

		@Override
		public void rzSetIMemberChangedCallback(IMemberChangedCallback callback) throws RemoteException
		{
			mMemberCallback = callback;
		}

		@Override
		public void rzGetMemberList() throws RemoteException
		{
			SVConnect.getMemberList();
		}

		@Override
		public void rzGetMemberInfo(int account, int update_bits) throws RemoteException
		{
			SVConnect.getMemberInfo(account, update_bits);
		}

		@Override
		public void rzSetInfor(int set_bits, String id, String title, int call_status, byte[] icon_buf) throws RemoteException
		{
			SVConnect.setInfomation(set_bits, id, title, call_status, icon_buf);
		}

		@Override
		public void rzCall(int targetid) throws RemoteException
		{
			SVConnect.call(targetid);
		}

		@Override
		public void rzAnswer() throws RemoteException
		{
			SVConnect.answer();
		}

		@Override
		public void rzReject() throws RemoteException
		{
			SVConnect.reject();
		}
	}

}

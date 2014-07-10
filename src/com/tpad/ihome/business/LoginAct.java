package com.tpad.ihome.business;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tpad.ihome.R;
import com.tpad.ihome.entity.LoginResult;
import com.tpad.ihome.serv.ILoginCallback;
import com.tpad.ihome.serv.IServManager;

public class LoginAct extends RemoteActivity implements OnClickListener
{
	// public final static String LOGIN_SERVER_ADDR = "221.228.229.117";
	// public final static String LOGIN_SERVER_ADDR = "unios.vicp.cc";
	// public final static String LOGIN_SERVER_ADDR = "49.212.166.110";
	public final static String LOGIN_SERVER_ADDR = "115.28.246.43";

	private Button btn_login;

	private EditText//
			edit_account, // for input account
			edit_passwd; // for input password

	private IServManager mRemoteServ;
	private ILoginCallback mLoginCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);

		btn_login = (Button) findViewById(R.id.btn_login_login);
		edit_account = (EditText) findViewById(R.id.edit_login_account);
		edit_passwd = (EditText) findViewById(R.id.edit_login_passwd);

		btn_login.setOnClickListener(this);

		mLoginCallback = new LoginCallback();
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service)
	{
		super.onServiceConnected(name, service);
		mRemoteServ = getRemoteService();
		try
		{
			mRemoteServ.rzSetILoginCallback(mLoginCallback);
		} catch (RemoteException e)
		{
			processRemoteException(e);
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.btn_login_login)
		{
			final int account = Integer.parseInt(edit_account.getText().toString());

			final String passwd = edit_passwd.getText().toString();

			login(account, passwd);
		}
	}

	private void login(int account, String passwd)
	{
		try
		{
			if (mRemoteServ != null)
			{
				mRemoteServ.rzLogin(LOGIN_SERVER_ADDR, account, passwd);
			}
		} catch (RemoteException e)
		{
			processRemoteException(e);
		}
	}

	final class LoginCallback extends ILoginCallback.Stub
	{
		@Override
		public void onLoginSuccess() throws RemoteException
		{
			printf("onloginSuccess !!!");
			goto_act(new Bundle(), RequestAct.class);

			finish();
		}

		@Override
		public void onLoginFailed(int state) throws RemoteException
		{

			final LoginResult event = LoginResult.values()[state];

			printf("onLoginFailed - %s!!!", event);

			post_ui(new Runnable()
			{
				public void run()
				{
					Toast.makeText(LoginAct.this, String.format("Login Failed  - %s!!!", event.getDesc()), Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}

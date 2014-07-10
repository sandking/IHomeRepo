package com.tpad.ihome.business;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.tpad.ihome.serv.IHomeService;
import com.tpad.ihome.serv.IServManager;

public class RemoteActivity extends BaseActivity implements ServiceConnection
{
	private IServManager remoteServ;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), IHomeService.class);

		Bundle bundle = new Bundle();

		final String smpName = getClass().getSimpleName();

		bundle.putString("act-name", smpName);

		intent.putExtras(bundle);

		bindService(intent, this, BIND_AUTO_CREATE);
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		printf(">>>>>>>>> onPause <<<<<<<<<");
		unbindService(this);
	}

	public IServManager getRemoteService()
	{
		return remoteServ;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service)
	{
		printf("！！！>>>>>>>>> onServiceConnected <<<<<<<<<");

		remoteServ = IServManager.Stub.asInterface(service);
	}

	@Override
	public void onServiceDisconnected(ComponentName name)
	{
		printf("！！！>>>>>>>>> onServiceDisconnected <<<<<<<<<");

		remoteServ = null;
	}

	protected void goto_communication(int type, String ip, int port, String key, int link_dir, int udp_socket)
	{
		printf("goto_communication - %s - %d", ip, port);
 
		Bundle bundle = new Bundle();

		bundle.putInt(BUNDLE_COMM_TYPE, type);
		bundle.putString(BUNDLE_ADDR_IP, ip);
		bundle.putInt(BUNDLE_ADDR_PORT, port);
		bundle.putString(BUNDLE_SERV_KEY, key);
		bundle.putInt(BUNDLE_LINK_DIR, link_dir);
		bundle.putInt(BUNDLE_UDP_SOCKET, udp_socket);
 
		goto_act(bundle, CommunityAct.class);
	}

	protected void processRemoteException(RemoteException e)
	{
		e.printStackTrace();
		post_ui(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(RemoteActivity.this, "Remote Communication Exception !!", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public final static String BUNDLE_COMM_TYPE = "comm-type"; // call in / out.
	public final static int TYPE_CALL_IN = 0x10;
	public final static int TYPE_CALL_OUT = 0x20;

	public final static String BUNDLE_ADDR_IP = "addr-ip";
	public final static String BUNDLE_ADDR_PORT = "addr-port";
	public final static String BUNDLE_SERV_KEY = "serv-key";

	public final static String BUNDLE_LINK_DIR = "link-dir";
	public final static String BUNDLE_UDP_SOCKET = "udp-socket";

	public final static int _RAZEM_LINK_DIR_NULL = 0;
	public final static int _RAZEM_LINK_DIR_IN = 1;
	public final static int _RAZEM_LINK_DIR_OUT = 2;
	public final static int _RAZEM_LINK_DIR_OUT_SERV = 3;
}

/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/sk/java-space/eclipse-workspace/IHome/src/com/tpad/ihome/serv/IPhoneStateCallback.aidl
 */
package com.tpad.ihome.serv;
public interface IPhoneStateCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.tpad.ihome.serv.IPhoneStateCallback
{
private static final java.lang.String DESCRIPTOR = "com.tpad.ihome.serv.IPhoneStateCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.tpad.ihome.serv.IPhoneStateCallback interface,
 * generating a proxy if needed.
 */
public static com.tpad.ihome.serv.IPhoneStateCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.tpad.ihome.serv.IPhoneStateCallback))) {
return ((com.tpad.ihome.serv.IPhoneStateCallback)iin);
}
return new com.tpad.ihome.serv.IPhoneStateCallback.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onCallSuccess:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.lang.String _arg2;
_arg2 = data.readString();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
this.onCallSuccess(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
case TRANSACTION_onCallFailed:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onCallFailed(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.tpad.ihome.serv.IPhoneStateCallback
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void onCallSuccess(java.lang.String ip, int port, java.lang.String key, int link_dir, int udp_socket) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(ip);
_data.writeInt(port);
_data.writeString(key);
_data.writeInt(link_dir);
_data.writeInt(udp_socket);
mRemote.transact(Stub.TRANSACTION_onCallSuccess, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onCallFailed(int state) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(state);
mRemote.transact(Stub.TRANSACTION_onCallFailed, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onCallSuccess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onCallFailed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void onCallSuccess(java.lang.String ip, int port, java.lang.String key, int link_dir, int udp_socket) throws android.os.RemoteException;
public void onCallFailed(int state) throws android.os.RemoteException;
}

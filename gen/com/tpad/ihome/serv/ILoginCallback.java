/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/sk/java-space/eclipse-workspace/IHome/src/com/tpad/ihome/serv/ILoginCallback.aidl
 */
package com.tpad.ihome.serv;
public interface ILoginCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.tpad.ihome.serv.ILoginCallback
{
private static final java.lang.String DESCRIPTOR = "com.tpad.ihome.serv.ILoginCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.tpad.ihome.serv.ILoginCallback interface,
 * generating a proxy if needed.
 */
public static com.tpad.ihome.serv.ILoginCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.tpad.ihome.serv.ILoginCallback))) {
return ((com.tpad.ihome.serv.ILoginCallback)iin);
}
return new com.tpad.ihome.serv.ILoginCallback.Stub.Proxy(obj);
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
case TRANSACTION_onLoginSuccess:
{
data.enforceInterface(DESCRIPTOR);
this.onLoginSuccess();
reply.writeNoException();
return true;
}
case TRANSACTION_onLoginFailed:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onLoginFailed(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.tpad.ihome.serv.ILoginCallback
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
@Override public void onLoginSuccess() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onLoginSuccess, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onLoginFailed(int state) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(state);
mRemote.transact(Stub.TRANSACTION_onLoginFailed, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onLoginSuccess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onLoginFailed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void onLoginSuccess() throws android.os.RemoteException;
public void onLoginFailed(int state) throws android.os.RemoteException;
}

/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/sk/java-space/eclipse-workspace/IHome/src/com/tpad/ihome/serv/IMemberChangedCallback.aidl
 */
package com.tpad.ihome.serv;
public interface IMemberChangedCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.tpad.ihome.serv.IMemberChangedCallback
{
private static final java.lang.String DESCRIPTOR = "com.tpad.ihome.serv.IMemberChangedCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.tpad.ihome.serv.IMemberChangedCallback interface,
 * generating a proxy if needed.
 */
public static com.tpad.ihome.serv.IMemberChangedCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.tpad.ihome.serv.IMemberChangedCallback))) {
return ((com.tpad.ihome.serv.IMemberChangedCallback)iin);
}
return new com.tpad.ihome.serv.IMemberChangedCallback.Stub.Proxy(obj);
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
case TRANSACTION_onMemberAcquired:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
int _arg3;
_arg3 = data.readInt();
byte[] _arg4;
_arg4 = data.createByteArray();
this.onMemberAcquired(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
reply.writeByteArray(_arg4);
return true;
}
case TRANSACTION_onMemberOnlineStateChanged:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.onMemberOnlineStateChanged(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_onMemberChanged:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.onMemberChanged(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.tpad.ihome.serv.IMemberChangedCallback
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
@Override public void onMemberAcquired(int accountid, java.lang.String id, java.lang.String title, int callstatus, byte[] icon) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(accountid);
_data.writeString(id);
_data.writeString(title);
_data.writeInt(callstatus);
_data.writeByteArray(icon);
mRemote.transact(Stub.TRANSACTION_onMemberAcquired, _data, _reply, 0);
_reply.readException();
_reply.readByteArray(icon);
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onMemberOnlineStateChanged(int account_id, int online_offline) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(account_id);
_data.writeInt(online_offline);
mRemote.transact(Stub.TRANSACTION_onMemberOnlineStateChanged, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onMemberChanged(int account_id, int changed_bits) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(account_id);
_data.writeInt(changed_bits);
mRemote.transact(Stub.TRANSACTION_onMemberChanged, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onMemberAcquired = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onMemberOnlineStateChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onMemberChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void onMemberAcquired(int accountid, java.lang.String id, java.lang.String title, int callstatus, byte[] icon) throws android.os.RemoteException;
public void onMemberOnlineStateChanged(int account_id, int online_offline) throws android.os.RemoteException;
public void onMemberChanged(int account_id, int changed_bits) throws android.os.RemoteException;
}

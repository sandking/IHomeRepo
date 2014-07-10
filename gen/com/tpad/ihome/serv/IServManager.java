/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/sk/java-space/eclipse-workspace/IHome/src/com/tpad/ihome/serv/IServManager.aidl
 */
package com.tpad.ihome.serv;
public interface IServManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.tpad.ihome.serv.IServManager
{
private static final java.lang.String DESCRIPTOR = "com.tpad.ihome.serv.IServManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.tpad.ihome.serv.IServManager interface,
 * generating a proxy if needed.
 */
public static com.tpad.ihome.serv.IServManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.tpad.ihome.serv.IServManager))) {
return ((com.tpad.ihome.serv.IServManager)iin);
}
return new com.tpad.ihome.serv.IServManager.Stub.Proxy(obj);
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
case TRANSACTION_rzLogin:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.lang.String _arg2;
_arg2 = data.readString();
this.rzLogin(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_rzGetLoginState:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.rzGetLoginState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_rzLogout:
{
data.enforceInterface(DESCRIPTOR);
this.rzLogout();
reply.writeNoException();
return true;
}
case TRANSACTION_rzSetILoginCallback:
{
data.enforceInterface(DESCRIPTOR);
com.tpad.ihome.serv.ILoginCallback _arg0;
_arg0 = com.tpad.ihome.serv.ILoginCallback.Stub.asInterface(data.readStrongBinder());
this.rzSetILoginCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_rzSetIPhoneStateCallback:
{
data.enforceInterface(DESCRIPTOR);
com.tpad.ihome.serv.IPhoneStateCallback _arg0;
_arg0 = com.tpad.ihome.serv.IPhoneStateCallback.Stub.asInterface(data.readStrongBinder());
this.rzSetIPhoneStateCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_rzSetIMemberChangedCallback:
{
data.enforceInterface(DESCRIPTOR);
com.tpad.ihome.serv.IMemberChangedCallback _arg0;
_arg0 = com.tpad.ihome.serv.IMemberChangedCallback.Stub.asInterface(data.readStrongBinder());
this.rzSetIMemberChangedCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_rzSetIEventCallback:
{
data.enforceInterface(DESCRIPTOR);
com.tpad.ihome.serv.IEventCallback _arg0;
_arg0 = com.tpad.ihome.serv.IEventCallback.Stub.asInterface(data.readStrongBinder());
this.rzSetIEventCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_rzSetInfor:
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
this.rzSetInfor(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
reply.writeByteArray(_arg4);
return true;
}
case TRANSACTION_rzGetMemberList:
{
data.enforceInterface(DESCRIPTOR);
this.rzGetMemberList();
reply.writeNoException();
return true;
}
case TRANSACTION_rzGetMemberInfo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.rzGetMemberInfo(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_rzCall:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.rzCall(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_rzAnswer:
{
data.enforceInterface(DESCRIPTOR);
this.rzAnswer();
reply.writeNoException();
return true;
}
case TRANSACTION_rzReject:
{
data.enforceInterface(DESCRIPTOR);
this.rzReject();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.tpad.ihome.serv.IServManager
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
@Override public void rzLogin(java.lang.String addr, int account, java.lang.String passwd) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(addr);
_data.writeInt(account);
_data.writeString(passwd);
mRemote.transact(Stub.TRANSACTION_rzLogin, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int rzGetLoginState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_rzGetLoginState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void rzLogout() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_rzLogout, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rzSetILoginCallback(com.tpad.ihome.serv.ILoginCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_rzSetILoginCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rzSetIPhoneStateCallback(com.tpad.ihome.serv.IPhoneStateCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_rzSetIPhoneStateCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rzSetIMemberChangedCallback(com.tpad.ihome.serv.IMemberChangedCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_rzSetIMemberChangedCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rzSetIEventCallback(com.tpad.ihome.serv.IEventCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_rzSetIEventCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rzSetInfor(int set_bits, java.lang.String id, java.lang.String title, int call_status, byte[] icon_buf) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(set_bits);
_data.writeString(id);
_data.writeString(title);
_data.writeInt(call_status);
_data.writeByteArray(icon_buf);
mRemote.transact(Stub.TRANSACTION_rzSetInfor, _data, _reply, 0);
_reply.readException();
_reply.readByteArray(icon_buf);
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rzGetMemberList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_rzGetMemberList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rzGetMemberInfo(int account, int update_bits) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(account);
_data.writeInt(update_bits);
mRemote.transact(Stub.TRANSACTION_rzGetMemberInfo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rzCall(int targetid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(targetid);
mRemote.transact(Stub.TRANSACTION_rzCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rzAnswer() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_rzAnswer, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rzReject() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_rzReject, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_rzLogin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_rzGetLoginState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_rzLogout = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_rzSetILoginCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_rzSetIPhoneStateCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_rzSetIMemberChangedCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_rzSetIEventCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_rzSetInfor = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_rzGetMemberList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_rzGetMemberInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_rzCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_rzAnswer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_rzReject = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
}
public void rzLogin(java.lang.String addr, int account, java.lang.String passwd) throws android.os.RemoteException;
public int rzGetLoginState() throws android.os.RemoteException;
public void rzLogout() throws android.os.RemoteException;
public void rzSetILoginCallback(com.tpad.ihome.serv.ILoginCallback callback) throws android.os.RemoteException;
public void rzSetIPhoneStateCallback(com.tpad.ihome.serv.IPhoneStateCallback callback) throws android.os.RemoteException;
public void rzSetIMemberChangedCallback(com.tpad.ihome.serv.IMemberChangedCallback callback) throws android.os.RemoteException;
public void rzSetIEventCallback(com.tpad.ihome.serv.IEventCallback callback) throws android.os.RemoteException;
public void rzSetInfor(int set_bits, java.lang.String id, java.lang.String title, int call_status, byte[] icon_buf) throws android.os.RemoteException;
public void rzGetMemberList() throws android.os.RemoteException;
public void rzGetMemberInfo(int account, int update_bits) throws android.os.RemoteException;
public void rzCall(int targetid) throws android.os.RemoteException;
public void rzAnswer() throws android.os.RemoteException;
public void rzReject() throws android.os.RemoteException;
}

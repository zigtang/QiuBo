package zig.qiubo.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class ContactUtil {
	// TODO 现在此Util中维系3个游标不关闭，不知道可行不可行~
	// TODO 每次由于一个item 开启一两个游标 用完立马关闭感觉相当不划算
	private Context context;
	private static ContactUtil contactUtil;

	private ContactUtil(Context context) {
		this.context = context;
	}

	public static ContactUtil getInstance(Context context) {
		if (contactUtil == null) {
			contactUtil = new ContactUtil(context);
		}
		return contactUtil;
	}

	/**
	 * 查询RawContact表
	 * 
	 * @return
	 */
	public Cursor getAllContact() {
		String sortOrder = RawContacts.DISPLAY_NAME_PRIMARY
				+ " COLLATE LOCALIZED ASC";
		Cursor cursor = context.getContentResolver().query(
				RawContacts.CONTENT_URI, null, null, null, sortOrder);
		return cursor;
	}

	public String getNameByNumber(String number) {
		String name = null;
		long rawContactId = getRawContactIdByNumber(number);
		if (rawContactId != -1) {
			name = getNameByRawContactID(rawContactId);
		}
		return name;
	}

	public long getRawContactIdByNumber(String number) {
		Cursor cursor = context.getContentResolver().query(
				Data.CONTENT_URI,
				new String[] { Data.RAW_CONTACT_ID }, 
				Data.DATA1 + " =?",
				new String[] { number }, null);
		long rawContactId = -1;
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				rawContactId = cursor.getLong(cursor
						.getColumnIndex(Data.RAW_CONTACT_ID));
			}
			cursor.close();
		}
		return rawContactId;
	}

	public String getNameByRawContactID(long rawContactId) {
		Cursor cursor = context.getContentResolver().query(
				Data.CONTENT_URI,
				new String[] { Data.DATA1 },
				Data.RAW_CONTACT_ID + " =? and " + Data.MIMETYPE + " = ?",
				new String[] { "" + rawContactId,
						"vnd.android.cursor.item/name" }, null);
		String name = null;
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				name = cursor.getString(cursor.getColumnIndex(Data.DATA1));
			}
			cursor.close();
		}
		return name;
	}

	/**
	 * 根据rawContactId获取联系人的电话号码
	 * 
	 * @param rawContactId
	 * @return
	 */
	public String getNumberByRawContactId(long rawContactId) {
		// long rawContactId =
		// cursor.getLong(cursor.getColumnIndex(RawContacts._ID));
		Cursor phoneCursor = context.getContentResolver().query(
				Data.CONTENT_URI,
				new String[] { Data._ID, Data.DATA1 },
				Data.RAW_CONTACT_ID + " =? and " + Data.MIMETYPE + " =?",
				new String[] { "" + rawContactId,
						"vnd.android.cursor.item/phone_v2" }, null);
		String phone = "";

		if (phoneCursor != null) {
			if (phoneCursor.getCount() != 0) {
				phoneCursor.moveToFirst();
				phone = phoneCursor.getString(phoneCursor
						.getColumnIndex(Data.DATA1));
			}
			phoneCursor.close();
		}
		return phone;
	}

	/**
	 * 根据contactID获取联系人图像
	 * 
	 * @param contactId
	 * @return 返回联系人图像的位图，如果没有则返回null
	 */
	public Bitmap getPhotoByContactId(long contactId) {
		// long contactId =
		// cursor.getLong(cursor.getColumnIndex(RawContacts.CONTACT_ID));
		InputStream photoIs = openPhoto(contactId);
		Bitmap contactPhoto = null;
		if (photoIs != null) {
			contactPhoto = BitmapFactory.decodeStream(photoIs);
		}
		return contactPhoto;
	}

	/**
	 * 根据联系人ID得到联系人图像文件的输入流~
	 * 
	 * @param contactId
	 * @return
	 */
	private InputStream openPhoto(long contactId) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
				contactId);
		Uri photoUri = Uri.withAppendedPath(contactUri,
				Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { Contacts.Photo.PHOTO }, null, null, null);

		if (cursor == null) {
			return null;
		}

		try {
			if (cursor.moveToFirst()) {
				byte[] data = cursor.getBlob(0);
				if (data != null) {
					return new ByteArrayInputStream(data);
				}
			}
		} finally {
			cursor.close();
		}
		return null;
	}
}

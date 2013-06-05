package com.example.locationbased;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatData {

	public static final String TAG = "ChatData";
	public static final String DB_NAME = "alldata.db";
	public static final int DB_VERSION = 2;
	public static final String TABLE_NAME = "chatdata";
	public static final String EMAIL_ID = "email_id";
	public static final String FIRST_NAME = "first_name";
	public static final String MESSAGE_TEXT = "message_text";
	public static final String TIME_OF_MESSAGE = "time_of_message";
	public static final String DIRECTION="direction";

	Context context;
	DBHelper dbHelper;
	SQLiteDatabase db;

	public ChatData(Context context) {
		this.context = context;
		dbHelper = new DBHelper();
	}

	public void insert(String emailId, String firstName, String messageText,
			long timeOfMessage,String direction) {
		db = dbHelper.getWritableDatabase();
Log.d(TAG, "On Insert");
		Date date = new Date();
		date.setTime((long) timeOfMessage * 1000);

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Log.d(TAG, "DateTime is "+dateFormat.format(date));

		ContentValues values = new ContentValues();
		values.put(EMAIL_ID, emailId);
		values.put(FIRST_NAME, firstName);
		values.put(MESSAGE_TEXT, messageText);
		values.put(TIME_OF_MESSAGE, dateFormat.format(date));
		values.put(DIRECTION, direction);
		db.insertWithOnConflict(TABLE_NAME, null, values,
				SQLiteDatabase.CONFLICT_IGNORE);
		Log.d(TAG, "Have finished inserting data");
return;
	}
	
	
	public Cursor getMessagesOfUser(String emailId)
	{
		db = dbHelper.getWritableDatabase();
		String sql="Select "+MESSAGE_TEXT+", "+TIME_OF_MESSAGE+", "+DIRECTION+" from "+TABLE_NAME+" where "+EMAIL_ID+"= ?";
		String selectionArgs[]={emailId};
	    Cursor messagesCursor=db.rawQuery(sql, selectionArgs);
Log.d(TAG, "Number of rows in cursor is "+messagesCursor.getCount());
	return messagesCursor;
	}

	public Cursor getUsers() {
		db = dbHelper.getWritableDatabase();

		String sql = "Select DISTINCT " + EMAIL_ID +" , "+FIRST_NAME+" from " + TABLE_NAME;
				
		Log.d(TAG, "getUsers before query");
		Cursor usersCursor = db.rawQuery(sql, null);
		Log.d(TAG, "getUsers after query");
		return usersCursor;

	}

	class DBHelper extends SQLiteOpenHelper {

		public DBHelper() {
			super(context, DB_NAME, null, DB_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			String sql = "Create TABLE " + TABLE_NAME + "(" + EMAIL_ID + " text, "
					+ FIRST_NAME + " text ," + MESSAGE_TEXT + " text ,"
					+ TIME_OF_MESSAGE + " DATETIME ,"+DIRECTION+" text)";
			Log.d(TAG, "Sql is "+sql);
			db.execSQL(sql);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "OnUpgrade");
			db.execSQL("Drop TABLE if exists " + TABLE_NAME);
			onCreate(db);

		}

	}

}

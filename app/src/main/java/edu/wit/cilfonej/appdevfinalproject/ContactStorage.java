package edu.wit.cilfonej.appdevfinalproject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 	Database Utility class used to store extra info not part of a standard Android Contact.
 * 	Also used to keep a record/query all contact added via the app
 */
public class ContactStorage {
	private static final String LOG_NAME = "ContactApp - Database";
	private static final String DB_FILE_NAME = "contacts.db";

	public static final String CREATED_ON = "created_timestamp";

	public static final String TWITTER_EXT = "twitter_ext";
	public static final String FACEBOOK_EXT = "facebook_ext";
	public static final String LINKED_IN_EXT = "linkedin_ext";
	public static final String YOUTUBE_EXT = "youtube_ext";

	private static final String DB_CREATE_SQL =
			"CREATE TABLE IF NOT EXISTS contacts ("
				+ "contact_id INT NOT NULL, "
				+ CREATED_ON + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
				+ TWITTER_EXT + " VARCHAR(50), "
				+ FACEBOOK_EXT + " VARCHAR(50), "
				+ LINKED_IN_EXT + " VARCHAR(68), "
				+ YOUTUBE_EXT + " VARCHAR(25), "
			+ "PRIMARY KEY (contact_id));";

	private static SQLiteDatabase DB;

// ============================================================================================================================= \\
// ================================================ Database Functions ========================================================= \\

	public static void initDatabase(Context context) {
		if(DB != null) {
			Log.w(LOG_NAME, "Database is already Connected!");
			return;
		}

		String path = "data/data/" + context.getPackageName() + "/" + DB_FILE_NAME;
		DB = SQLiteDatabase.openOrCreateDatabase(path, null);
		DB.execSQL(DB_CREATE_SQL);
	}

	public static boolean isConnected() {
		return !(DB == null || !DB.isOpen());
	}

	private static void validateConnection() {
		if(!isConnected())
			throw new IllegalStateException("Database is not accessible! Call 'initDatabase(Context)' first");
	}

	public static List<Integer> listContactIds() {
		Cursor query = DB.query("contacts", new String[] { "contact_id" }, null, null, null, null, null);
		List<Integer> values = new ArrayList<>();

		if(query != null && query.getCount() > 0) {
			while(query.moveToNext()) {
				for (String column : query.getColumnNames()) {
					values.add(query.getInt(query.getColumnIndex(column)));
				}
			}

			query.close();
		}

		return values;
	}

	public static Map<String, Object> queryContact(int contact_id) {
		Cursor query = DB.query("contacts", null, "contact_id = ?", new String[] { String.valueOf(contact_id) }, null, null, null);
		Map<String, Object> values = new HashMap<>();

		if(query != null && query.getCount() > 0 && query.moveToNext()) {
			for (String column : query.getColumnNames()) {

				// use spacial parsing for certain columns
				if(column.equals(CREATED_ON)) {
					values.put(column, query.getInt(query.getColumnIndex(column)));

				// default to String if not a special case
				} else {
					values.put(column, query.getString(query.getColumnIndex(column)));
				}
			}

			query.close();
		}

		return values;
	}

	public static void addLastContact(Context context, Contact contact) {
		ContentResolver cr = context.getContentResolver();
		Cursor query = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[] { ContactsContract.Contacts._ID }, null, null, null);

		if(query != null && query.getCount() > 0 && query.moveToLast()) {
			DB.execSQL("INSERT INTO contacts (contact_id) VALUES(" + query.getInt(query.getColumnIndex(ContactsContract.Contacts._ID)) + ")");
		}
	}

	public static void removeContact(int contact_id) {
		DB.execSQL("DELETE FROM contacts WHERE contact_id = " + contact_id);
	}
}

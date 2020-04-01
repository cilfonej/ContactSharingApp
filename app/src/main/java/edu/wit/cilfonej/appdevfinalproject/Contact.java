package edu.wit.cilfonej.appdevfinalproject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.Address;
import ezvcard.property.Email;
import ezvcard.property.Title;

public class Contact {
	private static final String QUERY_BY_CONTACT_ID = ContactsContract.Contacts._ID + " = ?";

	// represents the associated contact in the phones contact-system
	private int contact_id;
	private VCard card;

	private LocalDateTime created_on;

	private String twitter_ext;
	private String facebook_ext;
	private String linkedin_ext;
	private String youtube_ext;

	public Contact(VCard card) {
		this.card = card;
	}

	public Contact(Context context, int contact_id) {
		this.contact_id = contact_id;

		try {
			this.card = loadAndroidVCard(context, contact_id);

			// query extra data not stored in common Android contact
			Map<String, Object> extraValue = ContactStorage.queryContact(contact_id);

			this.twitter_ext = (String) extraValue.get(ContactStorage.TWITTER_EXT);
			this.facebook_ext = (String) extraValue.get(ContactStorage.FACEBOOK_EXT);
			this.linkedin_ext = (String) extraValue.get(ContactStorage.LINKED_IN_EXT);
			this.youtube_ext = (String) extraValue.get(ContactStorage.YOUTUBE_EXT);

			this.created_on = null;
		} catch (NotFoundException e) {
			ContactStorage.removeContact(contact_id);
		}
	}

	private static VCard loadAndroidVCard(Context context, int contact_id) {
		ContentResolver resolver = context.getContentResolver();
		// query contact's LOOKUP_KEY by contact_id
		Cursor query = resolver.query(
				ContactsContract.Contacts.CONTENT_URI,
				new String[] { ContactsContract.Contacts.LOOKUP_KEY },

				QUERY_BY_CONTACT_ID,
				new String[] { String.valueOf(contact_id) }, null);


		Exception cause = null;

		// if a contact is found
		if(query != null && query.getCount() > 0 && query.moveToNext()) {
			// extract the LOOKUP_KEY
			String lookupKey = query.getString(query.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
			// build a URI to the VCard file for the Contact
			Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);

			try {
				// attempt to open the VCard File
				AssetFileDescriptor fd = resolver.openAssetFileDescriptor(uri, "r");
				// try to parse the VCard file into a "list" of VCards
				List<VCard> cards = Ezvcard.parse(fd.createInputStream()).all();

				// if there is at-least one VCard, return it
				if(cards != null && !cards.isEmpty())
					return cards.get(0);

			} catch (IOException e) {
				cause = e;

			} finally {
				query.close();
			}
		}

		throw new NotFoundException("Could not find a Contact with specified ID", cause);
	}

	public String getEmail() {
		List<Email> emails = card.getEmails();
		return emails.size() < 1 ? "<no email>" : emails.get(0).getValue();
	}

	public String getAddress() {
		List<Address> addresses = card.getAddresses();
		return addresses.size() < 1 ? "<no address>" : addresses.get(0).getStreetAddress() ;
	}

	public String getFirstName() { return card.getStructuredName() == null ? "" : card.getStructuredName().getGiven(); }
	public String getLastName() { return card.getStructuredName() == null ? "" : card.getStructuredName().getFamily(); }

	public boolean isValid() { return card != null; }

	public static List<Contact> getAllContacts(Context context) {
		// if the database hasn't been opened, do so now...
		if(!ContactStorage.isConnected()) ContactStorage.initDatabase(context);

		// get list of all contact in the app's storage
		List<Integer> ids = ContactStorage.listContactIds();
		List<Contact> contacts = new ArrayList<>(ids.size());

		for(Integer id : ids) {
			contacts.add(new Contact(context, id));
		}

		return contacts;
	}
}

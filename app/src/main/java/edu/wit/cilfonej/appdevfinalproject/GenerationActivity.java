package edu.wit.cilfonej.appdevfinalproject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.android.ContactOperations;
import ezvcard.property.Address;
import ezvcard.property.Impp;
import ezvcard.property.Member;
import ezvcard.property.Note;
import ezvcard.property.StructuredName;

public class GenerationActivity extends AppCompatActivity {

	private DecoratedBarcodeView barcodeView;
	private BeepManager beepManager;
	private String lastText;

	private RecyclerView contact_disp;

	private BarcodeCallback callback = new BarcodeCallback() {
		@Override
		public void barcodeResult(BarcodeResult result) {
			if(result.getText() == null || result.getText().equals(lastText)) {
				// Prevent duplicate scans
				return;
			}

			lastText = result.getText();

			try {
				barcodeView.setStatusText("Checking Code...");
				VCard card = Ezvcard.parse(lastText).first();
				if(card == null) throw new IllegalArgumentException();

				beepManager.playBeepSoundAndVibrate();
				barcodeView.setStatusText("Contact Found!");

				barcodeView.pause();

				barcodeView.setVisibility(View.GONE);
				contact_disp.setVisibility(View.VISIBLE);

				Contact contact = new Contact(card);

				ContactAdapter ca = new ContactAdapter(Arrays.asList(contact));
				contact_disp.setAdapter(ca);
				contact_disp.invalidate();

				// add contact
				try {
					ContactOperations operations = new ContactOperations(getApplicationContext());
					operations.insertContact(card);

					ContactStorage.addLastContact(GenerationActivity.this, contact);

				} catch (RemoteException | OperationApplicationException e) {
					e.printStackTrace();
				}

			} catch(Exception e) {
				Log.i("ContactScan", "Not a valid VCard");
				barcodeView.setStatusText("INVALID!");
			}
		}

		@Override
		public void possibleResultPoints(List<ResultPoint> resultPoints) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generation);

		barcodeView = findViewById(R.id.barcode_scanner);
		Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE);
		barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
		barcodeView.initializeFromIntent(getIntent());
		barcodeView.decodeContinuous(callback);

		beepManager = new BeepManager(this);

		contact_disp = findViewById(R.id.contact_disp);
		LinearLayoutManager wow = new LinearLayoutManager(this);
		wow.setOrientation(LinearLayoutManager.VERTICAL);
		contact_disp.setLayoutManager(wow);

		SharedPreferences preferences = getSharedPreferences("edu.wit.cilfonej.appdevfinalproject", Context.MODE_PRIVATE);
		VCard vcard = new VCard();

		StructuredName n = new StructuredName();
		n.setGiven(preferences.getString("my_firstname", ""));
		n.setFamily(preferences.getString("my_lastname", ""));
		vcard.setStructuredName(n);

		vcard.addEmail(preferences.getString("my_email", ""));

		Address address = new Address();
		address.setStreetAddress(preferences.getString("my_address", ""));
		vcard.addAddress(address);

		vcard.addTelephoneNumber(preferences.getString("my_phone", ""));

		String str = Ezvcard.write(vcard).version(VCardVersion.V4_0).go();

		try {
			BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
			Bitmap bitmap = barcodeEncoder.encodeBitmap(str, BarcodeFormat.QR_CODE, 800, 800);

			ImageView qrImg = (ImageView) findViewById(R.id.qr_view);
			qrImg.setImageBitmap(bitmap);

		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		barcodeView.resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		barcodeView.pause();
	}
}

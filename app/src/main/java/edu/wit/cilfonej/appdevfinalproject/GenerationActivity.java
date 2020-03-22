package edu.wit.cilfonej.appdevfinalproject;

import android.content.OperationApplicationException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Base64;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.android.ContactOperations;
import ezvcard.property.Impp;
import ezvcard.property.Member;
import ezvcard.property.Note;
import ezvcard.property.StructuredName;

public class GenerationActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generation);

		VCard vcard = new VCard();

		StructuredName n = new StructuredName();
		n.setFamily("Doe");
		n.setGiven("Jonathan");
		n.getPrefixes().add("Mr");
		vcard.setStructuredName(n);

		vcard.setFormattedName("John Doe");

		vcard.addNote("Twitter: https://twitter.com/nasa");
		vcard.addNote("Twitter: https://twitter.com/nasa");
		vcard.addNote("Twitter: https://twitter.com/nasa");

		String str = Ezvcard.write(vcard).version(VCardVersion.V4_0).go();

		try {
			ContactOperations operations = new ContactOperations(getApplicationContext());
			operations.insertContact(vcard);
		} catch (RemoteException | OperationApplicationException e) {
			e.printStackTrace();
		}

		try {
			BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
			Bitmap bitmap = barcodeEncoder.encodeBitmap(str, BarcodeFormat.QR_CODE, 800, 800);

			ImageView qrImg = (ImageView) findViewById(R.id.qr_view);
			qrImg.setImageBitmap(bitmap);

		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
}

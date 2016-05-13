package com.axelfriberg.bikebuddy;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

public class NFCActivity extends AppCompatActivity {
    NFCManager nfcMger;
    String id = "My bike";
    NdefMessage message;
    boolean writeable = false;
    TextView tv;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_nfc);
        setTitle(R.string.nfc_fragment_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nfcMger = new NFCManager(this);
        Log.v("NFC", "onCreate");
        message = createTextMessage(id);
        tv = (TextView) findViewById(R.id.scan_text);
        image = (ImageView) findViewById(R.id.scan_image);
        tv.setText("Hold device against tag to scan");
    }

        @Override
        protected void onResume () {
            super.onResume();
            Log.v("NFC", "onResume");
            try {
                nfcMger.verifyNFC();
                Intent nfcIntent = new Intent(this, getClass());
                nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
                IntentFilter[] intentFiltersArray = new IntentFilter[]{};
                String[][] techList = new String[][]{
                        {android.nfc.tech.Ndef.class.getName()},
                        {android.nfc.tech.NdefFormatable.class.getName()}
                };
                NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
                nfcAdpt.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);

            } catch (IOException nfcnsup) {
                Toast.makeText(getApplicationContext(), "NFC Not supported/enabled", Toast.LENGTH_SHORT).show();
            }

        }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        Tag nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (nfcTag == null) {
            Log.v("hjhj", "Unable to obtain NFC tag from intent!");
            tv.setText("This is not your bike!");
            image.setImageResource(R.drawable.cross);
        } else {
            if(writeable) {
                Toast.makeText(getApplicationContext(), "writing", Toast.LENGTH_SHORT).show();
                writeTag(nfcTag, message);
            }
            readFromTag(getIntent(), nfcTag);
        }
    }

    public void writeTag(Tag tag, NdefMessage message) {
        if (tag != null) {
            Log.v("NFC", "4");
            try {
                Ndef ndefTag = Ndef.get(tag);
                if (ndefTag == null)  {
                    Log.v("NFC", "1");
// Let's try to format the Tag in NDEF
                    NdefFormatable nForm = NdefFormatable.get(tag);
                    if (nForm != null) {
                        Log.v("NFC", "2");
                        nForm.connect();
                        nForm.format(message);
                        nForm.close();
                    }
                }
                else {
                    Log.v("NFC", "3");
                    ndefTag.connect();
                    ndefTag.writeNdefMessage(message);
                    ndefTag.close();
                    Toast.makeText(getApplicationContext(), "Write succeeded", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        else if (tag == null){ Log.v("NFC", "5");
        }}

    public NdefMessage createTextMessage(String content) {
        try {

            // Get UTF-8 byte
            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] text = content.getBytes("UTF-8"); // Content in UTF-8

            int langSize = lang.length;
            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                    NdefRecord.RTD_TEXT, new byte[0],
                    payload.toByteArray());
            return new NdefMessage(new NdefRecord[]{record});
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void readFromTag(Intent intent, Tag tag){

        Ndef ndef = Ndef.get(tag);


        try{
            ndef.connect();


            Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (messages != null) {
                NdefMessage[] ndefMessages = new NdefMessage[messages.length];
                for (int i = 0; i < messages.length; i++) {

                    ndefMessages[i] = (NdefMessage) messages[i];

                }
                NdefRecord record = ndefMessages[0].getRecords()[0];

                byte[] payload = record.getPayload();
                String text = new String(payload);
                ndef.close();

                text = text.substring(3);

                if (text.equals(id)){
                    tv.setText("This is your bike!");
                    image.setImageResource(R.drawable.check);
                }else{
                    tv.setText("This is not your bike!");
                    image.setImageResource(R.drawable.cross);
                }

            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot Read From Tag.", Toast.LENGTH_LONG).show();

        }

    }


    public class NFCManager {

        private Activity activity;
        private NfcAdapter nfcAdpt;

        public NFCManager(Activity activity) {
            this.activity = activity;
        }

        public void verifyNFC() throws IOException{

            nfcAdpt = NfcAdapter.getDefaultAdapter(activity);

            if (nfcAdpt == null)
                throw new IOException();

            if (!nfcAdpt.isEnabled())
                throw new IOException();

        }
    }


}

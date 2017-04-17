package vibhor.prakhar.example.com.nfc_tagger.Service;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vibhor.prakhar.example.com.nfc_tagger.Model.Wallet;

/**
 * Created by Pewds on 17-Apr-17.
 */

public class NFCManager1 {
    private Activity activity;
    private NfcAdapter nfcAdapter;
    private Intent intent;
    WriteCardDialog writeCardDialog;

    public NFCManager1(Activity activity) {
        this.activity = activity;
    }

    public void verifyNFC(){

        nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        if (nfcAdapter != null) {
            if(nfcAdapter.isEnabled()) {
                Toast.makeText(activity, "NFC Available!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(activity, "NFC is off!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(activity,"NFC Not Available!",Toast.LENGTH_SHORT).show();
        }

    }

    public void enableForegroundDispath(){
        Log.e("NFC","foreground enable");
        intent = new Intent(activity,getClass()).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, intentFilters, null);
    }

    public void disableForegroundDispath(){
        Log.e("NFC","foreground disable");
        nfcAdapter.disableForegroundDispatch(activity);

    }

    public void formatTag(Tag tag, NdefMessage ndefMessage){

        try {

            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null){
                Toast.makeText(activity,"Tag is not NdefFormatable!",Toast.LENGTH_SHORT).show();
                Log.e("NFC","Tag is not NdefFormatable!");
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
            Toast.makeText(activity,"Message Written1",Toast.LENGTH_SHORT).show();
            Log.e("NFC","Message Written1");
            disableForegroundDispath();
//            writeCardDialog.dismiss();
            activity.finish();

        }catch (Exception e){
            Log.e("formatTag", e.getMessage());
        }

    }

    public void writeNdefMessage(Tag tag, NdefMessage ndefMessage){

        try {

            if (tag == null){
                Toast.makeText(activity,"Tag object cannot be null",Toast.LENGTH_SHORT).show();
                Log.e("NFC","Tag object cannot be null");
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if (ndef == null){
                //format tag with the ndef format and writes the message
                formatTag(tag, ndefMessage);
            }else {
                ndef.connect();

                if (!ndef.isWritable()){
                    Toast.makeText(activity,"Tag is not writable!",Toast.LENGTH_SHORT).show();
                    Log.e("NFC","Tag is not writable!");
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Toast.makeText(activity,"Message Written2",Toast.LENGTH_SHORT).show();
                Log.e("NFC","Message Written2");
//                closeActivity();
            }

        }catch (Exception e){
            Log.e("writeNdefMessage", e.getMessage());
        }

    }

    public NdefRecord createTextRecord(String content) {
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

            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
            return record;
        }
        catch (Exception e) {
            Log.e("createTextRecord", e.getMessage());
        }

        return null;
    }

    public NdefMessage createNdefMessage(String content, List<Wallet> walletList){

        NdefRecord ndefRecord1 = createTextRecord(content);
        NdefRecord ndefRecord2;
        List<NdefRecord> ndefList = new ArrayList<NdefRecord>();
        ndefList.add(ndefRecord1);

        if(walletList.size()!=0) {
            for (int i = 0; i < walletList.size(); i++) {

                String url = walletList.get(i).getWallet_key().toString();
                if(url==""){
                    url="NA";
                }
                Uri uri = Uri.parse(url);
                ndefRecord1 = createTextRecord(walletList.get(i).getWallet_name().toString());
                ndefRecord2 = NdefRecord.createUri(uri);
                ndefList.add(ndefRecord1);
                ndefList.add(ndefRecord2);
            }
        }
        NdefRecord[] ndefRecordsArray = ndefList.toArray(new NdefRecord[ndefList.size()]);
//        Log.e("ndefArray",ndefRecordsArray.length + "");
        NdefMessage ndefMessage = new NdefMessage(ndefRecordsArray);
//        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        return ndefMessage;
    }

    public void closeActivity(){
        disableForegroundDispath();
//        writeCardDialog.dismiss();
        activity.finish();
    }
}

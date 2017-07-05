package vibhor.prakhar.example.com.nfc_tagger.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import vibhor.prakhar.example.com.nfc_tagger.Activity.MainActivity;
import vibhor.prakhar.example.com.nfc_tagger.Model.Card;
import vibhor.prakhar.example.com.nfc_tagger.Model.Wallet;

/**
 * Created by Prakhar Gupta on 25/11/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "BitAccount";

    // Table Names
    private static final String TABLE_CARD = "card";
    private static final String TABLE_WALLET = "wallet";

    // Common column names
    private static final String KEY_ID = "id";

    // CARD Table - column names
    private static final String KEY_CARD_NAME = "card_name";

    private static final String KEY_CARD_ID = "card_id";
    // WALLET Table - column names
    private static final String KEY_WALLET_TYPE = "wallet_type";
    private static final String KEY_WALLET_NAME = "wallet_name";
    private static final String KEY_WALLET_KEY = "wallet_key";

    // Table Create Statements
    // Card table create statement
    private static final String CREATE_TABLE_CARD = "CREATE TABLE "
            + TABLE_CARD + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CARD_NAME
            + " TEXT" + ")";

    // Wallet table create statement
    private static final String CREATE_TABLE_WALLET = "CREATE TABLE "
            + TABLE_WALLET + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CARD_ID + " INTEGER," + KEY_WALLET_TYPE + " TEXT," + KEY_WALLET_NAME+ " TEXT," + KEY_WALLET_KEY+ " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CARD);
        sqLiteDatabase.execSQL(CREATE_TABLE_WALLET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLET);

        // create new tables
        onCreate(sqLiteDatabase);
    }

    /**
     * Creating a Card
     */
    public long createCard(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CARD_NAME, card.getCardName());

        // insert row
        long card_id = db.insert(TABLE_CARD, null, values);
        closeDB();

        return card_id;
    }

    /**
     * getting all cards
     * */
    public List<Card> getAllCards() {
        List<Card> cardList = new ArrayList<Card>();
        String selectQuery = "SELECT  * FROM " + TABLE_CARD;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Card cd = new Card();
                cd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                cd.setCardName((c.getString(c.getColumnIndex(KEY_CARD_NAME))));

                // adding to todo list
                cardList.add(cd);
            } while (c.moveToNext());
        }

        c.close();
        closeDB();

        return cardList;
    }

    /**
     * getting cards count
     */
    public int getCardsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CARD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        closeDB();

        // return count
        return count;
    }

    /**
     * Deleting a card
     */
    public void deleteCard(long card_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WALLET, KEY_CARD_ID + " = ?",
                new String[] { String.valueOf(card_id) });
        db.delete(TABLE_CARD, KEY_ID + " = ?",
                new String[] { String.valueOf(card_id) });
        closeDB();
    }

    /**
     * Updating a card
     */
    public void updateCard(long card_id, String cardName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_CARD_NAME, cardName);
        db.update(TABLE_CARD, cv, KEY_ID+"="+card_id, null);
        closeDB();
    }




    /**
     * Creating wallet
     */
    public long createWallet(long card_id, Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CARD_ID, card_id);
        values.put(KEY_WALLET_TYPE, wallet.getWallet_type());
        values.put(KEY_WALLET_NAME, wallet.getWallet_name());
        values.put(KEY_WALLET_KEY, wallet.getWallet_key());

        long id = db.insert(TABLE_WALLET, null, values);
        closeDB();

        return id;
    }

    public List<Wallet> getWallets(long card_id) {
        List<Wallet> walletList = new ArrayList<Wallet>();
        String selectQuery = "SELECT  * FROM " + TABLE_WALLET + " WHERE "
                + KEY_CARD_ID + " = " + card_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Wallet wallet = new Wallet();
                wallet.setWallet_type((c.getString(c.getColumnIndex(KEY_WALLET_TYPE))));
                wallet.setWallet_name((c.getString(c.getColumnIndex(KEY_WALLET_NAME))));
                wallet.setWallet_key((c.getString(c.getColumnIndex(KEY_WALLET_KEY))));
                wallet.setId(Long.parseLong((c.getString(c.getColumnIndex(KEY_ID)))));

                // adding to todo list
                walletList.add(wallet);
            } while (c.moveToNext());
        }

        c.close();
        closeDB();

        return walletList;
    }

    /**
     * Deleting a wallet
     */
    public void deleteWallet(long wallet_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WALLET, KEY_ID + " = ?",
                new String[] { String.valueOf(wallet_id) });
        closeDB();
    }

    /**
     * Updating a wallet
     */
    public void updateWallet(long wallet_id, String walletType, String walletName, String walletKey) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_WALLET_TYPE, walletType);
        cv.put(KEY_WALLET_NAME, walletName);
        cv.put(KEY_WALLET_KEY, walletKey);
        db.update(TABLE_WALLET, cv, KEY_ID+"="+wallet_id, null);
        closeDB();
    }

    public String createBackup(){
        String result = "";

        //creating directory
        File data = Environment.getDataDirectory();
        File appFolder = new File(Environment.getExternalStorageDirectory() + "/Qrypto");
        boolean success = true;
        if (!appFolder.exists()) {
            success = appFolder.mkdir();
        }

        if (success) {

            Log.e(LOG,"directory created");

            try {
                SQLiteDatabase db = this.getWritableDatabase();
                String dbPath = "//data//" + MainActivity.PACKAGE_NAME + "//databases//" + DATABASE_NAME;
//                String dbPath = db.getPath();
                Log.e("DBpath", dbPath);
                File currentDB = new File(data, dbPath);
                File backupDB = new File(appFolder.getPath()+"/"+DATABASE_NAME);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                closeDB();
                result = backupDB.getPath();
                Log.e(LOG, "BACKUP CREATED!");

            }catch (Exception e){
                Log.e(LOG, e.toString());
                Log.e(LOG, "BACKUP FAILED!");
            }

        } else {
            Log.e(LOG,"could not create directory");
        }
        return result;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}

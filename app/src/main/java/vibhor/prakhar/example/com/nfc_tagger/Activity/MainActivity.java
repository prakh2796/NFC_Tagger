package vibhor.prakhar.example.com.nfc_tagger.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import vibhor.prakhar.example.com.nfc_tagger.Service.CircleTransform;
import vibhor.prakhar.example.com.nfc_tagger.Fragment.MyCards;
import vibhor.prakhar.example.com.nfc_tagger.LoginActivity;
import vibhor.prakhar.example.com.nfc_tagger.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";

    private static final String MY_PREFS_NAME = "USER_CREDENTIALS";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String name, email, profile_pic;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private View navHeader;
    private Intent intent;
    private Fragment objFragment;
    private Class fragmentClass;

    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                intent = new Intent(MainActivity.this, AddOrRemoveWallet.class);
                startActivityForResult(intent, 0);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
//        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        loadNavHeader();
        navigationView.setNavigationItemSelectedListener(this);

        objFragment=null;
        fragmentClass = MyCards.class;
        if (savedInstanceState == null) {
            fragmentClass = MyCards.class;
            loadFragment(fragmentClass);
        }


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            Toast.makeText(this,"NFC Available!",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"NFC Not Available!",Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNavHeader() {
        // name, website
//        Profile profile = Profile.getCurrentProfile();
//        txtName.setText(profile.getName());

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        name = sharedPreferences.getString("name", "Username");
        email = sharedPreferences.getString("email", "ecommerce@ecomerce.com");
        profile_pic = sharedPreferences.getString("profile_pic", "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg");

        txtName.setText(name);
        txtWebsite.setText(email);

        // loading header background image
//        Glide.with(this).load(urlNavHeaderBg)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(profile_pic)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            if(isFBLoggedIn()) {
                Log.e("prakhar","logout");
                LoginManager.getInstance().logOut();
            } else if(isTwitterLoggedIn()){
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeSessionCookie();
                Twitter.getSessionManager().clearActiveSession();
                Twitter.logOut();
            }
            intent = new Intent(MainActivity.this, LoginActivity.class);
            editor.putString("name", "\0");
            editor.putString("email", "\0");
            editor.putString("profile_pic", "\0");
            editor.commit();
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mycard) {
            fragmentClass = MyCards.class;
        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_nfc_action) {

        } else if (id == R.id.nav_buy_qrypto) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_email) {
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
            intent.putExtra(Intent.EXTRA_TEXT   , "body of email");
            try {
                startActivity(Intent.createChooser(intent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

        }

        loadFragment(fragmentClass);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean isFBLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken == null;
    }

    public boolean isTwitterLoggedIn() {
        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        return twitterSession != null;
    }

    public void loadFragment(Class fragmentClass){
        try {
            objFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        android.app.FragmentManager fragmentManager= getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, objFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        fragmentClass = MyCards.class;
        loadFragment(fragmentClass);

    }
}

package vibhor.prakhar.example.com.nfc_tagger.Model;

/**
 * Created by Prakhar Gupta on 25/11/2016.
 */

public class Wallet {
    String wallet_name;
    String wallet_key;

    public Wallet() {
    }

    public Wallet(String wallet_name, String wallet_key) {
        this.wallet_name = wallet_name;
        this.wallet_key = wallet_key;
    }

    public String getWallet_name() {
        return wallet_name;
    }

    public void setWallet_name(String wallet_name) {
        this.wallet_name = wallet_name;
    }

    public String getWallet_key() {
        return wallet_key;
    }

    public void setWallet_key(String wallet_key) {
        this.wallet_key = wallet_key;
    }
}

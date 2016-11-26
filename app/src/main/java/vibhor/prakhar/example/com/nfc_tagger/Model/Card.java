package vibhor.prakhar.example.com.nfc_tagger.Model;

/**
 * Created by Prakhar Gupta on 25/11/2016.
 */

public class Card {
    long id;
    String cardName;

    public Card() {
    }

    public Card(String cardName) {
        this.cardName = cardName;
    }

    public Card(long id, String cardName) {
        this.id = id;
        this.cardName = cardName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
}

package vibhor.prakhar.example.com.nfc_tagger.Model;

/**
 * Created by Prakhar Gupta on 23/11/2016.
 */

public class MyCardsItem {
    private String title,content;

    public MyCardsItem() {
    }

    public MyCardsItem(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

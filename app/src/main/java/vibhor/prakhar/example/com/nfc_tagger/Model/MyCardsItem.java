package vibhor.prakhar.example.com.nfc_tagger.Model;

/**
 * Created by Prakhar Gupta on 23/11/2016.
 */

public class MyCardsItem {
    private String type,title,content;

    public MyCardsItem() {
    }

    public MyCardsItem(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public MyCardsItem(String type, String title, String content) {
        this.type = type;
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

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }
}

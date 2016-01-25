package sampa.com.imagesearch;

public class Image {
    public String thumbnail;
    public String link;
    public int height;
    public int width;

    public Image(String thumbnail, String link, int height, int width) {
        this.thumbnail = thumbnail;
        this.link = link;
        this.height = height;
        this.width = width;
    }
}

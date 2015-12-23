package sampa.com.imagesearch;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchResponse {
    public boolean success;
    public int total_items;
    public ArrayList<Image> images;

    public class Image {
        public String url;

        @Override
        public String toString() {
            return "Image{ link='" + url + "\'}";
        }
    }

    @Override public String toString() {
        return "ImageResponse{" +
                "success=" + success +
                ", total_items=" + String.valueOf(total_items) +
                ", images=[" + printAllImages() +
                '}';
    }

    private String printAllImages() {
        String output = "";
        if(images.size() > 1) {
            output += images.get(0);
            images.remove(0);
        }
        for(Image image : images) {
            output += ",\n" + image.toString();
        }
        return output;
    }
}
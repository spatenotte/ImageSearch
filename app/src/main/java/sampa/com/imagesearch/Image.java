package sampa.com.imagesearch;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class Image {
    Bitmap bitmap;

    public Image(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

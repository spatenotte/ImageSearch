package sampa.com.imagesearch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<String> {
    public ImageAdapter(Context context, ArrayList<String> images) {
        super(context, 0, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String image = getItem(position);
        Log.d("ImageAdapter", image);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        Picasso.with(getContext())
                .load(image)
                .into(imageView);
        return convertView;
    }
}
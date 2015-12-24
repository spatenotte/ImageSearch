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

public class ImageAdapter extends ArrayAdapter<Image> {

    LayoutInflater inflater = LayoutInflater.from(getContext());

    public ImageAdapter(Context context, ArrayList<Image> images) {
        super(context, 0, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;

        // Get the data item for this position
        Image image = getItem(position);
        Log.d("ImageAdapter", image + " position " + position);
        // Check if an existing viewholder is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_image, parent, false);
            viewHolder = new ViewHolderItem();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        Picasso.with(getContext())
            .load(image.link)
            .fit().centerInside()
            .into(viewHolder.imageView);

        return convertView;
    }

    static class ViewHolderItem {
        ImageView imageView;
    }
}
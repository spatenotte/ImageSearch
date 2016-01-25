package sampa.com.imagesearch;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import sampa.com.imagesearch.models.Item;
import sampa.com.imagesearch.models.TagImages;

public class MainActivity extends AppCompatActivity {

    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create adapter for images
        ArrayList<Image> imageDisplay = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imageDisplay);
        ListView listView = (ListView) findViewById(R.id.list_view_main);
        listView.setAdapter(imageAdapter);

        Button button = (Button) findViewById(R.id.button_search);
        final AutoCompleteTextView input = (AutoCompleteTextView) findViewById(R.id.auto_complete_text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input.getText().toString().trim().equals("")) {
                    input.setError("Search cannot be empty");
                }
                else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    executeRequest(input.getText().toString());
                }
            }
        });
    }

    private void executeRequest(String tag) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.imgur.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImgurApi apiService = retrofit.create(ImgurApi.class);

        Call<TagImages> call = apiService.getImages(Constants.getClientAuth(), tag);
        call.enqueue(new Callback<TagImages>() {
            @Override
            public void onResponse(Response<TagImages> response) {
                int statusCode = response.code();
                TagImages images = response.body();
                if(statusCode != 200) {
                    Snackbar.make(findViewById(R.id.layout_main), "Request failed", Snackbar.LENGTH_LONG).show();
                }
                else {
                    saveImages(images);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Failure", t.toString());
                Snackbar.make(findViewById(R.id.layout_main), "No response from server", Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void saveImages(TagImages images) {
        for(Item image : images.getData().getItems()) {
            if(image.getIsAlbum() == false && image.getType().equals("image/jpeg")) {
                String link = image.getLink();
                imageAdapter.add(new Image(
                    link.substring(0, link.length() - 4) + "l" + link.substring(link.length() - 4),
                    link,
                    image.getHeight(),
                    image.getWidth()
                ));
            }
        }
    }
}
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
                    executeRequest(input.getText().toString(), null);
                }
            }
        });
    }

    private boolean executeRequest(String tag, Callback<SearchResponse> callback) {
        final Callback<SearchResponse> cb = callback;

        RestAdapter restAdapter = buildRestAdapter();
        restAdapter.create(ImgurApi.class).getImages(
            Constants.getClientAuth(),
            tag,
            new Callback<SearchResponse>() {
                @Override
                public void success(SearchResponse searchResponse, Response response) {
                    if (cb != null) cb.success(searchResponse, response);
                    if (response == null) {
                        Log.d("ImageSearch", "Null response");
                        Snackbar.make(findViewById(R.id.layout_main), "No response from server", Snackbar.LENGTH_LONG).show();
                    }
                    try {
                        JSONObject object = parseJSON(response);
                        Log.d("ImageSearch, length", String.valueOf(object.getJSONObject("data").getInt("total_items")));
                        saveImages(object.getJSONObject("data"));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("ImageSearch", "Request failure");
                    Snackbar.make(findViewById(R.id.layout_main), "Network error", Snackbar.LENGTH_LONG).show();
                }
            }
        );
        return cb == null;
    }

    private JSONObject parseJSON(Response response) throws IOException, JSONException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(response.getBody().in()));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        return new JSONObject(responseStrBuilder.toString());
    }

    private RestAdapter buildRestAdapter() {
        return new RestAdapter.Builder()
                .setEndpoint(ImgurApi.server)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    private void saveImages(JSONObject object) throws JSONException {
        Log.d("saveImages", "Entered");
        JSONArray images = (JSONArray) object.get("items");
        int length = images.length();
        JSONObject current;
        for(int i=0; i < length; i++) {
            current = images.getJSONObject(i);
            if (current.has("type") && current.getString("type").equals("image/jpeg")) {
                Log.d("saveImages", "Image" + i + ": " + current.getString("link"));
                imageAdapter.add(new Image(
                    current.getString("link"),
                    current.getInt("height"),
                    current.getInt("width")));
            }
        }
    }
}
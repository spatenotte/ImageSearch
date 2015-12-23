package sampa.com.imagesearch;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

public interface ImgurApi {
    String server = "https://api.imgur.com";

    @GET("/3/gallery/t/{t_name}")
    void getImages(
            @Header("Authorization") String auth,
            @Path("t_name") String tag,
            Callback<SearchResponse> cb
    );
}

package sampa.com.imagesearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import sampa.com.imagesearch.models.TagImages;

public interface ImgurApi {
    @GET("3/gallery/t/{t_name}")
    Call<TagImages> getImages(
            @Header("Authorization") String auth,
            @Path("t_name") String tag
    );
}

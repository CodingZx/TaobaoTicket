package lol.cicco.tbunion.common.api;


import java.util.List;

import io.reactivex.Observable;
import lol.cicco.tbunion.common.entity.CategoryEntity;
import retrofit2.http.GET;

public interface HomeApi {

    @GET("discovery/categories")
    Observable<List<CategoryEntity>> getHomeCategory();

}

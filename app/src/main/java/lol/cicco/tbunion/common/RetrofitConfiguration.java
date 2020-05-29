package lol.cicco.tbunion.common;

import java.util.concurrent.TimeUnit;

import lol.cicco.tbunion.BuildConfig;
import lol.cicco.tbunion.common.util.LogUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitConfiguration {

    public static final Retrofit retrofit;

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConfiguration.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(it -> {
                    Request request = it.request();
                    LogUtils.info(RetrofitConfiguration.class, "HttpUrl -> " + request.url());
                    return it.proceed(request);
                }).build();
    }


}

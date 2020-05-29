package lol.cicco.tbunion.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import lol.cicco.tbunion.common.exception.HttpRequestException;
import lol.cicco.tbunion.common.result.ResponseModel;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class GsonConfiguration {

    private static final Gson gson = new GsonBuilder().create();

    public static GsonConverterFactory create() {
        return new GsonConverterFactory();
    }

    private static class GsonConverterFactory extends Converter.Factory {
        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            return new GsonRequestBodyConverter<>(adapter);
        }
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));

            return (Converter<ResponseBody, Object>) value -> {
                ResponseModel<JsonElement> result = gson.fromJson(value.string(), new TypeToken<ResponseModel<JsonElement>>(){}.getType());
                if(result.code != ResponseModel.OK_CODE) {
                    throw new HttpRequestException(result.code);
                }
                return adapter.fromJson(gson.toJson(result.data));
            };
        }
    }

    private static class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");
        private static final Charset UTF_8 = StandardCharsets.UTF_8;

        private TypeAdapter<T> adapter;

        GsonRequestBodyConverter(TypeAdapter<T> adapter) {
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();
            return RequestBody.create(
                    MEDIA_TYPE,
                    buffer.readByteString()
            );
        }
    }
}

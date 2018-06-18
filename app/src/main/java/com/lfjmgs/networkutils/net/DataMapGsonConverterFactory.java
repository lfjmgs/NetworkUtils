package com.lfjmgs.networkutils.net;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 提取data字段，抛出ApiException
 * Created by liufang03 on 2017/8/29.
 */

public class DataMapGsonConverterFactory extends Converter.Factory {

    public static DataMapGsonConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new DataMapGsonConverterFactory(gson);
    }

    private final Gson gson;

    private DataMapGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }
    
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(type);
        if (rawType != Result.class) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            return new DataMapGsonResponseBodyConverter<>(adapter, rawType != Response.class);
        }
        return super.responseBodyConverter(type, annotations, retrofit);
    }

    private static class DataMapGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

        private final TypeAdapter<T> adapter;

        private final boolean withData;

        DataMapGsonResponseBodyConverter(TypeAdapter<T> adapter, boolean withData) {
            this.adapter = adapter;
            this.withData = withData;
        }

        @Override
        public T convert(@NonNull ResponseBody value) throws IOException {
            try {
                String json = value.string();
                JSONObject jsonObject = new JSONObject(json);
                String code = jsonObject.getString(Response.KEY_CODE);
                if (Response.REQUEST_SUCCESS.equals(code)) {
                    if (withData) {
                        json = jsonObject.get(Response.KEY_DATA).toString();
                        if ("null".equalsIgnoreCase(json)) {
                            // rxjava2不能发射null会抛出NPE，这里抛出自定义异常和NPE区分
                            throw new DataIsNullException();
                        }
                    }
                    return adapter.fromJson(json);
                } else {
                    throw new ApiException(code, jsonObject.getString(Response.KEY_MESSAGE));
                }
            } catch (JSONException e) {
                throw new IOException(e);
            } finally {
                value.close();
            }
        }
    }
}

package com.jieniuwuliu.jieniu.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Createtime: 2018/7/5
 * Author: wanglei
 * Description: gson 工具类
 * getGson : 获取 Gson 对象
 * toJson  : 对象转 Json 串
 * fromJson: Json 串转对象
 */
public final class GsonUtil {

    private GsonUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final Gson GSON = createGson(true);
    private static final Gson GSON_NO_NULLS = createGson(false);
    private static String json = null;

    /**
     * Gets pre-configured {@link Gson} instance.
     *
     * @return {@link Gson} instance.
     */
    public static Gson getGson() {
        return getGson(true);
    }

    //json转对象
    public static <T> Object praseJsonToModel(String json, Class<T> clazz) {
        Object o = null;
        try {
            o = GSON.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    //json转数组
    public static <T> List<Object> praseJsonToList(String json, Class<T> clazz) {
        List<Object> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                Object o = GSON.fromJson(array.get(i).toString(), clazz);
                list.add(o);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //对象转json
    public static String objectToJson(Object o) {
        json = GSON.toJson(o);
        return json;
    }

    //数组转json
    public static String listToJson(List<?> list) {
        json = GSON.toJson(list);
        return json;
    }

    //map转json
    public static String mapToJson(Map<String, Object> map) {
        json = GSON.toJson(map);
        return json;
    }

    /**
     * Gets pre-configured {@link Gson} instance.
     *
     * @param serializeNulls determines if nulls will be serialized.
     * @return {@link Gson} instance.
     */
    public static Gson getGson(final boolean serializeNulls) {
        return serializeNulls ? GSON_NO_NULLS : GSON;
    }

    /**
     * Serializes an object into json.
     *
     * @param object the object to serialize.
     * @return object serialized into json.
     */
    public static String toJson(final Object object) {
        return toJson(object, true);
    }

    /**
     * Serializes an object into json.
     *
     * @param object       the object to serialize.
     * @param includeNulls determines if nulls will be included.
     * @return object serialized into json.
     */
    public static String toJson(final Object object, final boolean includeNulls) {
        return includeNulls ? GSON.toJson(object) : GSON_NO_NULLS.toJson(object);
    }


    /**
     * Converts {@link String} to given type.
     *
     * @param json the json to convert.
     * @param type type type json will be converted to.
     * @return instance of type
     */
    public static <T> T fromJson(final String json, final Class<T> type) {
        return GSON.fromJson(json, type);
    }

    /**
     * Converts {@link String} to given type.
     *
     * @param json the json to convert.
     * @param type type type json will be converted to.
     * @return instance of type
     */
    public static <T> T fromJson(final String json, final Type type) {
        return GSON.fromJson(json, type);
    }

    /**
     * Converts {@link Reader} to given type.
     *
     * @param reader the reader to convert.
     * @param type   type type json will be converted to.
     * @return instance of type
     */
    public static <T> T fromJson(final Reader reader, final Class<T> type) {
        return GSON.fromJson(reader, type);
    }

    /**
     * Converts {@link Reader} to given type.
     *
     * @param reader the reader to convert.
     * @param type   type type json will be converted to.
     * @return instance of type
     */
    public static <T> T fromJson(final Reader reader, final Type type) {
        return GSON.fromJson(reader, type);
    }

    /**
     * Create a pre-configured {@link Gson} instance.
     *
     * @param serializeNulls determines if nulls will be serialized.
     * @return {@link Gson} instance.
     */
    private static Gson createGson(final boolean serializeNulls) {
        final GsonBuilder builder = new GsonBuilder();
        if (serializeNulls) {
            builder.serializeNulls();
        }
        return builder.create();
    }

}

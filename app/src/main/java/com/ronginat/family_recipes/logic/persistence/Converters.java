package com.ronginat.family_recipes.logic.persistence;

import android.graphics.Color;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.ronginat.family_recipes.model.CategoryEntity;
import com.ronginat.family_recipes.layout.modelTO.CategoryTO;
import com.ronginat.family_recipes.utils.logic.CrashLogger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.room.TypeConverter;

/**
 * Created by ronginat on 01/01/2019.
 */
@SuppressWarnings("WeakerAccess")
public class Converters {

    private static Gson gson = new Gson();

    // region recipes

    @TypeConverter
    public static List<String> fromString(String value) {
        Type type = new TypeToken<List<String>>() { }
                .getType();
        try {
            return gson.fromJson(value, type);
        } catch (JsonParseException ex) {
            CrashLogger.logException(ex);
            return Collections.singletonList("error");
        }
    }

    /*@TypeConverter
    public List<RecipeEntity.Comment> fromStringToComments(String comments) {
        Type type = new TypeToken<List<RecipeEntity.Comment>>() {
        }.getType();
        return gson.fromJson(comments, type);
    }

    @TypeConverter
    public static String ListCommentToString(List<RecipeEntity.Comment> list) {
        return gson.toJson(list);
    }*/

    @TypeConverter
    public static String ListToString(List<String> list) {
        return gson.toJson(list);
    }

    // endregion


    // region categories

    @TypeConverter
    public static List<CategoryEntity> fromStringToCategories(String categories) {
        //CrashLogger.e(getClass().getSimpleName(), "set string categories, " + categories);
        Type type = new TypeToken<List<CategoryEntity>>() {}.getType();
        try {
            return gson.fromJson(categories, type);
        } catch (JsonParseException ex) {
            CrashLogger.logException(ex);
            List<CategoryEntity> errorList = new ArrayList<>();
            errorList.add(new CategoryEntity.Builder().name("error").color(Color.RED).build());
            return errorList;
        }
    }

    @TypeConverter
    public static String ListCategoriesToString(List<CategoryEntity> list) {
        return gson.toJson(list);
    }

    //@TypeConverter
    public static List<CategoryEntity> fromCategoryTOList(List<CategoryTO> list) {
        List<CategoryEntity> entities = new ArrayList<>();
        if (list != null) {
            for (CategoryTO to: list) {
                entities.add(to.toEntity());
            }
        }
        return entities;
    }

    // endregion

    // region pendingRecipes

    /*@TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }*/

    // endregion
}

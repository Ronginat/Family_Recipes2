package com.myapps.ron.family_recipes.recycler;

import android.graphics.Color;

import com.myapps.ron.family_recipes.model.CategoryEntity;
import com.myapps.ron.family_recipes.utils.Constants;

import java.util.List;

/**
 * Created by ronginat on 30/12/2018.
 */
public class RecipesAdapterHelper {

    public static int getCategoryColorByName(List<CategoryEntity> categoryList, String name) {
        if (categoryList != null) {
            for (CategoryEntity category : categoryList) {
                if (category.getName().equals(name))
                    return category.getIntColor();

                if (category.hasSubCategories()) {
                    int color = getSubCategoryColorByName(category.getCategories(), name);
                    if (color != -1)
                        return color;
                }
            }
        }
        return Color.parseColor(Constants.DEFAULT_COLOR);
    }

    private static int getSubCategoryColorByName(List<CategoryEntity> categoryList, String name) {
        int color = - 1;
        for (CategoryEntity category: categoryList) {
            if (category.getName().equals(name)) {
                color = category.getIntColor();
                break;
            } else {
               if (category.hasSubCategories()) {
                   color = getSubCategoryColorByName(category.getCategories(), name);
                   if (color != -1)
                       break;
               }
            }
        }
        return color;
    }
}

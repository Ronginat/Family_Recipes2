package com.myapps.ron.family_recipes.network;

import com.google.gson.JsonObject;
import com.myapps.ron.family_recipes.model.Recipe;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface RecipeService {

    String recipes = "/recipes";
    String categories = "/recipes/categories";
    String food = "/food";

    //region recipes
    @GET(recipes)
    Call<List<Recipe>> getAllRecipes(
            @HeaderMap Map<String, String> headers,
            @Query(Constants.DATE_QUERY) String query
    );

    @POST(recipes)
    Call<JsonObject> postPendRecipe(
            @HeaderMap Map<String, String> headers,
            @Body Map<String, String> body
    );

    @PATCH(recipes)
    Call<Recipe> patchRecipe(
            @HeaderMap Map<String, String> headers,
            @Body Map<String, String> body
    );

    //endregion

    //region categories
    @GET(categories)
    Call<JsonObject> getAllCategories(
            @HeaderMap Map<String, String> headers,
            @Query(Constants.DATE_QUERY) String query
    );

    //endregion

    //region food
    @PUT(food)
    Call<JsonObject> putFoodPictures(
            @HeaderMap Map<String, String> headers,
            @Body Map<String, String> body
    );

    //endregion


    /*@GET("/repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors(
            @Path("owner") String owner,
            @Path("repo") String repo);*/
}

package com.myapps.ron.family_recipes.network.modelTO;

import com.google.gson.annotations.SerializedName;
import com.myapps.ron.family_recipes.model.RecipeEntity;
import com.myapps.ron.family_recipes.utils.Constants;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;


/**
 * Created by ronginat on 31/12/2018.
 */
public class RecipeTO {

    //public static String image = "https://api.androidhive.info/json/images/keanu.jpg";

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("creationDate")
    private String creationDate;
    @SerializedName("lastModifiedDate")
    private String lastModifiedDate;
    @SerializedName("recipeFile")
    private String recipeFile;
    @SerializedName("uploader")
    private String uploader;
    @SerializedName("categories")
    private List<String> categories;
    @SerializedName("foodFiles")
    private List<String> foodFiles;
    @SerializedName("likes")
    private int likes;


    public RecipeTO() {
    }

    public RecipeTO (RecipeEntity recipe) {
        this();
        if (recipe != null) {
            this.id = recipe.getId();
            this.name = recipe.getName();
            this.description = recipe.getDescription();
            this.creationDate = recipe.getCreationDate();
            this.lastModifiedDate = recipe.getLastModifiedDate();
            this.recipeFile = recipe.getRecipeFile();
            this.uploader = recipe.getUploader();
            this.categories = recipe.getCategories();
            /*if (recipe.getComments() != null) {
                this.comments = new ArrayList<>();
                for (RecipeEntity.Comment comment: recipe.getComments()) {
                    this.comments.add(new Comment(comment));
                }
            }*/
            this.foodFiles = recipe.getFoodFiles();
            this.likes = recipe.getLikes();
        }

    }

    public RecipeEntity toEntity() {
        RecipeEntity rv = new RecipeEntity();
        rv.setId(this.id);
        rv.setName(this.name);
        rv.setDescription(this.description);
        rv.setCreationDate(this.creationDate);
        rv.setLastModifiedDate(this.lastModifiedDate);
        rv.setRecipeFile(this.recipeFile);
        rv.setUploader(this.uploader);
        rv.setCategories(this.categories);
        /*List<RecipeEntity.Comment> commentsRv = null;
        if (this.comments != null) {
            commentsRv = new ArrayList<>();
            for (Comment comment: this.comments) {
                commentsRv.add(comment.toEntity());
            }
        }
        rv.setComments(commentsRv);*/
        rv.setFoodFiles(this.foodFiles);
        rv.setLikes(this.likes);

        return rv;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RecipeTO) {
            return getId().equals(((RecipeTO)obj).getId());
        }
        return false;
        //return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, creationDate, lastModifiedDate, recipeFile, uploader, categories/*, comments*/, foodFiles, likes);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if(name != null)
            return name;
        return Constants.DEFAULT_RECIPE_NAME;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        if(description != null)
            return description;
        return Constants.DEFAULT_RECIPE_DESC;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationDate() {
        if(creationDate != null)
            return creationDate;
        return com.myapps.ron.family_recipes.network.Constants.DEFAULT_UPDATED_TIME;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getRecipeFile() {
        return recipeFile;
    }

    public void setRecipeFile(String recipeFile) {
        this.recipeFile = recipeFile;
    }

    public String getUploader() {
        if(uploader != null)
            return uploader;
        return Constants.DEFAULT_RECIPE_UPLOADER;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getFoodFiles() {
        return foodFiles;
    }

    public void setFoodFiles(List<String> foodFiles) {
        this.foodFiles = foodFiles;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }


    @NonNull
    @Override
    public String toString() {
        return "RecipeTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", lastModifiedDate='" + lastModifiedDate + '\'' +
                ", recipeFile='" + recipeFile + '\'' +
                ", uploader='" + uploader + '\'' +
                ", categories=" + categories +
                ", foodFiles=" + foodFiles +
                ", likes=" + likes +
                '}';
    }

}
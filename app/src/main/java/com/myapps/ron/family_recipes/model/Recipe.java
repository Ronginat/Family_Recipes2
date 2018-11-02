package com.myapps.ron.family_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.myapps.ron.family_recipes.utils.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Recipe implements Parcelable{
    private static final int FALSE = 0;
    private static final int TRUE = 1;
    public static String image = "https://api.androidhive.info/json/images/keanu.jpg";

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("lastModifiedAt")
    private String lastModifiedAt;
    @SerializedName("recipeFile")
    private String recipeFile;
    @SerializedName("uploader")
    private String uploader;
    @SerializedName("categories")
    private List<String> categories;
    @SerializedName("comments")
    private List<String> comments;
    @SerializedName("foodFiles")
    private List<String> foodFiles;
    @SerializedName("likes")
    private int likes;

    private boolean meLike;
/*    @SerializedName("sharedKey")
    private String sharedKey;*/

    /*private String id;
    private String name;
    private String description;
    private String createdAt;
    private String lastModifiedAt;
    private String recipeFile;
    private String uploader;
    private Set<String> categories;
    private List<String> comments;
    private List<String> foodFiles;
    private int likes;
    private String sharedKey;*/

    private static final Gson gson = new Gson();

    public Recipe() {
    }

    //from server
    public Recipe(String id, String name, String description, String createdAt,
                  String lastModifiedAt, String recipeFile, String uploader,
                  @Nullable List<String> categories, @Nullable List<String> comments, @Nullable List<String> foodFiles,
                  int likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.recipeFile = recipeFile;
        this.uploader = uploader;
        this.categories = categories;
        this.comments = comments;
        this.foodFiles = foodFiles;
        this.likes = likes;

        this.meLike = false;
    }

    //from db
    public Recipe(String id, String name, String description, String createdAt,
                  String lastModifiedAt, String recipeFile, String uploader,
                  String categories, String comments, String foodFiles,
                  int likes, boolean meLike) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.recipeFile = recipeFile;
        this.uploader = uploader;
        this.likes = likes;

        this.meLike = meLike;

        setStringCategories(categories);
        setStringComments(comments);
        setStringFoodFiles(foodFiles);
    }

    public Recipe(String id, String name, String description, String createdAt,
                  String lastModifiedAt, String recipeFile, String uploader,
                  String categories, String comments, String foodFiles,
                  int likes) {

        this(id, name, description, createdAt, lastModifiedAt, recipeFile, uploader, categories,
                comments, foodFiles, likes, false);
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    private Recipe(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.createdAt = in.readString();
        this.lastModifiedAt = in.readString();
        this.recipeFile = in.readString();
        this.uploader = in.readString();

        categories = new ArrayList<>();
        comments = new ArrayList<>();
        foodFiles = new ArrayList<>();

        in.readList(this.categories, String.class.getClassLoader());
        in.readList(this.comments, String.class.getClassLoader());
        in.readList(this.foodFiles, String.class.getClassLoader());

        this.likes = in.readInt();
        this.meLike = in.readInt() == TRUE;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Recipe) {
            return getId().equals(((Recipe)obj).getId());
        }
        return false;
        //return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, createdAt, lastModifiedAt, recipeFile, uploader, categories, comments, foodFiles, likes, meLike);
    }

    /**
     * check all attributes
     * @param other - other recipe
     * @return whether or not current and second recipes are exactly the same
     */
    public boolean identical(Recipe other) {
        boolean ids = getId() == null && other.getId() == null;
        if (id != null && other.getId() != null)
            ids = getId().equals(other.getId());

        boolean names = getName() == null && other.getName() == null;
        if (getName() != null && other.getName() != null)
            names = getName().equals(other.getName());

        boolean descriptions = description == null && other.getDescription() == null;
        if (description != null && other.getDescription() != null)
            descriptions = description.equals(other.description);

        boolean uploaders = getUploader() == null && other.getUploader() == null;
        if (getUploader() != null && other.getUploader() != null)
            uploaders = getUploader().equals(other.getUploader());

        boolean cats = categories == null && other.getCategories() == null;
        if (categories != null && other.getCategories() != null)
            cats = getStringCategories().equals(other.getStringCategories());

        boolean created = getCreatedAt() == null && other.getCreatedAt() == null;
        if (getCreatedAt() != null && other.getCreatedAt() != null)
            created = getCreatedAt().equals(other.getCreatedAt());

        boolean modified = getLastModifiedAt() == null && other.getLastModifiedAt() == null;
        if (getLastModifiedAt() != null && other.getLastModifiedAt() != null)
            modified = getLastModifiedAt().equals(other.getLastModifiedAt());

        boolean file = getRecipeFile() == null && other.getRecipeFile() == null;
        if (getRecipeFile() != null && other.getRecipeFile() != null)
            file = getRecipeFile().equals(other.getRecipeFile());

        boolean images = getFoodFiles() == null && other.getFoodFiles() == null;
        if (getFoodFiles() != null && other.getFoodFiles() != null)
            images = getStringFoodFiles().equals(other.getStringFoodFiles());

        boolean comments = getComments() == null && other.getComments() == null;
        if (getComments() != null && other.getComments() != null)
            comments = getStringComments().equals(other.getStringComments());

        boolean likes = getLikes() == other.getLikes();
        boolean meLikes = getMeLike() == other.getMeLike();

        return ids && names && descriptions && uploaders && cats && created && modified
                && file && images && comments && likes && meLikes;

        /*return getId().equals(other.getId()) && getName().equals(other.getName()) && getDescription().equals(other.getDescription())
                && getUploader().equals(other.getUploader()) && getCategories().equals(other.getCategories())
                && getCreatedAt().equals(other.getCreatedAt()) && getLastModifiedAt().equals(other.getLastModifiedAt())
                //&& getRecipeFile().equals(other.getRecipeFile()) && getComments().equals(other.getComments())
                && getFoodFiles().equals(other.getFoodFiles()) && getLikes() == other.getLikes()
                && getMeLike() == other.getMeLike();*/
    }

    public String getStringCategories() {
        return gson.toJson(getCategories());
    }

    public void setStringCategories(String categories) {
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> value = gson.fromJson(categories, type);
        setCategories(value);
    }

    public String getStringComments() {
        return gson.toJson(getComments());
    }

    public void setStringComments(String comments) {
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> value = gson.fromJson(comments, type);
        setComments(value);
    }

    public String getStringFoodFiles() {
        return gson.toJson(getFoodFiles());
    }

    public void setStringFoodFiles(String foodFiles) {
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> value = gson.fromJson(foodFiles, type);
        setFoodFiles(value);
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

    public String getCreatedAt() {
        if(createdAt != null)
            return createdAt;
        return com.myapps.ron.family_recipes.network.Constants.DEFAULT_UPDATED_TIME;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(String lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
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

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
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

    public boolean getMeLike() {
        return meLike;
    }

    public void setMeLike(boolean meLike) {
        this.meLike = meLike;
    }

    public boolean hasTags(List<String> tags) {
        if (tags != null && !tags.isEmpty())
            return categories.containsAll(tags);
        else
            return true;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "image='" + image + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", lastModifiedAt='" + lastModifiedAt + '\'' +
                ", recipeFile='" + recipeFile + '\'' +
                ", uploader='" + uploader + '\'' +
                ", categories=" + categories +
                ", comments=" + comments +
                ", foodFiles=" + foodFiles +
                ", likes=" + likes +
                ", meLike=" + meLike +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.createdAt);
        dest.writeString(this.lastModifiedAt);
        dest.writeString(this.recipeFile);
        dest.writeString(this.uploader);
        dest.writeList(this.categories);
        dest.writeList(this.comments);
        dest.writeList(this.foodFiles);
        dest.writeInt(this.likes);
        dest.writeInt(getMeLike() ? TRUE : FALSE);
    }
}

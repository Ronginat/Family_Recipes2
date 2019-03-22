package com.myapps.ron.family_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myapps.ron.family_recipes.utils.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static com.myapps.ron.family_recipes.utils.Constants.FALSE;
import static com.myapps.ron.family_recipes.utils.Constants.TRUE;

/**
 * Created by ronginat on 31/12/2018.
 */
//@Fts4
@Entity(tableName = "recipes"/*, indices = { @Index(value = {"name", "description"}), @Index("categories") }*/)
public class RecipeEntity implements Parcelable{

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CATEGORIES = "categories";
    public static final String KEY_CREATED = "creationDate";
    public static final String KEY_MODIFIED = "lastModifiedDate";
    public static final String KEY_UPLOADER = "uploader";
    public static final String KEY_FOOD_FILES = "foodFiles";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_FAVORITE = "meLike";

    @Ignore
    public static String image = "https://api.androidhive.info/json/images/keanu.jpg";

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = KEY_ID)
    private String id;
    @ColumnInfo(name = KEY_NAME)
    private String name;
    @ColumnInfo(name = KEY_DESCRIPTION)
    private String description;
    @ColumnInfo(name = KEY_CREATED)
    private Long creationDate;
    @ColumnInfo(name = KEY_MODIFIED)
    private Long lastModifiedDate;
    @ColumnInfo(name = "recipeFile")
    private String recipeFile;
    @ColumnInfo(name = KEY_UPLOADER)
    private String uploader;
    @ColumnInfo(name = KEY_CATEGORIES)
    private List<String> categories;
    @ColumnInfo(name = KEY_FOOD_FILES)
    private List<String> foodFiles;
    @ColumnInfo(name = KEY_LIKES)
    private int likes;
    @ColumnInfo(name = KEY_FAVORITE)
    private int meLike;


    private static final Gson gson = new Gson();

    public RecipeEntity() {
        super();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RecipeEntity createFromParcel(Parcel in) {
            return new RecipeEntity(in);
        }

        public RecipeEntity[] newArray(int size) {
            return new RecipeEntity[size];
        }
    };

    private RecipeEntity(Parcel in) {
        this.id = Objects.requireNonNull(in.readString());
        this.name = in.readString();
        this.description = in.readString();
        this.creationDate = in.readLong();
        this.lastModifiedDate = in.readLong();
        this.recipeFile = in.readString();
        this.uploader = in.readString();

        categories = new ArrayList<>();
        //comments = new ArrayList<>();
        foodFiles = new ArrayList<>();

        in.readList(this.categories, String.class.getClassLoader());
        //in.readList(this.comments, RecipeEntity.Comment.class.getClassLoader());
        in.readList(this.foodFiles, String.class.getClassLoader());

        this.likes = in.readInt();
        this.meLike = in.readInt();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RecipeEntity) {
            return getId().equals(((RecipeEntity)obj).getId());
        }
        return false;
        //return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, creationDate, lastModifiedDate, recipeFile, uploader, categories/*, comments*/, foodFiles, likes, meLike);
    }

    /**
     * check all attributes
     * @param other - other recipe
     * @return whether or not current and second recipes are exactly the same
     */
    public boolean identical(RecipeEntity other) {
        boolean ids = getId().equals(other.getId());

        boolean names = getName() == null && other.getName() == null;
        if (getName() != null && other.getName() != null)
            names = getName().equals(other.getName());

        boolean descriptions = description == null && other.getDescription() == null;
        if (description != null && other.getDescription() != null)
            descriptions = description.equals(other.description);

        boolean uploaders = getUploader() == null && other.getUploader() == null;
        if (getUploader() != null && other.getUploader() != null)
            uploaders = getUploader().equals(other.getUploader());

        boolean cats = getCategoriesToString().equals(other.getCategoriesToString());

        /*boolean cats = categories == null && other.getFilters() == null;
        if (categories != null && other.getFilters() != null)
            cats = getCategoriesToString().equals(other.getCategoriesToString());*/

        boolean created = getCreationDate() == other.getCreationDate();

        boolean modified = getLastModifiedDate() == other.getLastModifiedDate();

        boolean file = getRecipeFile() == null && other.getRecipeFile() == null;
        if (getRecipeFile() != null && other.getRecipeFile() != null)
            file = getRecipeFile().equals(other.getRecipeFile());

        boolean images = getFoodFilesToString().equals(other.getFoodFilesToString());

        /*boolean images = getFoodFiles() == null && other.getFoodFiles() == null;
        if (getFoodFiles() != null && other.getFoodFiles() != null)
            images = getFoodFilesToString().equals(other.getFoodFilesToString());*/

        //boolean comments = getCommentsToString().equals(other.getCommentsToString());

        boolean likes = getLikes() == other.getLikes();
        //boolean meLikes = getMeLike() == other.getMeLike();

        return ids && names && descriptions && uploaders && cats && created && modified
                && file && images /*&& comments*/ && likes/* && meLikes*/;

        /*return getId().equals(other.getId()) && getName().equals(other.getName()) && getDescription().equals(other.getDescription())
                && getUploader().equals(other.getUploader()) && getFilters().equals(other.getFilters())
                && getCreationDate().equals(other.getCreationDate()) && getLastModifiedDate().equals(other.getLastModifiedDate())
                //&& getRecipeFile().equals(other.getRecipeFile()) && getComments().equals(other.getComments())
                && getFoodFiles().equals(other.getFoodFiles()) && getLikes() == other.getLikes()
                && getMeLike() == other.getMeLike();*/
    }

    private String getCategoriesToString() {
        if (getCategories() != null)
            return gson.toJson(getCategories());
        return "";
    }

    private void setStringCategories(String categories) {
        if (categories == null) {
            setCategories(null);
        } else {
            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> value = gson.fromJson(categories, type);
            setCategories(value);
        }
    }

    private String getFoodFilesToString() {
        if (getFoodFiles() != null)
            return gson.toJson(getFoodFiles());
        return "";
    }

    private void setStringFoodFiles(String foodFiles) {
        if (foodFiles == null) {
            setFoodFiles(null);
        } else {
            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> value = gson.fromJson(foodFiles, type);
            setFoodFiles(value);
        }
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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

    public Long getCreationDate() {
        if(creationDate != null)
            return creationDate;
        return com.myapps.ron.family_recipes.network.Constants.DEFAULT_UPDATED_TIME;
    }

    public void setCreationDate(Long creationDate) {
        if (creationDate != null)
            this.creationDate = creationDate;
    }

    public Long getLastModifiedDate() {
        if (lastModifiedDate != null) {
            return lastModifiedDate;
        }
        return com.myapps.ron.family_recipes.network.Constants.DEFAULT_UPDATED_TIME;
    }

    public void setLastModifiedDate(Long lastModifiedDate) {
        if (lastModifiedDate != null)
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

    public int getMeLike() {
        return meLike;
    }

    public void setMeLike(int meLike) {
        this.meLike = meLike;
    }

    public boolean isUserLiked() {
        return this.meLike == TRUE;
    }

    public boolean hasTags(List<String> tags) {
        if (tags != null && !tags.isEmpty())
            return categories.containsAll(tags);
        else
            return true;
    }

    @NonNull
    @Override
    public String toString() {
        return "Recipe{" +
                //"image='" + image + '\'' +
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
        dest.writeLong(this.creationDate);
        dest.writeLong(this.lastModifiedDate);
        dest.writeString(this.recipeFile);
        dest.writeString(this.uploader);
        dest.writeList(this.categories);
        dest.writeList(this.foodFiles);
        dest.writeInt(this.likes);
        dest.writeInt(this.meLike);
    }


    public static class RecipeBuilder {
        private String builderId;
        private String builderName;
        private String builderDescription;
        private long builderCreatedAt;
        private long builderLastModifiedAt;
        private String builderRecipeFile;
        private String builderUploader;
        private String builderCategories;
        //private String builderComments;
        private String builderFoodFiles;
        private int builderLikes;

        private List<String> builderListCategories;

        private boolean builderMeLike;

        public RecipeBuilder() {}

        public RecipeEntity.RecipeBuilder id (String id) {
            this.builderId = id;
            return this;
        }

        public RecipeEntity.RecipeBuilder name (String name) {
            this.builderName = name;
            return this;
        }

        public RecipeEntity.RecipeBuilder description (String description) {
            this.builderDescription = description;
            return this;
        }

        public RecipeEntity.RecipeBuilder creationDate(long createdAt) {
            this.builderCreatedAt = createdAt;
            return this;
        }

        public RecipeEntity.RecipeBuilder lastModifiedAt (long lastModifiedAt) {
            this.builderLastModifiedAt = lastModifiedAt;
            return this;
        }

        public RecipeEntity.RecipeBuilder recipeFile (String recipeFile) {
            this.builderRecipeFile = recipeFile;
            return this;
        }

        public RecipeEntity.RecipeBuilder uploader (String uploader) {
            this.builderUploader = uploader;
            return this;
        }

        public RecipeEntity.RecipeBuilder categoriesJson (String categoriesJson) {
            this.builderCategories = categoriesJson;
            return this;
        }

        public RecipeEntity.RecipeBuilder categories (List<String> categories) {
            this.builderListCategories = categories;
            return this;
        }

        public RecipeEntity.RecipeBuilder foodFilesJson (String foodFilesJson) {
            this.builderFoodFiles = foodFilesJson;
            return this;
        }

        public RecipeEntity.RecipeBuilder likes (int likes) {
            this.builderLikes = likes;
            return this;
        }

        public RecipeEntity.RecipeBuilder meLike (boolean meLike) {
            this.builderMeLike = meLike;
            return this;
        }

        public RecipeEntity build() {
            RecipeEntity recipe = new RecipeEntity();
            recipe.setId(builderId);
            recipe.setName(builderName);
            recipe.setDescription(builderDescription);
            recipe.setCreationDate(builderCreatedAt);
            recipe.setLastModifiedDate(builderLastModifiedAt);
            recipe.setRecipeFile(builderRecipeFile);
            recipe.setUploader(builderUploader);
            recipe.setStringCategories(builderCategories);
            recipe.setStringFoodFiles(builderFoodFiles);
            recipe.setLikes(builderLikes);
            recipe.setMeLike(builderMeLike ? TRUE : FALSE);

            return recipe;
        }


        public RecipeEntity buildTest() {
            RecipeEntity recipe = new RecipeEntity();
            recipe.setId(builderId);
            recipe.setName(builderName);
            recipe.setDescription(builderDescription);
            recipe.setCreationDate(builderCreatedAt);
            recipe.setLastModifiedDate(builderLastModifiedAt);
            recipe.setCategories(builderListCategories);
            recipe.setLikes(builderLikes);
            recipe.setMeLike(builderMeLike ? TRUE : FALSE);

            return recipe;
        }

    }
}

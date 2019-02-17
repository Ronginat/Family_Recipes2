package com.myapps.ron.family_recipes.utils;

public class Constants {
    // region Notifications
    public static final String BODY = "message";
    public static final String TITLE = "title";
    public static final String CHANNEL = "channel";
    public static final String ID = "id";
    public static final String NOTIFICATION = "notification";

    // endregion

    public static final String RECIPE_ID = "recipe";
    public static final String RECIPE_PATH = "recipe_file";
    public static final String DEFAULT_RECIPE_PATH = "/recipe/default.html";

    public static final String ACTION_UPDATE_FROM_SERVICE = "update_from_service";
    public static final String ACTION_UPLOAD_IMAGES_SERVICE = "upload_images_service";

    public static final int RECIPE_ACTIVITY_CODE = 0;
    public static final int POST_RECIPE_ACTIVITY_CODE = 1;

    public static final long FADE_ANIMATION_DURATION = 200;

    public static final long REFRESH_DELAY = 60000; // 60 seconds

    public static final String DEFAULT_RECIPE_NAME = "anonymous";
    public static final String DEFAULT_RECIPE_DESC = "A sample app to showcase Cognito Identity and the SDK for Android.";
    public static final String DEFAULT_RECIPE_UPLOADER = "unknown";

    public static final int FALSE = 0;
    public static final int TRUE = 1;

    //RecipeActivity
    public static final int MAX_FILES_TO_UPLOAD = com.myapps.ron.family_recipes.network.Constants.MAX_FILES_TO_UPLOAD;

    //PostRecipeActivity
    public static final int MIN_NUMBER_OF_HTML_ELEMENTS = 2;

    public static final String DEFAULT_COLOR = "#827f93";

    //Dark Theme Preferences
    //public static final String[] DARK_THEME = {"always", "auto", "battery", "never"};
    public static final String DARK_THEME_ALWAYS = "always";
    public static final String DARK_THEME_NIGHT_BATTERY_SAVER = "auto";
    public static final String DARK_THEME_BATTERY_SAVER = "battery";
    public static final String DARK_THEME_NEVER = "never";

}

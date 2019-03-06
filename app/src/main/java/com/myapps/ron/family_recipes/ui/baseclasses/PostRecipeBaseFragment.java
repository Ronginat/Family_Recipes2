package com.myapps.ron.family_recipes.ui.baseclasses;

import android.os.Bundle;
import android.view.View;

import com.myapps.ron.family_recipes.FabExtensionAnimator;
import com.myapps.ron.family_recipes.R;
import com.myapps.ron.family_recipes.ui.activities.PostRecipeActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by ronginat on 04/03/2019.
 */
public abstract class PostRecipeBaseFragment extends MyFragment {

    protected PostRecipeActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroyed = false;

        activity = (PostRecipeActivity)getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        onMyViewCreated(view, savedInstanceState);

        togglePersistentUi();
    }

    protected abstract void onMyViewCreated(View view, Bundle savedInstanceState);

    public void toggleFab(boolean show) { activity.toggleFab(show); }

    protected boolean showsFab() { return false; }

    private void togglePersistentUi() {
        //toggleFab(showsFab());
        if (!showsFab())
            setFabExtended(false);
        else if (!restoredFromBackStack() && showsFab()) {
            setFabExtended(true);
            setFabExtended(false, 2000);
        }

        activity.setTitle(getTitle());
        activity.updateFab(getFabState());
        activity.setFabClickListener(getFabClickListener());
    }

    private void setFabExtended(boolean extended) {
        activity.setFabExtended(extended);
    }

    @SuppressWarnings("SameParameterValue")
    protected void setFabExtended(boolean extended, long delay) {
        activity.setFabExtended(extended, delay);
    }

    //protected boolean isFabExtended() { return activity.isFabExtended(); }

    protected String getTitle() {
        return getClass().getSimpleName();
    }

    protected FabExtensionAnimator.GlyphState getFabState() {
        return FabExtensionAnimator.newState(R.string.post_recipe_next, R.drawable.ic_done_fab);
    }

    protected View.OnClickListener getFabClickListener() { return view -> activity.nextFragmentDelayed(); }
}

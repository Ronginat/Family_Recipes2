package com.myapps.ron.family_recipes.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.myapps.ron.family_recipes.R;
import com.myapps.ron.family_recipes.adapters.HtmlElementsAdapter;
import com.myapps.ron.family_recipes.ui.activities.PostRecipeActivity;
import com.myapps.ron.family_recipes.utils.Constants;
import com.myapps.ron.family_recipes.utils.MyFragment;
import com.myapps.ron.family_recipes.viewmodels.PostRecipeViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

/**
 * Created by ronginat on 29/10/2018.
 */
public class AdvancedStepFragment extends MyFragment {
    private final String TAG = getClass().getSimpleName();

    private View view;
    private ViewGroup parent;

    private HtmlElementsAdapter mAdapter;
    private RecyclerView recyclerView;
    private Button preview, sample, reset;

    private PostRecipeActivity activity;
    private PostRecipeViewModel viewModel;

    private SpeedDialView mSpeedDialView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (PostRecipeActivity)getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG, "on attach");
        /*if (parent != null){
            parent.addView(recyclerView);
            parent.addView(preview);
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "on detach");
        //parent.removeAllViews();
    }

    @Override
    public boolean onBackPressed() {
        activity.previousFragment();
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (parent == null) {
            //Log.e(TAG, "on create view");
            view = inflater.inflate(R.layout.content_post_advanced_step, container, false);
            parent = (ViewGroup) view;

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        if (recyclerView == null) {
            recyclerView = view.findViewById(R.id.advanced_step_recycler);
            mSpeedDialView = view.findViewById(R.id.advanced_step_speedDial);
            viewModel =  ViewModelProviders.of(activity).get(PostRecipeViewModel.class);

            initSpeedDial(savedInstanceState == null);
            initRecycler();
        }

        //viewModel =  ViewModelProviders.of(activity).get(PostRecipeViewModel.class);
        /*preview = view.findViewById(R.id.advanced_step_preview_button);
        sample = view.findViewById(R.id.advanced_step_load_sample_button);
        reset = view.findViewById(R.id.advanced_step_reset_button);*/

        activity.setTitle(getString(R.string.nav_main_post_recipe) + " 2/3");
        //setListeners();
    }

    private void initSpeedDial(boolean addActionItems) {
        if (addActionItems) {
            /*Drawable drawable = AppCompatResources.getDrawable(activity, R.drawable.ic_custom_color);
            FabWithLabelView fabWithLabelView = mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id
                    .fab_custom_color, drawable)
                    .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.inbox_primary, getTheme()))
                    .setLabel(R.string.label_custom_color)
                    .setLabelColor(Color.WHITE)
                    .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.inbox_primary,
                            getTheme()))
                    .create());
            if (fabWithLabelView != null) {
                fabWithLabelView.setSpeedDialActionItem(fabWithLabelView.getSpeedDialActionItemBuilder()
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_white_1000,
                                getTheme()))
                        .create());
            }*/

            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_add_action, R.drawable
                    .ic_action_post_black)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.md_blue_grey_100,
                            activity.getTheme()))
                    .setLabel(R.string.post_recipe_advanced_step_fab_add)
                    .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.md_blue_grey_600,
                            activity.getTheme()))
                    .setLabelBackgroundColor(Color.TRANSPARENT)
                    .create());

            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_preview_action, R.drawable.ic_preview_fab)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.md_blue_700,
                            activity.getTheme()))
                    .setLabel(R.string.post_recipe_advanced_step_fab_preview)
                    .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.md_blue_700,
                            activity.getTheme()))
                    .setLabelBackgroundColor(Color.TRANSPARENT)
                    .create());

            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_template_action, R.drawable.ic_template_fab)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.md_light_green_800,
                            activity.getTheme()))
                    .setLabel(R.string.post_recipe_advanced_step_fab_template)
                    .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.md_light_green_800,
                            activity.getTheme()))
                    .setLabelBackgroundColor(Color.TRANSPARENT)
                    //.setTheme(R.style.AppTheme_Purple)
                    .create());

            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_reset_action, R.drawable.ic_reset_fab)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.md_grey_900,
                            activity.getTheme()))
                    .setLabel(R.string.post_recipe_advanced_step_fab_reset)
                    .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.md_grey_900,
                            activity.getTheme()))
                    .setLabelBackgroundColor(Color.TRANSPARENT)
                    .create());

        }
        //Set main action click listener.
        mSpeedDialView.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {
                //Toast.makeText(activity,"Main action clicked!", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "onMainActionSelected");
                return false; // True to keep the Speed Dial open
            }

            @Override
            public void onToggleChanged(boolean isOpen) {
                Log.w(TAG, "Speed dial toggle state changed. Open = " + isOpen);
            }
        });

        //Set option fabs clicklisteners.
        mSpeedDialView.setOnActionSelectedListener(actionItem -> {
            switch (actionItem.getId()) {
                case R.id.fab_add_action:
                    showToast("No label action clicked!\nClosing with animation");
                    mSpeedDialView.close(); // To close the Speed Dial with animation
                    return true; // false will close it without animation
                case R.id.fab_preview_action:
                    showSnackbar(actionItem.getLabel(activity) + " clicked!");
                    break;
                case R.id.fab_template_action:
                    showToast(actionItem.getLabel(activity) + " clicked!\nClosing without animation.");
                    return false; // closes without animation (same as mSpeedDialView.close(false); return false;)
                case R.id.fab_reset_action:
                    showToast(actionItem.getLabel(activity) + " clicked!");
                    break;
                /*case R.id.fab_add_action:
                    mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_replace_action,
                            R.drawable.ic_replace_white_24dp)
                            .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color
                                            .material_orange_500,
                                    getTheme()))
                            .setLabel(getString(R.string.label_replace_action))
                            .create(), ADD_ACTION_POSITION);
                    break;
                case R.id.fab_replace_action:
                    mSpeedDialView.replaceActionItem(new SpeedDialActionItem.Builder(R.id
                            .fab_remove_action,
                            R.drawable.ic_delete_white_24dp)
                            .setLabel(getString(R.string.label_remove_action))
                            .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.inbox_accent,
                                    getTheme()))
                            .create(), ADD_ACTION_POSITION);
                    break;
                case R.id.fab_remove_action:
                    mSpeedDialView.removeActionItemById(R.id.fab_remove_action);
                    break;*/
                default:
                    break;
            }
            return true; // To keep the Speed Dial open
        });

    }

    private Toast mToast;
    private Snackbar mSnackbar;

    private void showToast(String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(activity, text, Toast.LENGTH_LONG);
        mToast.show();
    }

    private void showSnackbar(String text) {
        mSnackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        mSnackbar.setAction("Close", view -> mSnackbar.dismiss());
        mSnackbar.show();
    }

    private void initNextButton() {
        Button nextButton = view.findViewById(R.id.fab);
        nextButton.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_BUTTON_PRESS:
                    nextButton.setText("Next");
                    break;
                case MotionEvent.ACTION_BUTTON_RELEASE:
                    nextButton.setText("");
                    break;
            }
            view.performClick();
            return true;
        });
        nextButton.setOnClickListener(View::performClick);
    }

    /*private void setExtended(boolean extended, boolean force) {
        if (isAnimating || (extended && isExtended() && !force)) return;

        ConstraintSet set = new ConstraintSet();
        set.clone(container.getContext(), extended ? R.layout.fab_extended : R.layout.fab_collapsed);

        TransitionManager.beginDelayedTransition(container, new AutoTransition()
                .addListener(listener).setDuration(150));

        if (extended) button.setText("qwerty");
        else button.setText("");

        set.applyTo(container);
    }*/

    private void initRecycler() {
        mAdapter = new HtmlElementsAdapter(activity);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void setListeners() {
        preview.setOnClickListener(view ->
                activity.showMyDialog(mAdapter.generateHtml("some name" , "long description")));

        View.OnClickListener clickListener = view -> {
            if (mAdapter.getItemCount() > 1) {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            /*if (view.getId() == R.id.advanced_step_reset_button)
                                mAdapter.reset();
                            else if (view.getId() == R.id.advanced_step_load_sample_button) {
                                mAdapter.loadSample();
                            }*/
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                    dialog.dismiss();
                };

                new AlertDialog.Builder(activity)
                        .setMessage(R.string.post_recipe_advanced_step_reset_message)
                        .setPositiveButton(R.string.yes, dialogClickListener)
                        .setNegativeButton(R.string.no, dialogClickListener)
                        .show();
            } /*else if (view.getId() == R.id.advanced_step_load_sample_button){
                mAdapter.loadSample();
            }*/
        };

        sample.setOnClickListener(clickListener);
        reset.setOnClickListener(clickListener);

        activity.nextButton.setOnClickListener(view -> {
            if(mAdapter.checkValidInput()) {
                String html = mAdapter.generateHtml(viewModel.recipe.getName(), viewModel.recipe.getDescription());
                Log.e(TAG, html);
                viewModel.setRecipeFile(activity, html);
                activity.nextFragment();
            } else {
                Toast.makeText(activity, getString(R.string.post_recipe_advanced_step_validation_message, Constants.MIN_NUMBER_OF_HTML_ELEMENTS), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.myapps.ron.family_recipes.ui;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myapps.ron.family_recipes.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ronginat on 24/10/2018.
 */
public class MyPagerAdapter extends PagerAdapter {
    private static final List<Item> items =
            Arrays.asList(new Item(R.color.md_indigo_500), new Item(R.color.md_green_500),
                    new Item(R.color.md_red_500), new Item(R.color.md_orange_500),
                    new Item(R.color.md_purple_500));

    @NonNull @Override public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View item = LayoutInflater.from(container.getContext())
                .inflate(R.layout.material_page, container, false);
        //CardView cardView = item.findViewById(R.id.card_view);
        //cardView.setCardBackgroundColor(
        //    ContextCompat.getColor(container.getContext(), (items.get(position).color)));
        container.addView(item);
        return item;
    }

    @Override public int getCount() {
        return items.size();
    }

    @Override public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private static class Item {
        private final int color;

        private Item(int color) {
            this.color = color;
        }
    }
}

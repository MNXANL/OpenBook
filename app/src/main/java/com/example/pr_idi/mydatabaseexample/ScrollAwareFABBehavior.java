/*
 * Copyright 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.pr_idi.mydatabaseexample;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public final class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {}

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof AppBarLayout || super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            if (dependency.getBottom() == 0 && child.getVisibility() == View.VISIBLE) {
                child.hide();
                return true;
            } else if (dependency.getBottom() == dependency.getHeight() && child.getVisibility() != View.VISIBLE) {
                // update the translation Y
                child.setTranslationY(getFabTranslationYForSnackbar(parent, child));
                child.show();
                return true;
            }
            return false;
        } else {
            return super.onDependentViewChanged(parent, child, dependency);
        }
    }

    /**
     * @see android.support.design.widget.FloatingActionButton.Behavior#getFabTranslationYForSnackbar(CoordinatorLayout, FloatingActionButton)
     */
    private float getFabTranslationYForSnackbar(CoordinatorLayout parent, FloatingActionButton fab) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(fab);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout) {
                minOffset = Math.min(minOffset, view.getTranslationY() - view.getHeight());
            }
        }

        return minOffset;
    }
}
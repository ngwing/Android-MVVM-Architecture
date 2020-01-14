package com.anthony.foodmap.core.dagger.builders;


import com.anthony.foodmap.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();
}

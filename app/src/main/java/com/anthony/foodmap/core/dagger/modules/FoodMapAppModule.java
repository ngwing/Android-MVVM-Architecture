package com.anthony.foodmap.core.dagger.modules;


import android.content.Context;

import com.anthony.foodmap.FoodMapApplication;
import com.anthony.foodmap.util.Utils;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class FoodMapAppModule {

    @Singleton
    @Provides
    Context provideContext(FoodMapApplication application){
        return application;
    }

    @Singleton
    @Provides
    Gson provideGson(){
        return new Gson();
    }

    @Singleton
    @Provides
    Utils provideUtils(Context context){
        return new Utils(context);
    }

}

/**
 * this project was designed and developped by "Rasmi Abdel Wadoud" if this last is used anywhere else without permission
 * you may face some problems
 * */



package com.medical.diabetes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.medical.diabetes.database.DiabetesDbHelper;
import com.medical.diabetes.fragments.infos;
import com.medical.diabetes.fragments.main_screen;
import com.medical.diabetes.fragments.utils.Type_FragmentType;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;
    //
    public static SQLiteDatabase writableDB, readableDB;
    //
    public static boolean firstRun;
    public final String firstRunPrefName = "firstRun";
    //
    public static InputMethodManager inputManager;
    //
    public static Context mainContext;

    /**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //changing theme after splashing the screen
        setTheme(R.style.AppTheme);
        //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        mainContext = this;

        //know if it's the first time for this app to be run
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);

        if (!preferences.contains(firstRunPrefName)) {
            firstRun = true;
            preferences.edit().putBoolean(firstRunPrefName, false).commit();
        } else firstRun = false;


        //preparing database to read from and written into
        DiabetesDbHelper helper = new DiabetesDbHelper(this);
        readableDB = helper.getReadableDatabase();
        writableDB = helper.getWritableDatabase();

        // get ready to use the fragments
        fragmentManager = getSupportFragmentManager();


        //set the infos page in case it is the first run
        if (firstRun) {
            infos infosScreen = new infos();
            setFragmentIntoScreen(infosScreen);
        } else {
            //set the main screen fragment into the activity in case not the firstRun
            setFragmentIntoScreen(new main_screen());
        }

        //get keyboard to close it later on call
        inputManager = ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE));

    }//


    /*to set any fragment to the screen*/
    public static void setFragmentIntoScreen(Type_FragmentType fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        if (!(firstRun) && fragment.type() != 0) fragmentTransaction.addToBackStack(null);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    //
    @Override
    protected void onDestroy() {
        writableDB.close();
        readableDB.close();
        super.onDestroy();
    }

    public static void closeKeyboard(View view) {
        //to hide the keyboard
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        //
    }


}//

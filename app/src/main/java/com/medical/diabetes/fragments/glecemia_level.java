package com.medical.diabetes.fragments;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.R;
import com.medical.diabetes.adapters.GlecemiaMeasuresAdapter;
import com.medical.diabetes.database.DataBaseTablesSchemas;
import com.medical.diabetes.database.ReadGlecemiaTableTask;
import com.medical.diabetes.fragments.utils.Type_FragmentType;
import com.medical.diabetes.models.glecemiaModels.DateTimeHeader;
import com.medical.diabetes.models.glecemiaModels.GlecemiaModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class glecemia_level extends Type_FragmentType {
    //
    public AlertDialog addWindow;
    //
    public RecyclerView recyclerView;
    //
    public ArrayList<DateTimeHeader> list;

    //
    public glecemia_level() {
        // Required empty public constructor
        list = new ArrayList<>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_glecemia_level, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //prepare the list
        recyclerView = view.findViewById(R.id.glecemia_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new GlecemiaMeasuresAdapter(list));
        //
        new ReadGlecemiaTableTask(list, recyclerView.getAdapter()).execute();
        //
        createAddMesureWindow();
        //configure the add measure button
        view.findViewById(R.id.add_mesure_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWindow.show();
                addWindow.getWindow().getDecorView().setBackgroundColor(0);
                //
                addWindow.findViewById(R.id.close_keyboard_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.closeKeyboard(v);
                    }
                });
                //
                addWindow.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addWindow.dismiss();
                    }
                });
                //
                addWindow.findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlecemiaModel newElement = null;

                        // a try catch block to prevent user from setting a wrong value

                        try {
                            double tauxavant = Double.valueOf(((TextView) addWindow.findViewById(R.id.taux_avant_repas)).getText().toString());
                            double tauxapres = Double.valueOf(((TextView) addWindow.findViewById(R.id.taux_apres_repas)).getText().toString());
                            double dozeinsul = Double.valueOf(((TextView) addWindow.findViewById(R.id.doze_insuline_injecte)).getText().toString());
                            newElement = new GlecemiaModel(-1, "", tauxavant, tauxapres, dozeinsul, timeRightNow());
                        } catch (Exception e) {
                            Toast.makeText(v.getContext(), "champ erroné entrez des nombres réels svp!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        MainActivity.closeKeyboard(v);
                        //verifiying if there's a header before if not add one for this day
                        SharedPreferences preferencesFile = getActivity().getPreferences(Context.MODE_PRIVATE);
                        if (!preferencesFile.getString("lastHeaderDateTime", "").equals(todayDate())) {
                            SharedPreferences.Editor editor = preferencesFile.edit();
                            editor.putString("lastHeaderDateTime", todayDate());
                            editor.commit();
                            //
                            DateTimeHeader header = new DateTimeHeader(-1, 1, todayDate());
                            header._ID = saveMesureInDB(header);
                            list.add(header);
                        }

                        //

                        newElement.dateTime = todayDate();
                        //
                        newElement._ID = saveMesureInDB(newElement); /*inserting element into database and getting it's id*/
                        list.add(newElement);
                        //
                        recyclerView.getAdapter().notifyDataSetChanged();
                        addWindow.dismiss();
                    }
                });

            }
        });

        //
        view.findViewById(R.id.back_navigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int type() {
        return 50;
    }

    private void createAddMesureWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(LayoutInflater.from(getContext()).inflate(R.layout.add_glecemia_window, null));
        addWindow = (builder).create();
    }


    public long saveMesureInDB(DateTimeHeader current) { /*returns the new element id*/
        ContentValues values = new ContentValues();
        //
        if (current.header == 1) { /*in case it's a header*/
            //
            values.put(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_HEADER, 1);
            values.put(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_DATE_TIME, current.dateTime);

            return MainActivity.writableDB.insert(DataBaseTablesSchemas.GlecemiaMesuresTable.TABLE_NAME, null, values);
        }

        GlecemiaModel castedCurrent = (GlecemiaModel) current; /*in case it's not a header*/
        //
        values.put(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_HEADER, 0);
        values.put(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_DATE_TIME, castedCurrent.dateTime);
        values.put(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_TIME, castedCurrent.time);
        values.put(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_TAUX_AVANT_REPAS, castedCurrent.tauxAvantRepas);
        values.put(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_TAUX_APRES_REPAS, castedCurrent.tauxApresRepas);
        values.put(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_DOZE_INSULINE_INJECTEE, castedCurrent.dozeInsuline);
        //

        return MainActivity.writableDB.insert(DataBaseTablesSchemas.GlecemiaMesuresTable.TABLE_NAME, null, values);
    }

    /**/
    public static String todayDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String date="";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SATURDAY:
                date = "Samedi";
                break;
            case Calendar.SUNDAY:
                date = "Dimanche";
                break;
            case Calendar.MONDAY:
                date = "Lundi";
                break;
            case Calendar.TUESDAY:
                date = "Mardi";
                break;
            case Calendar.WEDNESDAY:
                date = "Mercredi";
                break;
            case Calendar.THURSDAY:
                date = "Jeudi";
                break;
            case Calendar.FRIDAY:
                date = "Vendredi";
                break;
        }

        date += " le " + dateFormat.format(new Date());

        return date;
    }

    /**/
    public String timeRightNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(new Date());
    }
}//

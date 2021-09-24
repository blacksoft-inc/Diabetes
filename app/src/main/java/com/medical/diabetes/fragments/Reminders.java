package com.medical.diabetes.fragments;


import android.app.AlertDialog;
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

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.R;
import com.medical.diabetes.adapters.RemindersListAdapter;
import com.medical.diabetes.database.ReadRemindersTableTask;
import com.medical.diabetes.fragments.utils.Type_FragmentType;
import com.medical.diabetes.models.Reminder;

import com.medical.diabetes.windows.RemindersWindow;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Reminders extends Type_FragmentType {
    public AlertDialog window;
    //
    public ArrayList<Reminder> alarmsList;
    //
    public RecyclerView recyclerView;
    //
    public static RemindersWindow remindersWindow;
    //
    public TextView rapport;

    //
    public Reminders() {
        alarmsList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminders, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        rapport = view.findViewById(R.id.rapport_textview);

        //preparing the window to appear
        createWindow();
        //
        recyclerView = view.findViewById(R.id.reminders_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RemindersListAdapter adapter = new RemindersListAdapter(alarmsList, rapport);
        adapter.recyclerView = recyclerView;
        adapter.window = window;
        recyclerView.setAdapter(adapter);

        //read alarms from database
        new ReadRemindersTableTask(alarmsList, recyclerView, rapport).execute();
        //
        (view.findViewById(R.id.add_alarm_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showing the dialog (window)
                remindersWindow.current = null;
                remindersWindow.showWindow(window, recyclerView, rapport);
            }
        });
        //
        view.findViewById(R.id.back_navigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.firstRun)
                    getActivity().onBackPressed();
                else {
                    MainActivity.firstRun = false;
                    MainActivity.setFragmentIntoScreen(new main_screen());}
            }
        });
        //
        remindersWindow = new RemindersWindow(alarmsList, rapport);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public int type() {
        return 40;
    }

    /*initialaztion of the custom dialog with its builder once at the first appearance of remiders fragment*/
    public final void createWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(getLayoutInflater().inflate(R.layout.add_modify_reminder, null));
        window = builder.create();
    }


}//

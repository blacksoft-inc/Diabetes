package com.medical.diabetes.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.R;
import com.medical.diabetes.adapters.MainSectionsAdapter;
import com.medical.diabetes.database.PrepareEmailThenSendItTask;
import com.medical.diabetes.database.ReadGlecemiaTableTask;
import com.medical.diabetes.database.ReadInfosTableTask;
import com.medical.diabetes.fragments.utils.Type_FragmentType;
import com.medical.diabetes.models.Section;
import com.medical.diabetes.models.infosLine.HeaderInfos;
import com.medical.diabetes.models.infosLine.InfosModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class main_screen extends Type_FragmentType {
    private Section[] sections;

    public main_screen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // prepare the list of sections in Array
        sections = new Section[4];
        sections[0] = new Section("Instructions", R.drawable.instructions_section_ic);
        sections[1] = new Section("Mesures", R.drawable.glicimia_level_section_ic);
        sections[2] = new Section("Table alimentaire", R.drawable.calories_table_section_ic);
        sections[3] = new Section("Recettes", R.drawable.recipe_section_ic);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initializing the recyclerView to get a grid of sections

        RecyclerView sectionsGrid = view.findViewById(R.id.sections_grid);
        sectionsGrid.setHasFixedSize(true);
        sectionsGrid.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        sectionsGrid.setAdapter(new MainSectionsAdapter(sections, this.getContext()));

        //initializing buttons
        initializeButtons(view);
    }

    /**/
    @Override
    public int type() {
        return 0;
    }

    /**/
    public void initializeButtons(View view) {
        final Button infos = view.findViewById(R.id.patient_doctor_infos),
                alarm = view.findViewById(R.id.alarm_notifs);

        //
        infos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setFragmentIntoScreen(new infos());
            }
        });
        //
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setFragmentIntoScreen(new Reminders());
            }
        });
        //
        view.findViewById(R.id.animated_background).
                startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.send_rapport_background_animation));
        //
        ImageView sendRapport = view.findViewById(R.id.send_rapport);
        sendRapport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PrepareEmailThenSendItTask().execute();
            }
        });


    }


}//

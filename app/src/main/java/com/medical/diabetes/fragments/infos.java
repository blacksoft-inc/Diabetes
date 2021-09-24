package com.medical.diabetes.fragments;

import android.media.midi.MidiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.R;
import com.medical.diabetes.adapters.InfosListAdapter;
import com.medical.diabetes.database.ReadInfosTableTask;
import com.medical.diabetes.database.WriteInfosTableTask;
import com.medical.diabetes.fragments.utils.Type_FragmentType;
import com.medical.diabetes.models.infosLine.HeaderInfos;
import com.medical.diabetes.models.infosLine.InfosModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class infos extends Type_FragmentType {
    public RecyclerView infosList;
    public ArrayList<HeaderInfos> infos;
    private int backgroundColor;

    public infos() {
        infos = new ArrayList<>();
        fillWithEmptyDataInCaseOfFirstRun();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_infos, container, false);
    }


    /**/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backgroundColor = view.getSolidColor();

        // initializing the list of infos
        infosList = view.findViewById(R.id.infos_list);
        infosList.setHasFixedSize(true);
        infosList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        infosList.setAdapter(new InfosListAdapter(this.getContext(), infos));


        //start back navigation  in case it's not the first run
        if (!MainActivity.firstRun) {

            ((ImageView) view.findViewById(R.id.back_navigation)).setImageResource(R.drawable.previous_ic);

            //fill infosList with its data both in first and none first runs
            new ReadInfosTableTask(infos, MainActivity.readableDB, infosList).execute();
            //
            (view.findViewById(R.id.back_navigation)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //close keyboard if it's on the screen
                    MainActivity.closeKeyboard(v);
                    //
                    getActivity().onBackPressed();
                }
            });


        } else {
            (view.findViewById(R.id.back_navigation)).setBackgroundColor(backgroundColor);
        }


        /*save infos when clicked*/
        view.findViewById(R.id.save_infos_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new WriteInfosTableTask(infos, MainActivity.writableDB, true).execute();

                //close keyboard if it's on the screen
                MainActivity.closeKeyboard(v);
                //return to main screen if not first run else go to reminders page
                if (MainActivity.firstRun)
                    MainActivity.setFragmentIntoScreen(new Reminders());

                else {

                    getActivity().onBackPressed();
                }
                //making a toast
                Toast toast = Toast.makeText(MainActivity.mainContext, "Contenu sauvegardé", Toast.LENGTH_SHORT);
                toast.show();
                //

            }
        });

        // close keyboard imageView
        view.findViewById(R.id.close_keyboard_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.closeKeyboard(v);
            }
        });

    }

    /**/
    @Override
    public int type() {
        return 20;
    }

    /**/
    public void fillWithEmptyDataInCaseOfFirstRun() {
        //write empty data in case it's the first run
        if (MainActivity.firstRun) {
            /*Malade infos*/
            infos.add(new HeaderInfos(1, HeaderInfos.HEADER_TYPE_MALADE));

            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_MALADE, InfosModel.CONTENT_TYPE_TEXT
                    , "Nom", "", 1));
            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_MALADE, InfosModel.CONTENT_TYPE_TEXT
                    , "Prènom", "", 1));
            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_MALADE, InfosModel.CONTENT_TYPE_DATE
                    , "Date de Naissance", "", 1));
            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_MALADE, InfosModel.CONTENT_TYPE_TEXT
                    , "Adresse", "", 0));
            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_MALADE, InfosModel.CONTENT_TYPE_NUMBER
                    , "N° de sécurité sociale", "", 1));
            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_MALADE, InfosModel.CONTENT_TYPE_NUMBER
                    , "Téléphone", "", 0));



            /*Medecin infos*/
            infos.add(new HeaderInfos(1, HeaderInfos.HEADER_TYPE_MEDECIN));

            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_MEDECIN, InfosModel.CONTENT_TYPE_TEXT
                    , "Nom complet", "", 0));
            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_MEDECIN, InfosModel.CONTENT_TYPE_NUMBER
                    , "Téléphone", "", 0));
            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_MEDECIN, InfosModel.CONTENT_TYPE_TEXT
                    , "E-mail", "", 1));
            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_MEDECIN, InfosModel.CONTENT_TYPE_NUMBER
                    , "Envoyer un rapport chaque x jours", "", 1));

            /*Urgence infos*/
            infos.add(new HeaderInfos(1, HeaderInfos.HEADER_TYPE_NUMS_URGENCE));

            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_NUMS_URGENCE, InfosModel.CONTENT_TYPE_NUMBER
                    , "Num 1", "", 1));
            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_NUMS_URGENCE, InfosModel.CONTENT_TYPE_NUMBER
                    , "Num 2", "", 0));
            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_NUMS_URGENCE, InfosModel.CONTENT_TYPE_NUMBER
                    , "Num 3", "", 0));

            /*Hopital infos*/
            infos.add(new HeaderInfos(1, HeaderInfos.HEADER_TYPE_HOPITAL));

            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_HOPITAL, InfosModel.CONTENT_TYPE_TEXT
                    , "Adresse", "", 0));
            infos.add(new InfosModel(0, HeaderInfos.HEADER_TYPE_HOPITAL, InfosModel.CONTENT_TYPE_NUMBER
                    , "Téléphone", "", 0));

            //execute writing task in a parallel thread
            new WriteInfosTableTask(infos, MainActivity.writableDB, false).execute();
        }
    }


}//

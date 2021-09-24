package com.medical.diabetes.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.R;
import com.medical.diabetes.fragments.WebNavigator;
import com.medical.diabetes.fragments.glecemia_level;
import com.medical.diabetes.models.Section;

public class MainSectionsAdapter extends RecyclerView.Adapter<MainSectionsAdapter.SectionHolder> {
    private Section[] sections;


    public MainSectionsAdapter(Section[] sections, Context context) {
        this.sections = sections;
    }


    public static class SectionHolder extends RecyclerView.ViewHolder {

        View sectionLayout;

        public SectionHolder(@NonNull View layout) {
            super(layout);
            sectionLayout = layout;
        }
    }//

    /**/
    @NonNull
    @Override
    public SectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_screen_section_model, parent, false);
        SectionHolder holder = new SectionHolder(layout);
        return holder;
    }

    /**/

    @Override
    public void onBindViewHolder(@NonNull final SectionHolder holder, final int position) {
        View sectionLayout = holder.sectionLayout;

        // setting the content in its layout
        ((TextView) sectionLayout.findViewById(R.id.main_section_title)).setText(sections[position].title);
        ((ImageView) sectionLayout.findViewById(R.id.main_section_img)).setImageResource(sections[position].imgRes);
        //
        sectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (sections[position].imgRes) {
                    case R.drawable.glicimia_level_section_ic:
                        MainActivity.setFragmentIntoScreen(new glecemia_level());
                        break;
                    case R.drawable.instructions_section_ic:
                        MainActivity.setFragmentIntoScreen(new WebNavigator("Instructions", "instructions/instructions.html"));
                        break;
                    case R.drawable.calories_table_section_ic:
                        MainActivity.setFragmentIntoScreen(new WebNavigator("Table Alimentaire", "calories_table.html"));
                        break;
                    case R.drawable.recipe_section_ic:
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setView(LayoutInflater.from(MainActivity.mainContext).inflate(R.layout.recettes_window, null, false));
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        //
                        RecyclerView recettesList = dialog.findViewById(R.id.recettesList);
                        recettesList.setLayoutManager(new LinearLayoutManager(v.getContext()));
                        recettesList.setAdapter(new RecettesAdapter(dialog));
                        recettesList.setHasFixedSize(true);
                        //
                        dialog.findViewById(R.id.dismiss_window).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return sections.length;
    }
}//

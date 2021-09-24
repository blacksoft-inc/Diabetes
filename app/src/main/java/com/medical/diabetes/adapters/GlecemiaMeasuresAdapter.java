
/**
 * this project was designed and developped by "Rasmi Abdel Wadoud" if this last is used anywhere else without permission
 * you may face some problems
 * */

package com.medical.diabetes.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.R;
import com.medical.diabetes.database.DataBaseTablesSchemas;
import com.medical.diabetes.models.glecemiaModels.DateTimeHeader;
import com.medical.diabetes.models.glecemiaModels.GlecemiaModel;

import java.util.ArrayList;
import java.util.List;

public class GlecemiaMeasuresAdapter extends RecyclerView.Adapter<GlecemiaMeasuresAdapter.ParentViewHolder> {
    private ArrayList<DateTimeHeader> mesuresList;


    //Parent viewholder
    public class ParentViewHolder extends RecyclerView.ViewHolder {
        public View container;

        public ParentViewHolder(View container) {
            super(container);
            this.container = container;
        }
    }

    //holder1
    public class HeaderDateTimeHolder extends ParentViewHolder {

        public HeaderDateTimeHolder(View container) {
            super(container);
            this.container = container;
        }
    }

    //holder2
    public class GlecemiaMesureHolder extends ParentViewHolder {


        public GlecemiaMesureHolder(View container) {
            super(container);
            this.container = container;
        }
    }


    public GlecemiaMeasuresAdapter(ArrayList<DateTimeHeader> mesuresList) {
        this.mesuresList = mesuresList;
    }

    //
    @Override
    public int getItemViewType(int position) {
        if (mesuresList.get(position) instanceof GlecemiaModel)
            return 1;

        else return 0;
    }

    //
    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) return new HeaderDateTimeHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.glecemia_date_time_header, parent, false));
        else return new GlecemiaMesureHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.glecemia_mesure, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, final int position) {
        //
        View container = holder.container;
        TextView textView = (container.findViewById(R.id.header_date_time_textview));
        //
        final DateTimeHeader current = mesuresList.get(position);
        //
        if (holder.getItemViewType() == 0) {/*in case it's a header to display date and time*/
            textView.setText(current.dateTime);
            return;
        }
        // in case it's not a header but an element
        final GlecemiaModel currentGlec = (GlecemiaModel) current;
        //
        textView = container.findViewById(R.id.glecemia_infos);
        textView.setText(Html.fromHtml(
                "<font color=\"#aaaaaa\">avant repas: </font> " + currentGlec.tauxAvantRepas + " <small>g/l</small>"
                        + "<br><font color=\"#aaaaaa\">après repas: </font> " + currentGlec.tauxApresRepas + " <small>g/l</small>"
                        + "<br><font color=\"#aaaaaa\">dose d'insuline: </font> " + currentGlec.dozeInsuline + " <small>ul</small>"));
        //
        ((TextView) container.findViewById(R.id.mesure_time)).setText(currentGlec.time);
        // state of person in this mesure by default it's good
        ImageView glecemiaState = container.findViewById(R.id.glecemia_state);
        if ((currentGlec.tauxApresRepas <= 0.95 && (currentGlec.tauxApresRepas > 0.45))
                || (currentGlec.tauxApresRepas < 1.4 && (currentGlec.tauxApresRepas > 1.26))) {

            glecemiaState.setImageResource(R.drawable.glecemia_bad_ic);
            glecemiaState.setBackgroundResource(R.drawable.orange_circle);

        } else if ((currentGlec.tauxApresRepas >= 1.4) || (currentGlec.tauxApresRepas <= 0.45)) {
            glecemiaState.setImageResource(R.drawable.glecemia_bad_ic);
            glecemiaState.setBackgroundResource(R.drawable.red_circle);
        }

        //delete button
        View deleteButton = container.findViewById(R.id.mesure_delete_imgview);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // remove the header if it's before this element and remove the element
                    if (!mesuresList.isEmpty()) {
                        MainActivity.writableDB.delete(DataBaseTablesSchemas.GlecemiaMesuresTable.TABLE_NAME, "_id = " + currentGlec._ID, null);
                        mesuresList.remove(position);
                        notifyDataSetChanged();
                        //
                        if (mesuresList.get(position - 1).header == 1) {
                            if (mesuresList.size() > position) {
                                if (mesuresList.get(position).header == 1) {
                                    MainActivity.writableDB.delete(DataBaseTablesSchemas.GlecemiaMesuresTable.TABLE_NAME,
                                            "_id = " + mesuresList.get((position - 1))._ID, null);
                                    //
                                    mesuresList.remove((position - 1));
                                    notifyDataSetChanged();
                                    //
                                    SharedPreferences.Editor editor = ((Activity) MainActivity.mainContext)
                                            .getPreferences(Context.MODE_PRIVATE).edit();
                                    editor.putString("lastHeaderDateTime", "");
                                    editor.commit();
                                }
                            } else {
                                MainActivity.writableDB.delete(DataBaseTablesSchemas.GlecemiaMesuresTable.TABLE_NAME,
                                        "_id = " + mesuresList.get((position - 1))._ID, null);
                                //
                                mesuresList.remove((position - 1));
                                notifyDataSetChanged();
                                //
                                SharedPreferences.Editor editor = ((Activity) MainActivity.mainContext)
                                        .getPreferences(Context.MODE_PRIVATE).edit();
                                editor.putString("lastHeaderDateTime", "");
                                editor.commit();

                            }
                        }

                    }
                    Log.v("list_size", "sizeList" + mesuresList.size());
                }


            });

    }//


    @Override
    public int getItemCount() {
        return mesuresList.size();
    }
}//

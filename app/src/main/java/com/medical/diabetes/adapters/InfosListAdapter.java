/**
 * this project was designed and developped by "Rasmi Abdel Wadoud" if this last is used anywhere else without permission
 * you may face some problems
 * */

package com.medical.diabetes.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.medical.diabetes.R;
import com.medical.diabetes.models.infosLine.HeaderInfos;
import com.medical.diabetes.models.infosLine.InfosModel;

import java.util.ArrayList;

public class InfosListAdapter extends RecyclerView.Adapter<InfosListAdapter.ParentViewHolder> {
    public ArrayList<HeaderInfos> infos;
    public Context context;
    public TextChangingListener textChangingListener;

    // what to do if the user is writing text in textfield
    public class TextChangingListener implements TextWatcher {
        public InfosModel info;

        public TextChangingListener() {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            info.content = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    //
    public InfosListAdapter(Context context, ArrayList<HeaderInfos> infos) {
        this.context = context;
        this.infos = infos;
    }

    //View holders
    //Parent view holder
    public class ParentViewHolder extends RecyclerView.ViewHolder {
        View layout;

        public ParentViewHolder(View layout) {
            super(layout);
            this.layout = layout;
        }

    }

    //view holder for headers
    public class HeaderHolder extends ParentViewHolder {

        public HeaderHolder(View layout) {
            super(layout);
        }
    }

    //view holder for a line of infos
    public class LineHolder extends ParentViewHolder {

        public LineHolder(View layout) {
            super(layout);
            textChangingListener = new TextChangingListener();
        }


    }//

    /**/
    @Override
    public int getItemViewType(int position) {

        if (infos.get(position).header == 1) {
            return 0;
        } else
            switch (((InfosModel) (infos.get(position))).contentType) {
                case InfosModel.CONTENT_TYPE_TEXT:
                    return 1;

                case InfosModel.CONTENT_TYPE_NUMBER:
                    return 2;

                case InfosModel.CONTENT_TYPE_DATE:
                    return 3;
            }

        return 0;
    }

    /**/
    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == 0) {
            return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.info_line_header, parent, false));
        }
        if (textChangingListener == null) textChangingListener = new TextChangingListener();
        return new LineHolder(LayoutInflater.from(context).inflate(R.layout.info_line_text_field, parent, false));

    }

    /**/
    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {

        View container = holder.layout;

        if (holder.getItemViewType() == 0) {
            ((TextView) (container.findViewById(R.id.line_infos_header_name))).setText((infos.get(position).headerType));
            return;
        }

        //in case it's not a header
        InfosModel infosLine = (InfosModel) infos.get(position);

        //filling with info name
        ((TextInputLayout) (container.findViewById(R.id.text_field_container))).setHint(infosLine.infosName);

        //filling with content and save it in the ArrayList while the user is writing and changing the text
        TextInputEditText editText = (container.findViewById(R.id.text_field));
        editText.setText(infosLine.content);

        //error flag if content is empty and must be filled
        if ((infosLine.must_be_filled == 1) && (infosLine.content.isEmpty()))
            editText.setError("champ obligatoire");


        switch (holder.getItemViewType()) {

            case 2:
                if (infosLine.infosName.equals("Téléphone") || infosLine.infosName.contains("Num"))
                    editText.setInputType(InputType.TYPE_CLASS_PHONE);
                else editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                return;

            case 3:
                editText.setInputType(InputType.TYPE_CLASS_DATETIME);
                return;
        }

    }


    @Override
    public void onViewAttachedToWindow(@NonNull final ParentViewHolder holder) {

        if (holder.getItemViewType() != 0) {
            ((TextInputEditText) holder.layout.findViewById(R.id.text_field)).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    ((InfosModel) infos.get(holder.getLayoutPosition())).content = s.toString();
                }
            });
        }


        super.onViewAttachedToWindow(holder);
    }

    /*saving content while scrolling*/
    @Override
    public void onViewDetachedFromWindow(@NonNull ParentViewHolder holder) {
        if (holder.getItemViewType() != 0) {
            ((InfosModel) infos.get(holder.getLayoutPosition())).content =
                    ((TextInputEditText) holder.layout.findViewById(R.id.text_field)).getText().toString();
        }
    }

    /**/
    @Override
    public int getItemCount() {
        return infos.size();
    }


}//

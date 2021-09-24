package com.medical.diabetes.adapters;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.R;
import com.medical.diabetes.database.DataBaseTablesSchemas;
import com.medical.diabetes.fragments.Reminders;
import com.medical.diabetes.fragments.utils.AlarmUtils;
import com.medical.diabetes.models.Reminder;

import java.util.ArrayList;


public class RemindersListAdapter extends RecyclerView.Adapter<RemindersListAdapter.ReminderHolder> {
    public ArrayList<Reminder> alarmsList;

    //
    public AlertDialog window;
    public RecyclerView recyclerView;
    public TextView rapport;

    //
    public RemindersListAdapter(ArrayList<Reminder> alarmsList, TextView rapport) {
        this.alarmsList = alarmsList;
        this.rapport = rapport;
    }


    public class ReminderHolder extends RecyclerView.ViewHolder {
        public View layout;

        public ReminderHolder(View layout) {
            super(layout);
            this.layout = layout;
        }
    }//


    @NonNull
    @Override
    public ReminderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReminderHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminders_list_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ReminderHolder holder, final int position) {
        holder.setIsRecyclable(false);
        //
        final View container = holder.layout;
        final Reminder current = alarmsList.get(position);

        // initialization of edit button
        ImageView editCurrent = container.findViewById(R.id.reminder_edit_imgview);
        if (!editCurrent.hasOnClickListeners())
            editCurrent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Reminders.remindersWindow.current = current;
                    Reminders.remindersWindow.showWindow(window, recyclerView, rapport);
                }
            });


        // initialization of delete button
        View delete = container.findViewById(R.id.reminder_delete_imgview);
        if (!delete.hasOnClickListeners())
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.writableDB.delete(DataBaseTablesSchemas.RemindersTable.TABLE_NAME,
                            "_id = " + current._ID, null);
                    //
                    ArrayList<Integer> repeatDaysIntList = AlarmUtils.repeatDaysIntList(current.days);
                    /*removing all alarms */
                    if (repeatDaysIntList.size() == 0)
                        AlarmUtils.removeAlarm(container.getContext(), current._ID);
                    else
                        for (int i = 0; i < repeatDaysIntList.size(); i++) {
                            AlarmUtils.removeAlarm(container.getContext(), (repeatDaysIntList.get(i) + (current._ID * 10)));
                        }
                    //
                    alarmsList.remove(current);
                    notifyDataSetChanged();
                    Toast.makeText(container.getContext(), "Alarm supprimée", Toast.LENGTH_LONG).show();
                }
            });


        // filling title and description
        TextView title = container.findViewById(R.id.reminder_title);
        TextView description = container.findViewById(R.id.reminder_description);
        title.setText(current.title);
        description.setText(Html.fromHtml(((current.timesPerWeek() > 0) ? current.timesPerWeek() + " fois par semaine" : "") +
                "  <font color=#000> a " + current.time + "</color>"));

        // changing state and updating it
        final ImageView state = container.findViewById(R.id.reminder_state);
        if (current.state == Reminder.STATE_PASSIVE) {
            state.setBackgroundResource(R.drawable.reminders_state_off);
            state.setImageResource(R.drawable.alarm_off_ic);

        }

        if (!state.hasOnClickListeners())
            state.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    if (current.state == Reminder.STATE_ACTIVE) {
                        state.setBackgroundResource(R.drawable.reminders_state_off);
                        state.setImageResource(R.drawable.alarm_off_ic);
                        current.state = Reminder.STATE_PASSIVE;
                        //
                        ArrayList<Integer> repeatDaysIntList = AlarmUtils.repeatDaysIntList(current.days);
                        /*removing all alarms */
                        if (repeatDaysIntList.size() == 0)
                            AlarmUtils.removeAlarm(container.getContext(), current._ID);
                        else
                            for (int i = 0; i < repeatDaysIntList.size(); i++) {
                                AlarmUtils.removeAlarm(container.getContext(), (repeatDaysIntList.get(i) + (current._ID * 10)));
                            }
                        //
                        Toast.makeText(v.getContext(), "Alarm desactivé", Toast.LENGTH_SHORT).show();
                    } else {
                        state.setBackgroundResource(R.drawable.reminders_state_on);
                        state.setImageResource(R.drawable.alarm_on_ic);
                        current.state = Reminder.STATE_ACTIVE;
                        AlarmUtils.addAlarm(container.getContext(), current);
                        Toast.makeText(v.getContext(), "Alarm activé", Toast.LENGTH_SHORT).show();
                    }
                    //update row in database
                    ContentValues row = new ContentValues();
                    row.put(DataBaseTablesSchemas.RemindersTable._ID, current._ID);
                    row.put(DataBaseTablesSchemas.RemindersTable.COLUMN_TITLE, current.title);
                    row.put(DataBaseTablesSchemas.RemindersTable.COLUMN_STATE, current.state);
                    row.put(DataBaseTablesSchemas.RemindersTable.COLUMN_TIME, current.time);
                    row.put(DataBaseTablesSchemas.RemindersTable.COLUMN_DAYS, current.days);
                    MainActivity.writableDB.replace(DataBaseTablesSchemas.RemindersTable.TABLE_NAME, null, row);
                }

            });

    }

    @Override
    public int getItemCount() {
        return alarmsList.size();
    }

}//

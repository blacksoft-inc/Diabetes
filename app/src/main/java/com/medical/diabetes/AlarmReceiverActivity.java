/**
 * this project was designed and developped by "Rasmi Abdel Wadoud" if this last is used anywhere else without permission
 * you may face some problems
 */

package com.medical.diabetes;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.medical.diabetes.database.DataBaseTablesSchemas;
import com.medical.diabetes.database.DiabetesDbHelper;
import com.medical.diabetes.models.Reminder;

public class AlarmReceiverActivity extends Activity {
    private MediaPlayer player;
    private Reminder reminder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove title and set background to transparent
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        //
        setContentView(R.layout.alarm_received_dialog);

        // flags to turn screen on while locked or sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        // play sound
        player = MediaPlayer.create(this, R.raw.alarm_buzzer);
        player.setScreenOnWhilePlaying(true);
        player.start();


        // get infos abt the alarm
        final Intent intent = getIntent();
        Long id = intent.getLongExtra("reminder_id", -1);

        DiabetesDbHelper helper = new DiabetesDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        //
        Cursor cursor = db.query(DataBaseTablesSchemas.RemindersTable.TABLE_NAME,
                null, "_id = " + id,
                null,
                null,
                null,
                null);

        //
        cursor.moveToFirst();
        reminder = new Reminder(cursor.getInt(cursor.getColumnIndex(DataBaseTablesSchemas.RemindersTable._ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseTablesSchemas.RemindersTable.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(DataBaseTablesSchemas.RemindersTable.COLUMN_TIME)),
                cursor.getString(cursor.getColumnIndex(DataBaseTablesSchemas.RemindersTable.COLUMN_DAYS)));

        // filling window (activity with infos) and initializing buttons
        ((TextView) findViewById(R.id.alarm_title_and_time)).setText(
                Html.fromHtml(reminder.title + "  <small>" + reminder.time + "</small>"));

        findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //case of repetition
        if (!reminder.days.isEmpty()) return;
        else {//in case there's no repetition

            // prepare the row to be updated in database
            ContentValues row = new ContentValues();
            row.put(DataBaseTablesSchemas.RemindersTable._ID, reminder._ID);
            row.put(DataBaseTablesSchemas.RemindersTable.COLUMN_TITLE, reminder.title);
            row.put(DataBaseTablesSchemas.RemindersTable.COLUMN_TIME, reminder.time);
            row.put(DataBaseTablesSchemas.RemindersTable.COLUMN_DAYS, reminder.days);

            // changing the state

            reminder.state = Reminder.STATE_PASSIVE;
            row.put(DataBaseTablesSchemas.RemindersTable.COLUMN_STATE, reminder.state);
            //
            cursor.close();
            db.close();
            db = helper.getWritableDatabase();
            db.replace(DataBaseTablesSchemas.RemindersTable.TABLE_NAME, null, row);
            db.close();
        }
        //prepare notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Alarme")
                .setSmallIcon(R.drawable.diabete_app_ic)
                .setContentTitle("Glyce Me")
                .setContentText(reminder.title + " " + reminder.time)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(Integer.valueOf(reminder._ID + ""), builder.build());

        //

    }//


    @Override
    public void finish() {
        if (player != null) player.release();
        player = null;
        super.finish();
    }


}

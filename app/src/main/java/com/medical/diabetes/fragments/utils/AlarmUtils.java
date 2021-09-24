package com.medical.diabetes.fragments.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.medical.diabetes.MainActivity;
import com.medical.diabetes.models.Reminder;
import com.medical.diabetes.services.ReminderCastReceiver;

import java.util.ArrayList;
import java.util.Calendar;

public final class AlarmUtils {

    //Add a new alarm
    public static void addAlarm(Context context, Reminder current) {

        //
        Intent intent = new Intent(context, ReminderCastReceiver.class);
        intent.putExtra("reminder_id", current._ID); /* putting informations to know how to handle it when it rings*/
        //
        PendingIntent pendingIntent;
        //
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String[] time = current.time.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(time[1]));
        calendar.set(Calendar.SECOND, 0);
        //phone sdk version to decide which method to use (idle etc...)
        int minSdk = Build.VERSION.SDK_INT;
        //
        long timeInMillis = calendar.getTimeInMillis();
        if (timeInMillis <= System.currentTimeMillis())
            timeInMillis += ((24 - calendar.get(Calendar.HOUR_OF_DAY)) * 60 * 60 * 1000) + calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000;

        //case no repetition
        if (current.days.isEmpty()) {
            //
            pendingIntent = PendingIntent.getBroadcast(MainActivity.mainContext,
                    Integer.valueOf(current._ID + ""), intent, PendingIntent.FLAG_ONE_SHOT);
            //
            if (minSdk < Build.VERSION_CODES.KITKAT)
                manager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            else if (minSdk >= Build.VERSION_CODES.M)
                manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            else
                manager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

        } else { // in case of repetition
            //
            int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
            ArrayList<Integer> repeatDays = repeatDaysIntList(current.days);

            //set repetitions for each day of this week only
            long repeatTimeInMillis = 7 * 24 * 60 * 60 * 1000;
            long timeInMillis2;
            for (int i = 0; i < repeatDays.size(); i++) {
                //calculating the time of the alarm
                timeInMillis2 = timeInMillis;
                if (currentDay <= repeatDays.get(i)) {
                    timeInMillis2 += ((repeatDays.get(i) - currentDay) * 24 * 60 * 60 * 1000);
                } else {
                    timeInMillis2 += (((7 - currentDay) + repeatDays.get(i)) * 24 * 60 * 60 * 1000);
                }

                // setting alarm id to be defined with
                pendingIntent = PendingIntent.getBroadcast(MainActivity.mainContext,
                        Integer.valueOf(((current._ID * 10) + repeatDays.get(i)) + ""), intent, 0); /*new id for each day*/

                //
                manager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis2, repeatTimeInMillis, pendingIntent);

            }

        }
    }

    /*cancel schedueled alarm*/
    public static void removeAlarm(Context context, long _id) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //
        Intent intent = new Intent(context, ReminderCastReceiver.class);
        //
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.mainContext,
                Integer.valueOf(_id + ""), intent, 0);
        //
        if (pendingIntent != null)
            manager.cancel(pendingIntent);
    }

    /*know repeat days from string and return arraylist of integers*/
    public static ArrayList<Integer> repeatDaysIntList(String days) {
        ArrayList<Integer> daysInt = new ArrayList<>();
        /*know all the repetitive days*/
        if (days.contains("sat")) daysInt.add(Reminder.SATURDAY_INT);
        if (days.contains("sun")) daysInt.add(Reminder.SUNDAY_INT);
        if (days.contains("mon")) daysInt.add(Reminder.MONDAY_INT);
        if (days.contains("tues")) daysInt.add(Reminder.TUESDAY_INT);
        if (days.contains("wed")) daysInt.add(Reminder.WEDNESDAY_INT);
        if (days.contains("thur")) daysInt.add(Reminder.THURSDAY_INT);
        if (days.contains("fri")) daysInt.add(Reminder.FRIDAY_INT);


        return daysInt;
    }


}

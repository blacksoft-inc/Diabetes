/**
 * this project was designed and developped by "Rasmi Abdel Wadoud" if this last is used anywhere else without permission
 * you may face some problems
 * */































package com.medical.diabetes.services;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;


import com.medical.diabetes.AlarmReceiverActivity;


public class ReminderCastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // opening a new activity
        Intent activity = new Intent(context, AlarmReceiverActivity.class);
        activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.putExtra("reminder_id", intent.getLongExtra("reminder_id", -1));
        //flags to turn screen on


        //
        context.startActivity(activity);

    }

}//



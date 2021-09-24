package com.medical.diabetes.database;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.fragments.glecemia_level;
import com.medical.diabetes.fragments.infos;
import com.medical.diabetes.fragments.utils.Type_FragmentType;
import com.medical.diabetes.models.infosLine.HeaderInfos;
import com.medical.diabetes.models.infosLine.InfosModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class PrepareEmailThenSendItTask extends AsyncTask<Void, Void, Void> {
    private ArrayList<HeaderInfos> infos;
    //
    private String doctorEmail = "";
    //
    private File newRapport = null;
    //
    private String email_infos = "";
    //
    private String email_content = "";
    //
    private boolean neededinfosComplete;
    private int remaining_days = -1;

    public PrepareEmailThenSendItTask() {
        infos = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        //Read needed infos from infos table
        if (!(neededinfosComplete = readNeededInfos())) return null;

        //Read glecemias to send them in a report to the doctor
        if ((remaining_days = readGlecemiasAndturnThemIntoHtmlCode()) != 0) return null;
        //
        writeRapportFile();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //
        if (!neededinfosComplete) {
            Toast.makeText(MainActivity.mainContext, "Vous devez remplir tout champ obligatoire", Toast.LENGTH_LONG).show();
            return;
        }
        //
        if (remaining_days > 0) {
            Toast.makeText(MainActivity.mainContext, "Il vous reste " +
                    remaining_days + " jours pour envoyer le rapport", Toast.LENGTH_LONG).show();
            return;
        }
        if (remaining_days == -1) {
            Toast.makeText(MainActivity.mainContext, "le nombre de jours doit etre compris entre 1 et 120", Toast.LENGTH_LONG).show();
            return;
        }
        //
        if (newRapport == null) {
            Toast.makeText(MainActivity.mainContext, "Erreur! lors l'écriture du nouveau rapport", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!doctorEmail.contains("@") && !doctorEmail.contains(".") || doctorEmail.length() < 10) {
            Toast.makeText(MainActivity.mainContext, "Email du docteur faux!", Toast.LENGTH_SHORT).show();
            MainActivity.setFragmentIntoScreen(new infos());
            return;
        }
        //
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Rapport GlyceMe");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{doctorEmail});
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(newRapport));
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(email_infos));
        MainActivity.mainContext.startActivity(Intent.createChooser(intent, "Envoyer le rapport par E-mail:"));

    }

    public boolean readNeededInfos() {
        Cursor cursor = MainActivity.readableDB.query(DataBaseTablesSchemas.InfosTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        // reading infos line per line
        while (cursor.moveToNext()) {
            //
            HeaderInfos infoLine;
            //
            int header = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.InfosTable.COLUMN_HEADER));
            String header_type = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.InfosTable.COLUMN_HEADER_TYPE));
            //
            if (header == 1) {
                //don't add headers in case i want to read infos just to send an intent
                continue;

            }
            //in case it's not a header read the must be filled elements in which the header type is malade or medecin
            int must_be_filled = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.InfosTable.COLUMN_MUST_BE_FILLED));
            //
            if (((!header_type.equals(HeaderInfos.HEADER_TYPE_MALADE)) && (!header_type.equals(HeaderInfos.HEADER_TYPE_MEDECIN)))
                    || must_be_filled == 0) {
                continue; /*to send an email just skip other data than medecin and malade*/
            }
            //
            String infoName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.InfosTable.COLUMN_INFO_NAME));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.InfosTable.COLUMN_CONTENT));
            String content_type = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.InfosTable.COLUMN_CONTENT_TYPE));
            //
            if (content.trim().isEmpty()) {/*in case the important infos are empty go and fill them*/
                MainActivity.setFragmentIntoScreen(new infos());
                return false;
            }
            //
            infoLine = new InfosModel(header, header_type, content_type, infoName, content, must_be_filled);
            infos.add(infoLine);
        }

        cursor.close();
        return true;
    }

    //
    /**/
    public int readGlecemiasAndturnThemIntoHtmlCode() {

        //
        int nbJours = 0;
        //
        for (int i = 0; i < infos.size(); i++) {

            InfosModel infosModel = (InfosModel) infos.get(i);
            //
            if (infosModel.infosName.equals("E-mail")) {
                doctorEmail = infosModel.content;
                continue;
            }
            //
            if (infosModel.infosName.equals("Envoyer un rapport chaque x jours")) {
                nbJours = Integer.valueOf(infosModel.content);
                if (nbJours < 1 || nbJours > 120) {
                    MainActivity.setFragmentIntoScreen(new infos());
                    return -1;
                }

            }
            //
            if (infosModel.headerType.equals(InfosModel.HEADER_TYPE_MALADE)) {
                email_infos += "" + infosModel.infosName + ": " + infosModel.content + "<br>";
            }

        }

        /**/
        email_content = "<!DOCTYPE html> + <html><head><title>rapport diabetes  " + email_infos + "</title>" +
                "<style>" +
                "table,h4{margin-left:5px;margin-right:5px;}" +
                "h1{color:#2196F3;font-size:60px;text-align:center;}" + /*to center the table in screen*/
                "table,th,td{border: 1px solid #2196F3; border-collapse:collapse;text-align:left;}" +
                "th,td{padding:15px;}" +
                "pre{font-size:20px;margin-left:5px}" +
                "hr{background-color: #2196F3;}" +
                "tr:nth-child(even){background-color: #2196F3; color:#ffffff;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1>Rapport GlyceMe</h1><hr>" +
                "<pre><br>" +
                email_infos.replace('é', 'e') +
                "<br></pre>" +
                "" +
                "" +
                "" +
                "<Table style=\"width:100%\">" +
                "<tr>" + "<th>Date</th>" + "<th>Temps</th>" + "<th>taux avant repas (g/l)</th>" + "<th>taux apres repas (g/l)</th>" + "<th>Dose d'insuline (ul)</th>" +
                "</tr> ";
        /*read glecemia mesures*/

        Cursor cursor = MainActivity.readableDB.query(DataBaseTablesSchemas.GlecemiaMesuresTable.TABLE_NAME,
                null,
                 DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_SENT_TO_DOCTOR + " = 0",
                null,
                null,
                null,
                null);

        //
        int headersNumb = 0;
        int header;
        while (cursor.moveToNext()) {
            header = cursor.getInt(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_HEADER));

            //
            if (header == 1) {
                headersNumb++;
                continue;
            }
            email_content += "<tr>" +
                    "<td>" + cursor.getString(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_DATE_TIME)) + "</td>" +
                    "<td>" + cursor.getString(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_TIME)) + "</td>" +
                    "<td>" + cursor.getDouble(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_TAUX_APRES_REPAS)) + "</td>" +
                    "<td>" + cursor.getDouble(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_TAUX_APRES_REPAS)) + "</td>" +
                    "<td>" + cursor.getDouble(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_DOZE_INSULINE_INJECTEE)) + "</td>"
                    + "</tr>";
            ContentValues value = new ContentValues();
            value.put(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_SENT_TO_DOCTOR, 1);
            MainActivity.writableDB.update(DataBaseTablesSchemas.GlecemiaMesuresTable.TABLE_NAME, value,
                    "_id = " + cursor.getInt(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable._ID)), null);
        }
        email_content += "</Table><br><hr><h4> Envoyé de GlyceMe<h4></body></html>";
        return (nbJours - headersNumb);
    }

    /*to write a new file containing the report in the external storage*/
    public boolean writeRapportFile() {
        try {
            if (!(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)))
                throw new Exception("external storage not available for writing");

            /*writing the html code into the file*/
            File primaryStorage = ContextCompat.getExternalFilesDirs(MainActivity.mainContext, null)[0];
            File dir = new File(primaryStorage.getAbsolutePath(), "/rapports/");
            if (!dir.exists()) dir.mkdir();
            newRapport = new File(dir.getAbsolutePath(), "rapport(" + glecemia_level.todayDate() + ").html");
            FileOutputStream fileOutputStream = new FileOutputStream(newRapport);
            fileOutputStream.write(email_content.getBytes());
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}//

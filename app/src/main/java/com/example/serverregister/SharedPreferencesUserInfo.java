package com.example.serverregister;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUserInfo {
    private static final String DEF_VALUE_STRING = "Неизвестно";
    private  SharedPreferences settings ;

    public  static  String prefsFile= "profile";
    private static String prefName = "name";
    private static String prefSurname = "surname";
    private static String prefPatronymic = "patronymic";
    private static String prefEmail = "email";
    private static String prefPassword = "password";

    public String getPrefFile() {
        return prefsFile;
    }

    public SharedPreferences getSettings (Context context){
        settings = context.getSharedPreferences(prefsFile,Context.MODE_PRIVATE);
      return  settings;
    }

    public void setSettings (Context context, String userName, String userSurname, String userPatronymic, String userEmail,String userPassword){
        settings = getSettings(context);
        SharedPreferences.Editor prefEditor = settings.edit();

        prefEditor.putString(prefName, userName);
        prefEditor.putString(prefSurname, userSurname);
        prefEditor.putString(prefPatronymic, userPatronymic);
        prefEditor.putString(prefEmail, userEmail);
        prefEditor.putString(prefPassword, userPassword);

        prefEditor.commit();
    }

    public String getPrefName(SharedPreferences settings) {
      return settings.getString(prefName, DEF_VALUE_STRING);
    }

    public static void setPrefName(String prefName) {
        SharedPreferencesUserInfo.prefName = prefName;
    }

    public String getPrefSurname() {
        return settings.getString(prefSurname, DEF_VALUE_STRING);
    }

    public static void setPrefSurname(String prefSurname) {
        SharedPreferencesUserInfo.prefSurname = prefSurname;
    }

    public String getPrefPatronymic() {
        return settings.getString(prefPatronymic, DEF_VALUE_STRING);
    }

    public static void setPrefPatronymic(String prefPatronymic) {
        SharedPreferencesUserInfo.prefPatronymic = prefPatronymic;
    }

    public String getPrefEmail() {
        return settings.getString(prefEmail, DEF_VALUE_STRING);
    }

    public static void setPrefEmail(String prefEmail) {
        SharedPreferencesUserInfo.prefEmail = prefEmail;
    }

    public String getPrefPassword() {
        return settings.getString(prefPassword, DEF_VALUE_STRING);
    }

    public static void setPrefPassword(String prefPassword) {
        SharedPreferencesUserInfo.prefPassword = prefPassword;
    }
}

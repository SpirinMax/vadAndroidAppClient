package com.example.serverregister;

import android.content.Context;
import android.content.SharedPreferences;

import entites.User;

public class SharedPreferencesUserInfo {
    private static final String DEF_VALUE_STRING = "Неизвестно";
    private  SharedPreferences settings ;

    public  static final String PREF_FILE = "profile";
    private static final String PREF_NAME = "name";
    private static final String PREF_SURNAME = "surname";
    private static final String PREF_PATRONYMIC = "patronymic";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";

    public String getPrefFile() {
        return PREF_FILE;
    }

    private SharedPreferences getSettings (Context context){
        settings = context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
      return  settings;
    }

    public void setSettings (Context context, String userName, String userSurname, String userPatronymic, String userEmail,String userPassword){
        settings = getSettings(context);
        SharedPreferences.Editor prefEditor = settings.edit();

        prefEditor.putString(PREF_NAME, userName);
        prefEditor.putString(PREF_SURNAME, userSurname);
        prefEditor.putString(PREF_PATRONYMIC, userPatronymic);
        prefEditor.putString(PREF_EMAIL, userEmail);
        prefEditor.putString(PREF_PASSWORD, userPassword);

        prefEditor.commit();
    }

    public User getSavedSettings(Context context){
        User user = new User();
        settings = getSettings(context);
        String firstname = getPrefSurname(settings);
        String lastname = getPrefName(settings);
        String patronymic = getPrefPatronymic(settings);
        String email = getPrefEmail(settings);
        String password = getPrefPassword(settings);

        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPatronymic(patronymic);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }

    public boolean checkPresenceSettings(Context context){
        settings = getSettings(context);
        String email = getPrefEmail(settings);
        String password = getPrefPassword(settings);
        if (password != DEF_VALUE_STRING || email != DEF_VALUE_STRING){
            return true;
        } else {
            return false;
        }
    }

    public String getPrefName(SharedPreferences settings) {
      return settings.getString(PREF_NAME, DEF_VALUE_STRING);
    }

    public String getPrefSurname(SharedPreferences settings) {
        return this.settings.getString(PREF_SURNAME, DEF_VALUE_STRING);
    }

    public String getPrefPatronymic(SharedPreferences settings) {
        return this.settings.getString(PREF_PATRONYMIC, DEF_VALUE_STRING);
    }

    public String getPrefEmail(SharedPreferences settings) {
        return this.settings.getString(PREF_EMAIL, DEF_VALUE_STRING);
    }

    public String getPrefPassword(SharedPreferences settings) {
        return this.settings.getString(PREF_PASSWORD, DEF_VALUE_STRING);
    }

}

package com.example.serverregister;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentManager;

import ui.registration.RegisterDialogFragment;

public class BehaviorActivity {
    private SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    private RegisterDialogFragment registerDialogFragment = new RegisterDialogFragment();

    private Context context;
    private FragmentManager fragmentManager;

    public void identifyContext(Context context) {
        this.context = context;
    }

    public Context receiveContext(){
        return context;
    }

    public void identifyFragmentManager (FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    public FragmentManager receiveFragmentManager(){
        return fragmentManager;
    }

    public void goInRegisterActivity(){
        goInActivity(MainActivity.class);
    }

    public void goInActivity(Class<?> finishActivity){
        Context context = receiveContext();
        Intent intent = new Intent(context, finishActivity);
        context.startActivity(intent);
    }

    public void goActivityAfterCheck (Class<?> finishActivity){
        Context context = receiveContext();
        FragmentManager fragmentManager = receiveFragmentManager();
        if (sharedPreferencesUserInfo.checkPresenceSettings(context)){
            goInActivity(finishActivity);
        } else {
            registerDialogFragment.show(receiveFragmentManager(),"needRegister");
        }
    }

    public void exitTheApp() {
        System.exit(0);
    }

//    protected Context thisContext = this;
//
//    public void goInRegisterActivity(){
//        goInActivity(AuthenticationActivity.class);
//    }
//
//    public void goInActivity(Class<?> finishActivity){
//        Intent intent = new Intent(thisContext, finishActivity);
//        thisContext.startActivity(intent);
//    }
//
//    public void goActivityAfterCheck (Class<?> finishActivity){
//        if (sharedPreferencesUserInfo.checkPresenceSettings(thisContext)){
//            goInActivity(finishActivity);
//        } else {
//            registerDialogFragment.show(getSupportFragmentManager(),"needRegister");
//        }
//    }
//
//    public void exitTheApp() {
//        System.exit(0);
//    }
//
//    public void refreshActivity(){
//        this.finish();
//        startActivity(getIntent());
//    }
//
//     public void identifyFaceActiveIconToolbar(Class<?> page){
//         RelativeLayout containerIcon = null;
//         ImageButton ImageButton = null;
//         TextView IconTextView = null;
//
//         Resources resources = getResources();
//         if (page.equals(ProfileActivity.class)){
//             containerIcon = findViewById(R.id.profileButton);
//             ImageButton = findViewById(R.id.profile);
//             IconTextView = findViewById(R.id.profileTextview);
//             ImageButton.setEnabled(false);
//             IconTextView.setEnabled(false);
//         }
//
//         ImageButton.setBackgroundColor(resources.getColor(R.color.activeIcon));
//         containerIcon.setBackgroundColor(resources.getColor(R.color.activeIcon));
//         IconTextView.setTextColor(resources.getColor(R.color.red));
//     }

}
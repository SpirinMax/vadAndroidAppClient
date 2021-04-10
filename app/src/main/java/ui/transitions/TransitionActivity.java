package ui.transitions;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentManager;

import com.example.serverregister.AuthenticationActivity;
import com.example.serverregister.SharedPreferencesUserInfo;

import ui.registration.RegisterDialogFragment;

public class TransitionActivity {
   private SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
   private RegisterDialogFragment registerDialogFragment = new RegisterDialogFragment();
   private static final  Class<?> AUTH_ACTIVITY = AuthenticationActivity.class;

   public void goInActivity(Context sourceActivity,  Class<?> finishActivity){
       Intent intent = new Intent(sourceActivity, finishActivity);
       sourceActivity.startActivity(intent);
   }

   public void goActivityAfterCheck (Context sourceActivity,Class<?> finishActivity, FragmentManager activityFragmentManager){
       if (sharedPreferencesUserInfo.checkPresenceSettings(sourceActivity)){
           Intent intent = new Intent(sourceActivity, finishActivity);
           sourceActivity.startActivity(intent);
       } else {
           registerDialogFragment.show(activityFragmentManager,"needRegister");
       }
   }
}

package ui.registration;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

public class UiRegistration {
    public static boolean  checkOfNull(@NotNull LinearLayout linearLayout, Context context) {
        int count=0;
        TextInputLayout inputLayout;
        EditText editText;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            inputLayout = (TextInputLayout) linearLayout.getChildAt(i);
            editText = inputLayout.getEditText();
            if (editText.getText().length() == 0) {
                inputLayout.setError("Пустое значение");
                count++;
            }
            else {
                inputLayout.setError(null);
                inputLayout.setErrorEnabled(false);
            }

        }
        if (count==0){
            return true;
        } else {
            return false;
        }

    }
}

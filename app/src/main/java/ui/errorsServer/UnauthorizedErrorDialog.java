package ui.errorsServer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ui.registration.TransitToRegistration;

public class UnauthorizedErrorDialog extends DialogFragment {
    private TransitToRegistration action;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle("Неверный логин и пароль").
                setMessage("Если вы не зарегистрированы нажмите ЗАРЕГИСТРИРОВАТЬСЯ").
                setPositiveButton("Зарегистрироваться", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        action.moveToRegistration();
                    }
                }).
                setNegativeButton("Закрыть",null).
                create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        action = (TransitToRegistration) context ;
    }
}

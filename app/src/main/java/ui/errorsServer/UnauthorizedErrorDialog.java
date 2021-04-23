package ui.errorsServer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.serverregister.R;

import ui.registration.TransitToRegistration;

public class UnauthorizedErrorDialog extends DialogFragment {
    private TransitToRegistration action;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle(R.string.invalidLogin).
                setMessage(R.string.messageInvalidLogin).
                setPositiveButton(R.string.signInVerb, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        action.moveToRegistration();
                    }
                }).
                setNegativeButton(R.string.exitDialog,null).
                create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        action = (TransitToRegistration) context ;
    }
}

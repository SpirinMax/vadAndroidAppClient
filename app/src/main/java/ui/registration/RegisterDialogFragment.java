package ui.registration;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.serverregister.R;

public class RegisterDialogFragment extends DialogFragment {
    private TransitToRegistration action;
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder.setTitle(R.string.needAuth).
                setMessage(R.string.dialogCancel).
                setPositiveButton(R.string.signInVerb, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        action.moveToRegistration();
                    }
                }).
                setNegativeButton(R.string.cancel,null).
                create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        action = (TransitToRegistration) context ;
    }
}

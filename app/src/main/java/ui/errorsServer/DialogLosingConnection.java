package ui.errorsServer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.serverregister.R;

public class DialogLosingConnection extends DialogFragment {
    private RefreshInActivity action;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle(R.string.errorNetwork).
                setMessage(R.string.noConnection).
                setPositiveButton(R.string.refreshApp, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        action.refreshActivity();
                    }
                }).
                create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        action = (RefreshInActivity) context;
    }

}

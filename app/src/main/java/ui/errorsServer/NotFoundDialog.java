package ui.errorsServer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.serverregister.R;

public class NotFoundDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle(R.string.notFoundedLabel).
                setMessage(R.string.notFoundedMes).
                setPositiveButton(R.string.OK, null).
                create();
    }
}

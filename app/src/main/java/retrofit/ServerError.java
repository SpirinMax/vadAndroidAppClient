package retrofit;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.serverregister.R;

import ui.errorsServer.DialogLosingConnection;

public class ServerError {
    private static final int INTERNAL_SERVER_ERROR  = 500;
    private static final int UNAUTHORIZED  = 401;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;

    public void handleError (int serverCode){
        if (serverCode==INTERNAL_SERVER_ERROR){

        } else if (serverCode==UNAUTHORIZED){

        } else if (serverCode==NOT_FOUND){

        } else if (serverCode==REQUEST_TIMEOUT){

        } else {

        }
    }

    public void DisplayDialogLossConnection (Context context, FragmentManager fragmentManager) {
        DialogLosingConnection dialogLosingConnection = new DialogLosingConnection() ;
        try{
            dialogLosingConnection.show(fragmentManager, "lostConnection");
        } catch (Exception e) {
            Toast.makeText(context, R.string.errorNetwork, Toast.LENGTH_LONG).show();
        }
    }

    private void handleError500(){

    }

}

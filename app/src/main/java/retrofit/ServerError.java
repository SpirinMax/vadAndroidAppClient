package retrofit;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.serverregister.BehaviorActivity;
import com.example.serverregister.R;

import ui.errorsServer.DialogLosingConnection;
import ui.errorsServer.InternalServerErrorDialog;
import ui.errorsServer.NotFoundDialog;
import ui.errorsServer.RequestTimeoutDialog;
import ui.errorsServer.UnauthorizedErrorDialog;

public class ServerError {
    private static final int INTERNAL_SERVER_ERROR  = 500;
    private static final int UNAUTHORIZED  = 401;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;

    private static InternalServerErrorDialog internalServerErrorDialog;

    public void handleError (int serverCode, BehaviorActivity behaviorActivity){
        Context context = behaviorActivity.receiveContext();
        FragmentManager fragmentManager = behaviorActivity.receiveFragmentManager();

        if (serverCode==INTERNAL_SERVER_ERROR){
            handleError500(fragmentManager);
        } else if (serverCode==UNAUTHORIZED){
            handleError401(fragmentManager);
        } else if (serverCode==NOT_FOUND){
            handleError404(fragmentManager);
        } else if (serverCode==REQUEST_TIMEOUT){
            handleError408(fragmentManager);
        } else {
            Toast.makeText(context,"Произошла необжиданная ошибка!", Toast.LENGTH_LONG).show();
        }
    }

    public static void DisplayDialogLossConnection (Context context, FragmentManager fragmentManager) {
        DialogLosingConnection dialogLosingConnection = new DialogLosingConnection() ;
        //try, если прошел переход на другую акстивность во время timeout сервера (10 сек)
        try{
            dialogLosingConnection.show(fragmentManager, "lostConnection");
        } catch (Exception e) {
            Toast.makeText(context, R.string.errorNetwork, Toast.LENGTH_LONG).show();
        }
    }

    private void handleError500(FragmentManager fragmentManager){
        InternalServerErrorDialog internalServerErrorDialog = new InternalServerErrorDialog();
        internalServerErrorDialog.show(fragmentManager,"500");
    }

    private void handleError401 (FragmentManager fragmentManager){
        UnauthorizedErrorDialog unauthorizedErrorDialog = new UnauthorizedErrorDialog();
        unauthorizedErrorDialog.show(fragmentManager,"401");
    }

    private void handleError404 (FragmentManager fragmentManager){
        NotFoundDialog notFoundDialog = new NotFoundDialog();
        notFoundDialog.show(fragmentManager,"404");
    }

    private void handleError408 (FragmentManager fragmentManager){
        RequestTimeoutDialog requestTimeoutDialog = new RequestTimeoutDialog();
        requestTimeoutDialog.show(fragmentManager,"408");
    }

}

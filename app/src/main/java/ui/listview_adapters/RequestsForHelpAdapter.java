package ui.listview_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.serverregister.R;

import java.util.List;

import entites.RequestForHelp;
import service.UserService;

public class RequestsForHelpAdapter extends ArrayAdapter<RequestForHelp> {

    private LayoutInflater inflater;
    private int layout;
    private List<RequestForHelp> listRequests;

    public RequestsForHelpAdapter(@NonNull Context context, int resource, @NonNull List<RequestForHelp> listRequests) {
        super(context, resource, listRequests);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.listRequests = listRequests;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(this.layout, parent, false);
        TextView nameRequest = view.findViewById(R.id.nameRequest);
        TextView typeRequest = view.findViewById(R.id.typeRequest);
        TextView cityInRequest = view.findViewById(R.id.cityInRequest);
        TextView streetInRequest = view.findViewById(R.id.streetInRequest);
        TextView houseInRequest = view.findViewById(R.id.houseInRequest);
        TextView dateAndTimeRequest = view.findViewById(R.id.dateAndTimeRequest);
        TextView participantsRequest = view.findViewById(R.id.participantsRequest);

        RequestForHelp markedRequests = listRequests.get(position);
        nameRequest.setText(markedRequests.getName());
        typeRequest.setText(markedRequests.getType());
        cityInRequest.setText(markedRequests.getCity());
        streetInRequest.setText(markedRequests.getStreet());
        houseInRequest.setText(markedRequests.getHouseNumber());
        dateAndTimeRequest.setText(UserService.receiveStringDateTime(markedRequests.getStartDate()));
        participantsRequest.setText(UserService.receiveCountParticipants(markedRequests.getParticipants()));
        return view;
    }


}

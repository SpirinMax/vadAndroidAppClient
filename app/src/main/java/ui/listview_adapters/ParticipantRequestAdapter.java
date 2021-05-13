package ui.listview_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.serverregister.R;

import java.util.List;

public class ParticipantRequestAdapter extends ArrayAdapter<ParticipantRequest> {

    private LayoutInflater inflater;
    private int layout;
    private List<ParticipantRequest> listParticipants;

    public ParticipantRequestAdapter(@NonNull Context context, int resource, @NonNull List<ParticipantRequest> listParticipants) {
        super(context, resource, listParticipants);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.listParticipants = listParticipants;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(this.layout, parent, false);
        ImageView imagePart = view.findViewById(R.id.imagePart);
        TextView surnamePart = view.findViewById(R.id.surnamePart);
        TextView namePart = view.findViewById(R.id.namePart);
        ParticipantRequest markedParticipantRequest = listParticipants.get(position);
        imagePart.setImageBitmap(markedParticipantRequest.getImageParticipant());
        surnamePart.setText(markedParticipantRequest.getSurname());
        namePart.setText(markedParticipantRequest.getName());
        return view;
    }
}

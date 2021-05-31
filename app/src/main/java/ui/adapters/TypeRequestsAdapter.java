package ui.adapters;

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

import entites.TypeRequests;

public class TypeRequestsAdapter extends ArrayAdapter<TypeRequests> {

    private LayoutInflater inflater;
    private int layout;
    private List<TypeRequests> typeRequests;

    public TypeRequestsAdapter(@NonNull Context context, int resource, @NonNull List<TypeRequests> typeRequests) {
        super(context, resource, typeRequests);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.typeRequests = typeRequests;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(this.layout, parent, false);
        ImageView iconTypeRequest = view.findViewById(R.id.iconTypeRequest);
        TextView nameTypeRequest = view.findViewById(R.id.nameTypeRequest);
        ImageView iconCkecked = view.findViewById(R.id.iconCkecked);
        TypeRequests markedTypeRequests = typeRequests.get(position);
        iconTypeRequest.setImageResource(markedTypeRequests.getIconResourse());
        nameTypeRequest.setText(markedTypeRequests.getName());
        iconCkecked.setImageResource(markedTypeRequests.getIconCheck());
        return view;
    }
}

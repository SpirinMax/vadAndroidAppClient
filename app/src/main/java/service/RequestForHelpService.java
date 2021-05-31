package service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.serverregister.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import entites.PhotoReport;
import entites.RequestForHelp;
import entites.User;
import ui.adapters.ParticipantRequest;
import ui.adapters.ParticipantRequestAdapter;

public class RequestForHelpService {
    RequestForHelp requestForHelp;

    public RequestForHelpService(RequestForHelp requestForHelp) {
        this.requestForHelp = requestForHelp;
    }

    public static int receiveCountParticipants(Set<User> participants) {
        int count = participants.size();
        return count;
    }

    public void fillTextFields(View view, ListView listParticipants){
        TextView nameRequest, cityInRequest, streetInRequest, houseInRequest, dateAndTimeRequest, descriptionRequest, countParticipants;
        nameRequest = view.findViewById(R.id.nameRequest);
        cityInRequest = view.findViewById(R.id.cityInRequest);
        streetInRequest = view.findViewById(R.id.streetInRequest);
        houseInRequest = view.findViewById(R.id.houseInRequest);
        dateAndTimeRequest = view.findViewById(R.id.dateAndTimeRequest);
        descriptionRequest = view.findViewById(R.id.descriptionRequest);
        countParticipants = view.findViewById(R.id.countParticipants);

        nameRequest.setText(requestForHelp.getName());
        cityInRequest.setText(requestForHelp.getCity() + ",");
        streetInRequest.setText(requestForHelp.getStreet() + ",");
        houseInRequest.setText(requestForHelp.getHouseNumber());
        dateAndTimeRequest.setText(UserService.receiveStringDateTime(requestForHelp.getStartDate()));
        descriptionRequest.setText(requestForHelp.getDescription());
        countParticipants.setText("(" + receiveCountParticipants(requestForHelp.getParticipants()) + " чел.)");
        fillListViewParticipants(view,requestForHelp.getParticipants(),listParticipants);
    }

    public void fillShortTextFields(View view){
        TextView nameRequest, cityInRequest, streetInRequest, houseInRequest, dateAndTimeRequest, descriptionRequest, countParticipants;
        nameRequest = view.findViewById(R.id.nameRequest);
        cityInRequest = view.findViewById(R.id.cityInRequest);
        streetInRequest = view.findViewById(R.id.streetInRequest);
        houseInRequest = view.findViewById(R.id.houseInRequest);
        dateAndTimeRequest = view.findViewById(R.id.dateAndTimeRequest);
        countParticipants = view.findViewById(R.id.participantsRequest);

        nameRequest.setText(requestForHelp.getName());
        cityInRequest.setText(requestForHelp.getCity() + ",");
        streetInRequest.setText(requestForHelp.getStreet() + ",");
        houseInRequest.setText(requestForHelp.getHouseNumber());
        dateAndTimeRequest.setText(UserService.receiveStringDateTime(requestForHelp.getStartDate()));
        countParticipants.setText("(" + receiveCountParticipants(requestForHelp.getParticipants()) + " чел.)");
    }

    private void fillListViewParticipants(View view, Set<User> participants, ListView listParticipants) {
        Context context = view.getContext();
        if (participants.size() != 0) {
            List<User> listPartFromRequest = new ArrayList<User>(participants);
            List<ParticipantRequest> listPart = new ArrayList<ParticipantRequest>();
            for (int i = 0; i < listPartFromRequest.size(); i++) {
                Bitmap imagePart = BitmapFactory.decodeResource(view.getResources(), R.drawable.empty_image);
                if (listPartFromRequest.get(i).getPhoto() != null) {
                    byte[] photo = listPartFromRequest.get(i).getPhoto();
                    imagePart = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                }
                String surname = listPartFromRequest.get(i).getFirstname();
                String name = listPartFromRequest.get(i).getLastname();
                ParticipantRequest participant = new ParticipantRequest(imagePart, name, surname);
                listPart.add(participant);
            }
            ParticipantRequestAdapter participantRequestAdapter =
                    new ParticipantRequestAdapter(context, R.layout.participants_listview_layout, listPart);
            listParticipants.setAdapter(participantRequestAdapter);
        } else {
            List<ParticipantRequest> listPart = new ArrayList<ParticipantRequest>();
            ParticipantRequestAdapter participantRequestAdapter =
                    new ParticipantRequestAdapter(context, R.layout.participants_listview_layout, listPart);
            listParticipants.setAdapter(participantRequestAdapter);
        }
    }

    public Boolean checkOwnerInAuthorRequest(User owner) {
        int idOwner = owner.getId();
        int idAuthorRequest = requestForHelp.getAuthorUser().getId();
        if (idOwner == idAuthorRequest) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkOwnerInListParticipants(User owner) {
        int idOwner = owner.getId();
        List<User> participants = new ArrayList<>(requestForHelp.getParticipants());
        byte count = 0;
        for (byte i = 0; i < participants.size(); i++) {
            int idPart = participants.get(i).getId();
            if (idOwner == idPart) {
                break;
            } else {
                count++;
            }
        }
        if (count == participants.size()) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkOwnerInAuthorsPhotoReports (int idOwner, Set<PhotoReport> photoReports){
        List<PhotoReport> listPhotoReports = new ArrayList<PhotoReport>(photoReports);
        int countPhotoReports = listPhotoReports.size();
        Boolean flag = false;
        for (int i = 0; i < countPhotoReports; i++) {
            PhotoReport currentPhotoReport = listPhotoReports.get(i);
            int idAuthorRequest = currentPhotoReport.getAuthorReport().getId();
            if (idOwner == idAuthorRequest){
                flag = true;
                break;
            }
        }
        return flag;
    }
}

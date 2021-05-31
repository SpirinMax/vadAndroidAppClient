package ui.adapters;

import android.graphics.Bitmap;

public class ParticipantRequest {
    private Bitmap imageParticipant;
    private String name;
    private String surname;

    public ParticipantRequest(Bitmap imageParticipant, String name, String surname) {
        this.imageParticipant = imageParticipant;
        this.name = name;
        this.surname = surname;
    }

    public Bitmap getImageParticipant() {
        return imageParticipant;
    }

    public void setImageParticipant(Bitmap imageParticipant) {
        this.imageParticipant = imageParticipant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}

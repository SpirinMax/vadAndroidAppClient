package entites;

import java.io.Serializable;

public class ImagePhotoReport implements Serializable {
    private int id;
    private int idPhotoReport;
    private byte[] image;

    public ImagePhotoReport(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public ImagePhotoReport() {
    }

    public int getIdPhotoReport() {
        return idPhotoReport;
    }

    public void setIdPhotoReport(int idPhotoReport) {
        this.idPhotoReport = idPhotoReport;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

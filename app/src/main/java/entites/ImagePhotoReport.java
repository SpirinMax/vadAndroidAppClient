package entites;

public class ImagePhotoReport {
    private int numberPhoto;
    private int idPhotoReport;
    private byte[] image;

    public int getNumberPhoto() {
        return numberPhoto;
    }

    public void setNumberPhoto(int numberPhoto) {
        this.numberPhoto = numberPhoto;
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

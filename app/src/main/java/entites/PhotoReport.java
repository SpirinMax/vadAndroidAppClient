package entites;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class PhotoReport {
    private int id;
    private User authorReport;
    private String name;
    private Set<ImagePhotoReport> images = new HashSet<ImagePhotoReport>();
    private Calendar creationDate;

    public int getId() {
        return id;
    }

    public User getAuthorReport() {
        return authorReport;
    }

    public void setAuthorReport(User authorReport) {
        this.authorReport = authorReport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ImagePhotoReport> getImages() {
        return images;
    }

    public void setImages(Set<ImagePhotoReport> images) {
        this.images = images;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }
}

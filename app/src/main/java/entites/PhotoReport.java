package entites;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class PhotoReport implements Serializable {
    private int id;
    private User authorReport;
    private String name;
    private String description;
    private Set<ImagePhotoReport> images;
    private Calendar creationDate;

    public PhotoReport(Set<ImagePhotoReport> images) {
        this.images = images;
    }

    public PhotoReport() {

    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

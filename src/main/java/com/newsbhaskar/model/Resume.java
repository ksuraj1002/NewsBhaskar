package com.newsbhaskar.model;




import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Transient
    private MultipartFile resumeFile;
    @Transient
    private MultipartFile  profilephotoFile;

    private String resume;
    private String profilephoto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getProfilephoto() {
        return profilephoto;
    }

    public void setProfilephoto(String profilephoto) {
        this.profilephoto = profilephoto;
    }

    public MultipartFile getResumeFile() {
        return resumeFile;
    }

    public void setResumeFile(MultipartFile resumeFile) {
        this.resumeFile = resumeFile;
    }

    public MultipartFile getProfilephotoFile() {
        return profilephotoFile;
    }

    public void setProfilephotoFile(MultipartFile profilephotoFile) {
        this.profilephotoFile = profilephotoFile;
    }
}

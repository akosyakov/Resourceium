package com.bitlegion.server.uploads;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bitlegion.server.accounts.Account;
import com.bitlegion.server.socials.Favorite;
import com.bitlegion.server.socials.Liked;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String description;

    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfUpload;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastEdited;

    // if the boolean value is true then the folder is private and only for the
    // user.
    private Boolean hidden;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "folder_tags", joinColumns = { @JoinColumn(name = "folder_id") }, inverseJoinColumns = {
            @JoinColumn(name = "tag_id") })
    private Set<Tag> tags;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @JsonIgnore
    @OneToMany(mappedBy = "folder")
    private Collection<File> files;

    @JsonIgnore
    @OneToMany(mappedBy = "folder")
    private Collection<Favorite> favorites;

    @JsonIgnore
    @OneToMany(mappedBy = "folder")
    private Collection<Liked> liked;

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Collection<Favorite> getFavorites() {
        return this.favorites;
    }

    public void setFavorites(Collection<Favorite> favorites) {
        this.favorites = favorites;
    }

    public Collection<Liked> getLiked() {
        return this.liked;
    }

    public void setLiked(Collection<Liked> liked) {
        this.liked = liked;
    }

    public Integer getId() {
        return this.id;
    }

    public Date getLastEdited() {
        return this.lastEdited;
    }

    public Collection<File> getFiles() {
        return this.files;
    }

    public void setFiles(Collection<File> files) {
        this.files = files;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateOfUpload() {
        return this.dateOfUpload;
    }

    public void setDateOfUpload(Date dateOfUpload) {
        this.dateOfUpload = dateOfUpload;
    }

    public Boolean isHidden() {
        return this.hidden;
    }

    public Boolean getHidden() {
        return this.hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", description='" + getDescription() + "'" + ", title='" + getTitle()
                + "'" + ", dateOfUpload='" + getDateOfUpload() + "'" + ", lastEdited='" + getLastEdited() + "'"
                + ", hidden='" + isHidden() + "'" + ", tags='" + getTags() + "'" + ", account='" + getAccount() + "'"
                + ", files='" + getFiles() + "'" + ", favorites='" + getFavorites() + "'" + ", liked='" + getLiked()
                + "'" + "}";
    }

}

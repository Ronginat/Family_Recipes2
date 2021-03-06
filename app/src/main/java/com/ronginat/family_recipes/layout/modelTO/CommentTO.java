package com.ronginat.family_recipes.layout.modelTO;

import com.google.gson.annotations.SerializedName;
import com.ronginat.family_recipes.model.CommentEntity;

import java.util.Objects;

import androidx.annotation.NonNull;

/**
 * Created by ronginat on 14/01/2019.
 */
public class CommentTO {
    @SerializedName("message")
    private String message;
    @SerializedName("user")
    private String username;
    @SerializedName("creationDate")
    private String creationDate;

    public CommentTO() {
        super();
    }

    /*public CommentTO(CommentEntity comment) {
        this();
        if (comment != null) {
            this.message = comment.getMessage();
            this.user = comment.getUser();
            this.creationDate = comment.getDate();
        }
    }*/

    public CommentEntity toEntity() {
        CommentEntity entity = new CommentEntity();
        entity.setMessage(getMessage());
        entity.setUser(getUsername());
        entity.setDate(getCreationDate());
        return entity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @NonNull
    @Override
    public String toString() {
        if (message == null || username == null || creationDate == null)
            return "null";
        return "CommentTO{" +
                "message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", creationDate='" + creationDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentTO comment = (CommentTO) o;
        return this.message.equals(comment.message) &&
                this.username.equals(comment.username) &&
                this.creationDate.equals(comment.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, username, creationDate);
    }
}

package com.music.webservice.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id")
    private Long songId;

    @Column(name = "song_name")
    private String songName;

    @Column(name = "link")
    private String link;

    @Column(name = "image")
    private String image;

    @Column(name = "views")
    private long views;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

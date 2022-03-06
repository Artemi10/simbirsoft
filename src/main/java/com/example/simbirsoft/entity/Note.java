package com.example.simbirsoft.entity;

import com.example.simbirsoft.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "notes")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "text")
    private String text;
    @Column(name = "creation_time")
    private Timestamp creationTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}

package com.example.simbirsoft.entity;

import com.example.simbirsoft.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "applications")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class App {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "creation_time")
    private Timestamp creationTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "app", fetch = FetchType.LAZY)
    private List<Event> events;
}

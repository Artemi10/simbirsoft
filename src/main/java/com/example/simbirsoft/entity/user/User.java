package com.example.simbirsoft.entity.user;

import com.example.simbirsoft.entity.Application;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;
    @Column(name = "reset_token")
    private String resetToken;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Application> applications;
}

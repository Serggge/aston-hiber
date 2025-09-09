package ru.serggge.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.serggge.annotations.UnitName;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "users")
@UnitName(name = "user-persistence")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private int age;
    @Column(name = "created_at")
    private Instant createdAt;
}
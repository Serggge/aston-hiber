package ru.serggge.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;
    private String name;
    private String email;
    private Integer age;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "active", insertable = false)
    @Setter
    private Boolean isActive;

    public User(@NonNull String name, @NonNull String email, @NonNull Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
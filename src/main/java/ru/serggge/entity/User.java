package ru.serggge.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;
import ru.serggge.annotations.UnitName;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "USERS")
@UnitName(name = "user-persistence")
@Getter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private Integer age;
    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    @Generated
    private Instant createdAt;
    @Column(name = "ACTIVE", insertable = false)
    @Generated
    @Setter
    private Boolean isActive;
}
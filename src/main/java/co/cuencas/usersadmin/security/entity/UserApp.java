package co.cuencas.usersadmin.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_app")
public class UserApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotEmpty(message = "La identificación es obligatoria")
    @Column(name = "identification", unique = true, nullable = false)
    private String identification;

    // @NotEmpty(message = "La contraseña es obligatoria")
    // @Column(name = "password", nullable = false)
    // private String password;

    @NotEmpty(message = "El nombre completo es obligatorio")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @NotEmpty(message = "El correo electrónico es obligatorio")
    @Column(name = "email", nullable = false)
    private String email;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private OffsetDateTime createdDate;

    @Column(name = "organization")
    private String organization;

    /**
     * Many users have only one role.
     * A role can belong to several users.
     */
    @ManyToOne(cascade = CascadeType.DETACH,
               fetch = FetchType.EAGER)
    @JoinColumn(name = "user_role_id", nullable = false)
    private UserRole userRole;
}
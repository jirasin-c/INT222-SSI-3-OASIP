package sit.ssi3.oasip.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import sit.ssi3.oasip.Enum.RoleEnum;

import javax.persistence.*;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
//@JsonIgnoreProperties(value = "password")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID", nullable = false, updatable = false)
    private Integer id;

    @NotEmpty(message = "A name must not be empty!")
    @Size(max = 100, message = "A name must be between 1 and 100")
    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Email(message = "An email must be well-formed email address", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @NotEmpty(message = "An email address must not be empty!")
    @Size(max = 50, message = "An email address must be between 1 and 50")
    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @NotEmpty(message = "A password must not be empty!")
//    @Size(min = 8, max = 14,message = "A password must be between 8 and 14")
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleEnum role;

    @Column(name = "createdOn", insertable = false, updatable = false, nullable = false)
    private Timestamp createdOn;

    @Column(name = "updatedOn", insertable = false, updatable = false, nullable = false)
    private Timestamp updatedOn;

    @Transient
    @AssertFalse(message = "Email must be unique")
    private boolean isEmailUnique;

    @Transient
    @AssertFalse(message = "Name must be unique")
    private boolean isNameUnique;

    @Transient
    @AssertFalse(message = "Role must be admin, lecturer, or student")
    private boolean isRoleSpecified;

    @Transient
    @AssertFalse(message = "A password must be between 8 and 14")
    private boolean isPasswordFit;
    
}
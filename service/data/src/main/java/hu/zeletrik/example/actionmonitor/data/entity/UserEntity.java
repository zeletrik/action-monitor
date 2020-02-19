package hu.zeletrik.example.actionmonitor.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "am_user")
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long user_id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Many to Many Example - see Role.
     * <p>
     * One User many have many Roles.
     * Each Role may be assigned to many Users.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value= FetchMode.SELECT)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserEntity that = (UserEntity) o;

        return new EqualsBuilder()
                .append(user_id, that.user_id)
                .append(username, that.username)
                .append(password, that.password)
                .append(roles, that.roles)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(user_id)
                .append(username)
                .append(password)
                .toHashCode();
    }
}

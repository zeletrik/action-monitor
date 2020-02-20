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
@Table(name = "role")
@NoArgsConstructor
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id", updatable = false, nullable = false)
    private Long role_id;

    @Column(name = "role", nullable = false)
    private String role;

    /**
     * Many to Many Example - see Role.
     * <p>
     * One User many have many Roles.
     * Each Role may be assigned to many Users.
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    @Fetch(value= FetchMode.SELECT)
    private Set<UserEntity> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoleEntity that = (RoleEntity) o;

        return new EqualsBuilder()
                .append(role_id, that.role_id)
                .append(role, that.role)
                .append(users, that.users)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(role_id)
                .append(role)
                .toHashCode();
    }
}

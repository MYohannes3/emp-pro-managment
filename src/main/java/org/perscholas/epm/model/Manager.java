package org.perscholas.epm.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "managers")
@Getter
@Setter
@AllArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Manager {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "manager_id", nullable = false)
     int managerId;

    @Column(name = "first_name", nullable = false)
     String firstName;
    @Column(name = "last_name", nullable = false)
     String lastName;
    @Column(name = "summary", nullable = false)
     String summary;

//  manager can manage many projects
    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    Set<Project> projects = new HashSet<>();

// manager is a user
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="user_id", referencedColumnName = "user_id", nullable = false)
    User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return managerId == manager.managerId && firstName.equals(manager.firstName) && lastName.equals(manager.lastName) && summary.equals(manager.summary);
    }

    @Override
    public int hashCode() {

        return Objects.hash(managerId, firstName, lastName, summary);
    }

    // Generate the Constructors and tostring

    @Override
    public String toString() {
        return "Manager{" +
                "managerId=" + managerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }

    public Manager() {
    }

    public Manager(String firstName, String lastName, String summary, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.summary = summary;
        this.user = user;
    }
}

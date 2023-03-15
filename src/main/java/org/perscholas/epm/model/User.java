package org.perscholas.epm.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Getter
@Setter
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    int userId;

    @Column(name = "email", nullable = false)
    String email;
    @Column(name = "password", nullable = false)
    String password;

    //Each user has a role

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_role",
    joinColumns = {@JoinColumn(name = "user_id")},
    inverseJoinColumns = {@JoinColumn(name ="role_id")})
     Set<Role> roles = new HashSet<>();

    // also each users can be an employee or manager
    @OneToOne(mappedBy = "user")
    Employee employee;

    @OneToOne(mappedBy = "user" )
    Manager manager;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && email.equals(user.email) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email, password);
    }

    // lets add Helper methods like assigning and remove roles to users

    public void assignRoleToUser(Role role){
        this.roles.add(role);
       role.getUsers().add(this);
   }

    public void removeRoleFromUser(Role role){
        this.roles.remove(role);
       role.getUsers().remove(this);
   }

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                '}';
    }
}

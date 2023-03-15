package org.perscholas.epm.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "employees")
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="employee_id", nullable = false)
    int employeeId;
    @Column(name="first_name", nullable = false)
    String firstName;

    @Column(name="last_name", nullable = false)
    String lastName;

    @Column(name="title", nullable = false)
    String title;


//  employees can work on many projects
   @ManyToMany(mappedBy = "employees", fetch = FetchType.LAZY)
    Set<Project> projects = new HashSet<>();

// employee is also a user
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="user_id", referencedColumnName = "user_id", nullable = false)
    User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return employeeId == employee.employeeId && firstName.equals(employee.firstName) && lastName.equals(employee.lastName) && title.equals(employee.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, firstName, lastName, title);
    }

    // Generate the constructors and tostring
    public Employee() {
    }

    public Employee(String firstName, String lastName, String title, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}

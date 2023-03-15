package org.perscholas.epm.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false)
    int projectId;

    @Column(name = "project_name", nullable = false)
    String projectName;
    @Column(name = "project_duration", nullable = false)
    String projectDuration;
    @Column(name = "project_description", nullable = false)
    String projectDescription;

    // a project can beint to one or many managers so we use many to one relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", referencedColumnName = "manager_id", nullable = false)
    Manager manager;

    // many projects can be assigned to many employees so the relation will be many to many
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "works_on",
            joinColumns = {@JoinColumn(name="project_id")},
            inverseJoinColumns = {@JoinColumn(name="employee_id")})
    Set<Employee> employees = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return projectId == project.projectId && projectName.equals(project.projectName) && projectDuration.equals(project.projectDuration) && projectDescription.equals(project.projectDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, projectDuration, projectDescription);
    }

    // lets add Helper methods like assigning and remove projects to employee
    public void assignEmployeeToProject(Employee employee){
        this.employees.add(employee);
        employee.getProjects().add(this);
    }

    public void removeEmployeeFromProject(Employee employee){
        this.employees.add(employee);
        employee.getProjects().remove(this);
    }

    // Generated my Constructors
    public Project() {
    }

    public Project(String projectName, String projectDuration, String projectDescription, Manager manager) {
        this.projectName = projectName;
        this.projectDuration = projectDuration;
        this.projectDescription = projectDescription;
        this.manager = manager;
    }
    // generated the tostring method which allow us to display information about project


    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", projectDuration='" + projectDuration + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                '}';
    }
}

package org.perscholas.epm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.perscholas.epm.dao.ManagerRepo;
import org.perscholas.epm.dao.ProjectRepo;
import org.perscholas.epm.model.Manager;
import org.perscholas.epm.model.Project;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    ProjectRepo projectRepo;
    @Mock
    ManagerRepo managerRepo;
    @InjectMocks
    ProjectService projectService;
    @Test
    void testLoadProjectById() {
        Project project = new Project();
        project.setProjectId(1);

        when(projectRepo.findById(any())).thenReturn(Optional.of(project));
        Project project1 = projectService.loadProjectById(1);
        assertEquals(project, project1);
    }

    @Test
    void testCreateProject() {
        Manager manager = new Manager();
        manager.setManagerId(1);

        when(managerRepo.findById(any())).thenReturn(Optional.of(manager));
        projectService.createProject("automation", "5 Months", "build an app for attendance", 1);
        verify(projectRepo).save(any());
    }

    @Test
    void testFindProjectsByProjectName() {
        String projectName = "migration";
        projectService.findProjectsByProjectName(projectName);
        verify(projectRepo).findProjectsByProjectNameContains(projectName);
    }

    @Test
    void testRemoveProject() {
        projectService.removeProject(1);
        verify(projectRepo).deleteById(1);
    }
}
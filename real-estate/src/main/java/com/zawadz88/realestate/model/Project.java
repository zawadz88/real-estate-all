package com.zawadz88.realestate.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * POJO containing real estate ad info.
 *
 * @author Piotr Zawadzki
 */
@DatabaseTable(tableName = "projects")
public class Project {

    /**
     * Project's unique identifier
     */
    @DatabaseField(id = true)
    private long projectId;

    /**
     * Title of the project
     */
    @DatabaseField
    private String title;

    /**
     * String name of the drawable resource containing the image of a given project
     */
    @DatabaseField
    private String resourceName;

    public Project() {
    }

    public Project(long projectId, String title, String resourceName) {
        this.projectId = projectId;
        this.title = title;
        this.resourceName = resourceName;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}

package com.mihael.mhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Overview.
 */
@Entity
@Table(name = "overview")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Overview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_generated")
    private ZonedDateTime dateGenerated;

    @JsonIgnoreProperties(value = { "overview", "featureTst" }, allowSetters = true)
    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
    @NotNull
    @JoinColumn(unique = true)
    private CodeStats parent;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_overview__project",
        joinColumns = @JoinColumn(name = "overview_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonIgnoreProperties(value = { "mdls", "featureTsts", "user", "features", "overviews" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Overview id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateGenerated() {
        return this.dateGenerated;
    }

    public Overview dateGenerated(ZonedDateTime dateGenerated) {
        this.setDateGenerated(dateGenerated);
        return this;
    }

    public void setDateGenerated(ZonedDateTime dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public CodeStats getParent() {
        return this.parent;
    }

    public void setParent(CodeStats codeStats) {
        this.parent = codeStats;
    }

    public Overview parent(CodeStats codeStats) {
        this.setParent(codeStats);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Overview user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Overview projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public Overview addProject(Project project) {
        this.projects.add(project);
        return this;
    }

    public Overview removeProject(Project project) {
        this.projects.remove(project);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Overview)) {
            return false;
        }
        return getId() != null && getId().equals(((Overview) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Overview{" +
            "id=" + getId() +
            ", dateGenerated='" + getDateGenerated() + "'" +
            "}";
    }

    void update() {}
}

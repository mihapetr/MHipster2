package com.mihael.mhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A MDLS.
 */
@Entity
@Table(name = "mdls")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MDLS implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "base_config")
    private String baseConfig;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @JsonIgnoreProperties(value = { "mdls", "featureTsts", "user", "features", "overviews" }, allowSetters = true)
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "mdls")
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MDLS id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaseConfig() {
        return this.baseConfig;
    }

    public MDLS baseConfig(String baseConfig) {
        this.setBaseConfig(baseConfig);
        return this;
    }

    public void setBaseConfig(String baseConfig) {
        this.baseConfig = baseConfig;
    }

    public String getContent() {
        return this.content;
    }

    public MDLS content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MDLS user(User user) {
        this.setUser(user);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        if (this.project != null) {
            this.project.setMdls(null);
        }
        if (project != null) {
            project.setMdls(this);
        }
        this.project = project;
    }

    public MDLS project(Project project) {
        this.setProject(project);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MDLS)) {
            return false;
        }
        return getId() != null && getId().equals(((MDLS) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MDLS{" +
            "id=" + getId() +
            ", baseConfig='" + getBaseConfig() + "'" +
            //", content='" + getContent() + "'" +
			",userId=" + getUser().getId() +
            "}";
    }
}

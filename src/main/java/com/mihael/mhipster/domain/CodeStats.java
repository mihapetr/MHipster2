package com.mihael.mhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A CodeStats.
 */
@Entity
@Table(name = "code_stats")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CodeStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "instructions")
    private Double instructions;

    @Column(name = "branches")
    private Double branches;

    @Column(name = "jhi_lines")
    private Double lines;

    @Column(name = "methods")
    private Double methods;

    @Column(name = "classes")
    private Double classes;

    @Column(name = "dead_instructions")
    private Double deadInstructions;

    @Column(name = "dead_branches")
    private Double deadBranches;

    @Column(name = "dead_lines")
    private Double deadLines;

    @Column(name = "dead_methods")
    private Double deadMethods;

    @Column(name = "dead_classes")
    private Double deadClasses;

    @JsonIgnoreProperties(value = { "parent", "testReports", "features", "project" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "parent")
    private FeatureTst featureTst;

    @JsonIgnoreProperties(value = { "parent", "user", "projects" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "parent")
    private Overview overview;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CodeStats id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getInstructions() {
        return this.instructions;
    }

    public CodeStats instructions(Double instructions) {
        this.setInstructions(instructions);
        return this;
    }

    public void setInstructions(Double instructions) {
        this.instructions = instructions;
    }

    public Double getBranches() {
        return this.branches;
    }

    public CodeStats branches(Double branches) {
        this.setBranches(branches);
        return this;
    }

    public void setBranches(Double branches) {
        this.branches = branches;
    }

    public Double getLines() {
        return this.lines;
    }

    public CodeStats lines(Double lines) {
        this.setLines(lines);
        return this;
    }

    public void setLines(Double lines) {
        this.lines = lines;
    }

    public Double getMethods() {
        return this.methods;
    }

    public CodeStats methods(Double methods) {
        this.setMethods(methods);
        return this;
    }

    public void setMethods(Double methods) {
        this.methods = methods;
    }

    public Double getClasses() {
        return this.classes;
    }

    public CodeStats classes(Double classes) {
        this.setClasses(classes);
        return this;
    }

    public void setClasses(Double classes) {
        this.classes = classes;
    }

    public Double getDeadInstructions() {
        return this.deadInstructions;
    }

    public CodeStats deadInstructions(Double deadInstructions) {
        this.setDeadInstructions(deadInstructions);
        return this;
    }

    public void setDeadInstructions(Double deadInstructions) {
        this.deadInstructions = deadInstructions;
    }

    public Double getDeadBranches() {
        return this.deadBranches;
    }

    public CodeStats deadBranches(Double deadBranches) {
        this.setDeadBranches(deadBranches);
        return this;
    }

    public void setDeadBranches(Double deadBranches) {
        this.deadBranches = deadBranches;
    }

    public Double getDeadLines() {
        return this.deadLines;
    }

    public CodeStats deadLines(Double deadLines) {
        this.setDeadLines(deadLines);
        return this;
    }

    public void setDeadLines(Double deadLines) {
        this.deadLines = deadLines;
    }

    public Double getDeadMethods() {
        return this.deadMethods;
    }

    public CodeStats deadMethods(Double deadMethods) {
        this.setDeadMethods(deadMethods);
        return this;
    }

    public void setDeadMethods(Double deadMethods) {
        this.deadMethods = deadMethods;
    }

    public Double getDeadClasses() {
        return this.deadClasses;
    }

    public CodeStats deadClasses(Double deadClasses) {
        this.setDeadClasses(deadClasses);
        return this;
    }

    public void setDeadClasses(Double deadClasses) {
        this.deadClasses = deadClasses;
    }

    public FeatureTst getFeatureTst() {
        return this.featureTst;
    }

    public void setFeatureTst(FeatureTst featureTst) {
        if (this.featureTst != null) {
            this.featureTst.setParent(null);
        }
        if (featureTst != null) {
            featureTst.setParent(this);
        }
        this.featureTst = featureTst;
    }

    public CodeStats featureTst(FeatureTst featureTst) {
        this.setFeatureTst(featureTst);
        return this;
    }

    public Overview getOverview() {
        return this.overview;
    }

    public void setOverview(Overview overview) {
        if (this.overview != null) {
            this.overview.setParent(null);
        }
        if (overview != null) {
            overview.setParent(this);
        }
        this.overview = overview;
    }

    public CodeStats overview(Overview overview) {
        this.setOverview(overview);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodeStats)) {
            return false;
        }
        return getId() != null && getId().equals(((CodeStats) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CodeStats{" +
            "\nid=" + getId() +
            "\n, instructions=" + getInstructions() +
            "\n, branches=" + getBranches() +
            "\n, lines=" + getLines() +
            "\n, methods=" + getMethods() +
            "\n, classes=" + getClasses() +
            "\n, deadInstructions=" + getDeadInstructions() +
            "\n, deadBranches=" + getDeadBranches() +
            "\n, deadLines=" + getDeadLines() +
            "\n, deadMethods=" + getDeadMethods() +
            "\n, deadClasses=" + getDeadClasses() +
            "}";
    }
}

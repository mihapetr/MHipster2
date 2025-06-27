package com.mihael.mhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A TestReport.
 */
@Entity
@Table(name = "test_report")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "html")
    private String html;

    @Column(name = "runtime_retention")
    private Boolean runtimeRetention;

    @Column(name = "missed_instructions")
    private Integer missedInstructions;

    @Column(name = "instructions")
    private Integer instructions;

    @Column(name = "missed_branches")
    private Integer missedBranches;

    @Column(name = "branches")
    private Integer branches;

    @Column(name = "missed_lines")
    private Integer missedLines;

    @Column(name = "jhi_lines")
    private Integer lines;

    @Column(name = "missed_methods")
    private Integer missedMethods;

    @Column(name = "methods")
    private Integer methods;

    @Column(name = "missed_classes")
    private Integer missedClasses;

    @Column(name = "classes")
    private Integer classes;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "parent", "testReports", "features", "project" }, allowSetters = true)
    private FeatureTst featureTst;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHtml() {
        return this.html;
    }

    public TestReport html(String html) {
        this.setHtml(html);
        return this;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Boolean getRuntimeRetention() {
        return this.runtimeRetention;
    }

    public TestReport runtimeRetention(Boolean runtimeRetention) {
        this.setRuntimeRetention(runtimeRetention);
        return this;
    }

    public void setRuntimeRetention(Boolean runtimeRetention) {
        this.runtimeRetention = runtimeRetention;
    }

    public Integer getMissedInstructions() {
        return this.missedInstructions;
    }

    public TestReport missedInstructions(Integer missedInstructions) {
        this.setMissedInstructions(missedInstructions);
        return this;
    }

    public void setMissedInstructions(Integer missedInstructions) {
        this.missedInstructions = missedInstructions;
    }

    public Integer getInstructions() {
        return this.instructions;
    }

    public TestReport instructions(Integer instructions) {
        this.setInstructions(instructions);
        return this;
    }

    public void setInstructions(Integer instructions) {
        this.instructions = instructions;
    }

    public Integer getMissedBranches() {
        return this.missedBranches;
    }

    public TestReport missedBranches(Integer missedBranches) {
        this.setMissedBranches(missedBranches);
        return this;
    }

    public void setMissedBranches(Integer missedBranches) {
        this.missedBranches = missedBranches;
    }

    public Integer getBranches() {
        return this.branches;
    }

    public TestReport branches(Integer branches) {
        this.setBranches(branches);
        return this;
    }

    public void setBranches(Integer branches) {
        this.branches = branches;
    }

    public Integer getMissedLines() {
        return this.missedLines;
    }

    public TestReport missedLines(Integer missedLines) {
        this.setMissedLines(missedLines);
        return this;
    }

    public void setMissedLines(Integer missedLines) {
        this.missedLines = missedLines;
    }

    public Integer getLines() {
        return this.lines;
    }

    public TestReport lines(Integer lines) {
        this.setLines(lines);
        return this;
    }

    public void setLines(Integer lines) {
        this.lines = lines;
    }

    public Integer getMissedMethods() {
        return this.missedMethods;
    }

    public TestReport missedMethods(Integer missedMethods) {
        this.setMissedMethods(missedMethods);
        return this;
    }

    public void setMissedMethods(Integer missedMethods) {
        this.missedMethods = missedMethods;
    }

    public Integer getMethods() {
        return this.methods;
    }

    public TestReport methods(Integer methods) {
        this.setMethods(methods);
        return this;
    }

    public void setMethods(Integer methods) {
        this.methods = methods;
    }

    public Integer getMissedClasses() {
        return this.missedClasses;
    }

    public TestReport missedClasses(Integer missedClasses) {
        this.setMissedClasses(missedClasses);
        return this;
    }

    public void setMissedClasses(Integer missedClasses) {
        this.missedClasses = missedClasses;
    }

    public Integer getClasses() {
        return this.classes;
    }

    public TestReport classes(Integer classes) {
        this.setClasses(classes);
        return this;
    }

    public void setClasses(Integer classes) {
        this.classes = classes;
    }

    public FeatureTst getFeatureTst() {
        return this.featureTst;
    }

    public void setFeatureTst(FeatureTst featureTst) {
        this.featureTst = featureTst;
    }

    public TestReport featureTst(FeatureTst featureTst) {
        this.setFeatureTst(featureTst);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestReport)) {
            return false;
        }
        return getId() != null && getId().equals(((TestReport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestReport{" +
            "id=" + getId() +
            ", html='" + getHtml() + "'" +
            ", runtimeRetention='" + getRuntimeRetention() + "'" +
            ", missedInstructions=" + getMissedInstructions() +
            ", instructions=" + getInstructions() +
            ", missedBranches=" + getMissedBranches() +
            ", branches=" + getBranches() +
            ", missedLines=" + getMissedLines() +
            ", lines=" + getLines() +
            ", missedMethods=" + getMissedMethods() +
            ", methods=" + getMethods() +
            ", missedClasses=" + getMissedClasses() +
            ", classes=" + getClasses() +
            "}";
    }
}

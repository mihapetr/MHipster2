package com.mihael.mhipster.service.dto;

public class CodeStatsDBDTO {

    public CodeStatsDBDTO(
        Double instructions,
        Double branches,
        Double lines,
        Double methods,
        Double classes,
        Double deadInstructions,
        Double deadBranches,
        Double deadLines,
        Double deadMethods,
        Double deadClasses
    ) {
        this.instructions = instructions;
        this.branches = branches;
        this.lines = lines;
        this.methods = methods;
        this.classes = classes;
        this.deadInstructions = deadInstructions;
        this.deadBranches = deadBranches;
        this.deadLines = deadLines;
        this.deadMethods = deadMethods;
        this.deadClasses = deadClasses;
    }

    public Double instructions;
    public Double branches;
    public Double lines;
    public Double methods;
    public Double classes;
    public Double deadInstructions;
    public Double deadBranches;
    public Double deadLines;
    public Double deadMethods;
    public Double deadClasses;
}

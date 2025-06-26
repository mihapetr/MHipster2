package com.mihael.mhipster.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TestReport getTestReportSample1() {
        return new TestReport()
            .id(1L)
            .html("html1")
            .missedInstructions(1)
            .instructions(1)
            .missedBranches(1)
            .branches(1)
            .missedLines(1)
            .lines(1)
            .missedMethods(1)
            .methods(1)
            .missedClasses(1)
            .classes(1);
    }

    public static TestReport getTestReportSample2() {
        return new TestReport()
            .id(2L)
            .html("html2")
            .missedInstructions(2)
            .instructions(2)
            .missedBranches(2)
            .branches(2)
            .missedLines(2)
            .lines(2)
            .missedMethods(2)
            .methods(2)
            .missedClasses(2)
            .classes(2);
    }

    public static TestReport getTestReportRandomSampleGenerator() {
        return new TestReport()
            .id(longCount.incrementAndGet())
            .html(UUID.randomUUID().toString())
            .missedInstructions(intCount.incrementAndGet())
            .instructions(intCount.incrementAndGet())
            .missedBranches(intCount.incrementAndGet())
            .branches(intCount.incrementAndGet())
            .missedLines(intCount.incrementAndGet())
            .lines(intCount.incrementAndGet())
            .missedMethods(intCount.incrementAndGet())
            .methods(intCount.incrementAndGet())
            .missedClasses(intCount.incrementAndGet())
            .classes(intCount.incrementAndGet());
    }
}

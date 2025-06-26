package com.mihael.mhipster.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CodeStatsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CodeStats getCodeStatsSample1() {
        return new CodeStats().id(1L);
    }

    public static CodeStats getCodeStatsSample2() {
        return new CodeStats().id(2L);
    }

    public static CodeStats getCodeStatsRandomSampleGenerator() {
        return new CodeStats().id(longCount.incrementAndGet());
    }
}

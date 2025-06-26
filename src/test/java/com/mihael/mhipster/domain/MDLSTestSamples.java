package com.mihael.mhipster.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MDLSTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MDLS getMDLSSample1() {
        return new MDLS().id(1L).baseConfig("baseConfig1");
    }

    public static MDLS getMDLSSample2() {
        return new MDLS().id(2L).baseConfig("baseConfig2");
    }

    public static MDLS getMDLSRandomSampleGenerator() {
        return new MDLS().id(longCount.incrementAndGet()).baseConfig(UUID.randomUUID().toString());
    }
}

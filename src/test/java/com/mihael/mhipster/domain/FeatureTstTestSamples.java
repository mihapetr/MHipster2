package com.mihael.mhipster.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FeatureTstTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FeatureTst getFeatureTstSample1() {
        return new FeatureTst().id(1L);
    }

    public static FeatureTst getFeatureTstSample2() {
        return new FeatureTst().id(2L);
    }

    public static FeatureTst getFeatureTstRandomSampleGenerator() {
        return new FeatureTst().id(longCount.incrementAndGet());
    }
}

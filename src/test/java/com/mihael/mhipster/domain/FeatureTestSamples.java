package com.mihael.mhipster.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FeatureTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Feature getFeatureSample1() {
        return new Feature().id(1L).name("name1");
    }

    public static Feature getFeatureSample2() {
        return new Feature().id(2L).name("name2");
    }

    public static Feature getFeatureRandomSampleGenerator() {
        return new Feature().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}

package com.mihael.mhipster.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OverviewTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Overview getOverviewSample1() {
        return new Overview().id(1L);
    }

    public static Overview getOverviewSample2() {
        return new Overview().id(2L);
    }

    public static Overview getOverviewRandomSampleGenerator() {
        return new Overview().id(longCount.incrementAndGet());
    }
}

package com.mihael.mhipster.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProjectTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Project getProjectSample1() {
        return new Project().id(1L).name("name1").description("description1").location("location1");
    }

    public static Project getProjectSample2() {
        return new Project().id(2L).name("name2").description("description2").location("location2");
    }

    public static Project getProjectRandomSampleGenerator() {
        return new Project()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString());
    }
}

package com.bzu.smartvax.web;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class WekaInitConfig {

    @PostConstruct
    public void forcePureJavaNetlib() {
        System.setProperty("com.github.fommil.netlib.BLAS", "com.github.fommil.netlib.F2jBLAS");
        System.setProperty("com.github.fommil.netlib.LAPACK", "com.github.fommil.netlib.F2jLAPACK");
        System.setProperty("com.github.fommil.netlib.ARPACK", "com.github.fommil.netlib.F2jARPACK");
        System.out.println("✅ Pure Java Netlib configuration applied successfully.");
    }
}

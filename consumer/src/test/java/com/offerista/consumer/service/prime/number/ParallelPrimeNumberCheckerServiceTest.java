package com.offerista.consumer.service.prime.number;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

class ParallelPrimeNumberCheckerServiceTest {

    private ParallelPrimeNumberCheckerService testSubject;

    @BeforeEach
    void setUp() throws Exception {
        try (var ignored = MockitoAnnotations.openMocks(this)) {
            testSubject = new ParallelPrimeNumberCheckerService();
        }
    }

    @Test
    void test_isPrime_normal_returnsCorrectResult() {
        Assertions.assertFalse(testSubject.isPrime(0));
        Assertions.assertFalse(testSubject.isPrime(1));
        Assertions.assertTrue(testSubject.isPrime(2));
        Assertions.assertTrue(testSubject.isPrime(3));
        Assertions.assertFalse(testSubject.isPrime(4));
        Assertions.assertTrue(testSubject.isPrime(5));
        Assertions.assertFalse(testSubject.isPrime(6));
        Assertions.assertTrue(testSubject.isPrime(7));
        Assertions.assertFalse(testSubject.isPrime(8));
        Assertions.assertFalse(testSubject.isPrime(9));
        Assertions.assertFalse(testSubject.isPrime(10));
        Assertions.assertTrue(testSubject.isPrime(11));
        Assertions.assertFalse(testSubject.isPrime(12));
        Assertions.assertTrue(testSubject.isPrime(1223));
        Assertions.assertTrue(testSubject.isPrime(9173));
        Assertions.assertTrue(testSubject.isPrime(19843));
        Assertions.assertTrue(testSubject.isPrime(25471));
        Assertions.assertFalse(testSubject.isPrime(25473));
    }

    @Test
    void test_isPrime_nullInput_returnsCorrectResult() {
        Assertions.assertFalse(testSubject.isPrime(null));
    }
}
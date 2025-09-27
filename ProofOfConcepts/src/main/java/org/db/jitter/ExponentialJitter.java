package org.db.jitter;

import java.util.function.Supplier;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

public class ExponentialJitter {
    public static void main(String[]args ) {
        RetryConfig config = RetryConfig.<String>custom()
                .maxAttempts(5) // Maximum number of retry attempts
                .intervalFunction(attempt -> {
                    long baseDelay = 1000; // Base delay in milliseconds
                    long maxDelay = 60000; // Maximum delay in milliseconds
                    double jitterFactor = 0.5; // Jitter factor (e.g., 0.5 for 50% randomness)

                    long exponentialDelay = (long) (baseDelay * Math.pow(2, attempt - 1));
                    long jitter = (long) (Math.random() * exponentialDelay * jitterFactor);
                    long delay = Math.min(exponentialDelay + jitter, maxDelay);
                    return delay;
                })
                .build();

        // Create a RetryRegistry and get a Retry instance
        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("myServiceRetry");

        // Define the operation to be retried
        Supplier<String> backendCall = Retry.decorateSupplier(retry, () -> {
            System.out.println("Attempting backend call...");
            // Simulate a transient failure
            if (Math.random() > 0.3) { // 70% chance of failure
                throw new RuntimeException("Service temporarily unavailable");
            }
            return "Success!";
        });

        try {
            String result = backendCall.get();
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.err.println("Operation failed after multiple retries: " + e.getMessage());
        }
    }
}

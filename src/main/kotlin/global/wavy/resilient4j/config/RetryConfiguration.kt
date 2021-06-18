package global.wavy.resilient4j.config

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.retry.RetryConfig
import io.github.resilience4j.retry.RetryRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class RetryConfiguration {

    @Bean
    fun retry(): Retry {
        val config = RetryConfig.custom<RetryConfig>()
            .maxAttempts(2)
            .waitDuration(Duration.ofSeconds(10))
            .build()
        val retryRegistry = RetryRegistry.ofDefaults();
        return retryRegistry.retry("manual", config)
    }

    @Bean
    fun circuitBreaker(): CircuitBreaker {
        val config = CircuitBreakerConfig.custom()
            .failureRateThreshold(50.0f)
            .slidingWindowSize(10)
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .minimumNumberOfCalls(5)
            .permittedNumberOfCallsInHalfOpenState(3)
            .build()
        val circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults()
        return circuitBreakerRegistry.circuitBreaker("manual", config)
    }
}
package global.wavy.resilient4j.controller

import global.wavy.resilient4j.client.MockClient
import global.wavy.resilient4j.client.RetryResponse
import global.wavy.resilient4j.config.CircuitBreakerAnnotation
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.vavr.control.Try
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("circuitbreaker")
class CircuitBreakerController (private val mockClient: MockClient,
                                private val circuitBreaker: CircuitBreaker){

    private val log = LoggerFactory.getLogger(CircuitBreakerController::class.java)

    @GetMapping("annotation")
    @CircuitBreakerAnnotation(name = "circuitbreaker-test", fallbackMethod = "circuitBreakerFallback")
    fun circuitBreakerByAnnotation():String {
        log.info("circuitBreaker")
        return mockClient.retryWS().message
    }

    fun circuitBreakerFallback(t:Throwable):String {
        log.info("fallback")
        return "Error in circuit break method"
    }

    @GetMapping("manual")
    fun circuitBreakerManual():String {
        val circuitBreakeble = CircuitBreaker.decorateCheckedSupplier(circuitBreaker) {
            mockClient.retryWS()
        }
        val result: Try<RetryResponse> = Try.of(circuitBreakeble)
        log.info("${result.isSuccess}")
        return if (result.isSuccess) result.get().message else "fallback"
    }

}
package global.wavy.resilient4j.controller

import global.wavy.resilient4j.client.MockClient
import global.wavy.resilient4j.client.RetryResponse
import global.wavy.resilient4j.config.RetryAnnotation
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.retry.RetryRegistry
import io.vavr.control.Try
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RestController
@RequestMapping("retry")
class RetryController(
    private val mockClient: MockClient,
    private val retry: Retry
) {

    private val log = LoggerFactory.getLogger(RetryController::class.java)

    @RetryAnnotation(name = "retry-test")
    @GetMapping("annotation")
    fun retryByAnnotation(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatDateTime = now.format(formatter)

        log.info("M=retry $formatDateTime")
        return mockClient.retryWS().message
    }

    @GetMapping("manual")
    fun retryManual(): String {
        val retryable = Retry.decorateCheckedSupplier(retry) { mockClient.retryWS() }

        val result: Try<RetryResponse> = Try.of(retryable)
            .recover { throw it }
        return result.get().message
    }
}
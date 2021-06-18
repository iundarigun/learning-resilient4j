package global.wavy.resilient4j.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient("mockClient", url ="http://localhost:1899/retry")
interface MockClient {
    @GetMapping
    fun retryWS(): RetryResponse
}

data class RetryResponse(
    var message: String
)
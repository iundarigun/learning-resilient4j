package global.wavy.resilient4j

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class Resilient4jApplication

fun main(args: Array<String>) {
	runApplication<Resilient4jApplication>(*args)
}

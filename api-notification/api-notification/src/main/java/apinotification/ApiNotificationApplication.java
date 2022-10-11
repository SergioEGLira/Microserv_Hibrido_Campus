package apinotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ApiNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiNotificationApplication.class, args);
	}

}

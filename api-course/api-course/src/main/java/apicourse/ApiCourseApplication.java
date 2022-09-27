package apicourse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ApiCourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCourseApplication.class, args);
	}

}

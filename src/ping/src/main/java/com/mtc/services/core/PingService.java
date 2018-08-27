package com.mtc.services.core;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@EnableCaching
@RequestMapping("ping")
public class PingService{

	private static final String THE_RECORD = "record";

	public static void main(String[] args) {
		SpringApplication.run(PingService.class, args);
	}

	@RequestMapping(value="/{recordId}", method=RequestMethod.GET)
	@Cacheable( value=THE_RECORD, key="#recordId" )
    public String index(@PathVariable int recordId){
		System.out.println("Executing " +  this.getClass().getSimpleName() + ".index(" + recordId + ")");
		return LocalDateTime.now().toString();
    }
}

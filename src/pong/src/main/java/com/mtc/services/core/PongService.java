package com.mtc.services.core;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("pong")
public class PongService {

	public static void main(String[] args) {
		SpringApplication.run(PongService.class, args);
	}

	@RequestMapping(value="/{recordId}", method=RequestMethod.GET)
	public String GetRecord(@PathVariable int recordId){
		return "Retrieved record " + recordId + " from DB at " + LocalDateTime.now().toString();
	}
}

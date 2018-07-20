package com.buga.boxes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buga.boxes.server.to.ResponseObject;

@RestController
@RequestMapping("api")
public class RestfulController {

	private final Logger logger=LoggerFactory.getLogger(RestfulController.class);

	public RestfulController() {
		logger.info("Initializing...");
	}

	@GetMapping("ping")
	public ResponseEntity<ResponseObject<String>> ping() {
		logger.info("ping");
		return ResponseEntity.ok(new ResponseObject<String>("PONG"));
	}
}
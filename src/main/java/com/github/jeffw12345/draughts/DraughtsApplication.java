package com.github.jeffw12345.draughts;

import com.github.jeffw12345.draughts.client.Client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DraughtsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DraughtsApplication.class, args);

		new Thread(() -> new Client().initialize()).start();
		new Thread(() -> new Client().initialize()).start();
	}
}

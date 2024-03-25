package com.github.jeffw12345.draughts.server;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class MessageController {
    @PostMapping
    public void receiveMessage(@RequestBody String message) {
        //TODO - Use Jackson to log message, convert message to objects and act on message.

    }
}
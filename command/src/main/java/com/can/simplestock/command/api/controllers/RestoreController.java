package com.can.simplestock.command.api.controllers;

import com.can.simplestock.command.api.commands.RestoreCommand;
import com.can.simplestock.common.dto.BaseResponse;
import com.can.simplestock.cqrsescore.command.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "api/v1/restore")
public class RestoreController {

    private final Logger logger = Logger.getLogger(RestoreController.class.getName());
    private final CommandDispatcher commandDispatcher;

    @Autowired
    public RestoreController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> restore() {
        try {
            commandDispatcher.send(new RestoreCommand());
            return new ResponseEntity<>(new BaseResponse("restore is successful"), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            logger.log(Level.WARNING, MessageFormat.format("Bad request - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected Error Occurred.");
            return new ResponseEntity<>(new BaseResponse("Unexpected Error Occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.can.simplestock.command.api.controllers;

import com.can.simplestock.command.api.commands.OpenStockCommand;
import com.can.simplestock.command.api.responses.OpenStockResponse;
import com.can.simplestock.common.dto.BaseResponse;
import com.can.simplestock.cqrsescore.command.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "api/v1/open-stock")
public class OpenStockController {

    private final Logger logger = Logger.getLogger(OpenStockController.class.getName());
    private final CommandDispatcher commandDispatcher;

    @Autowired
    public OpenStockController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> openStock(@RequestBody OpenStockCommand command) {
        var id = UUID.randomUUID().toString();
        command.setId(id);

        try {
            commandDispatcher.send(command);
            return new ResponseEntity<>(new OpenStockResponse("Stock creation is successful", id), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            logger.log(Level.WARNING, MessageFormat.format("Bad request - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected Error Occurred.");
            return new ResponseEntity<>(new OpenStockResponse("Unexpected Error Occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

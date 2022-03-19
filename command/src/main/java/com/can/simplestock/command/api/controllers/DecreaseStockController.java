package com.can.simplestock.command.api.controllers;

import com.can.simplestock.command.api.commands.DecreaseStockCommand;
import com.can.simplestock.common.dto.BaseResponse;
import com.can.simplestock.cqrsescore.command.CommandDispatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "api/v1/decrease-stock")
public class DecreaseStockController {

    private final Logger logger = Logger.getLogger(DecreaseStockController.class.getName());
    private final CommandDispatcher commandDispatcher;

    public DecreaseStockController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PutMapping(path = "/{id}")
    ResponseEntity<BaseResponse> decreaseStock(@PathVariable String id, @RequestBody DecreaseStockCommand command) {
        try {
            command.setId(id);
            commandDispatcher.send(command);
            return new ResponseEntity<>(new BaseResponse("Decreasing stock is successful"), HttpStatus.OK);
        } catch (IllegalStateException e) {
            logger.log(Level.WARNING, MessageFormat.format("Bad request - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected Error Occurred.");
            return new ResponseEntity<>(new BaseResponse("Unexpected Error Occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

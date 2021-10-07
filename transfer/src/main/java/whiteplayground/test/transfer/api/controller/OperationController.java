package whiteplayground.test.transfer.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.api.service.OperationService;
import whiteplayground.test.transfer.model.BasicOperationRequest;
import whiteplayground.test.transfer.model.TransferRequest;
import whiteplayground.test.transfer.utils.response.ResponseMessage;

@RestController
@RequestMapping("operation")
public class OperationController {
    @Autowired
    OperationService operationSvc;
    @Autowired
    ResponseMessage messages;


    @PostMapping(value = "/transfer")
    @ResponseBody
    @Operation(summary = "transfer", security = @SecurityRequirement(name = "bearerAuth"))
    public Mono<ResponseEntity> transfer(@RequestBody TransferRequest request){
        return getUsernameFromSecurityContext().flatMap(name->
                        this.operationSvc.transfer(name,request))
                .map(result->{
                    if(result.isSuccess()){
                        return ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.TEXT_PLAIN)
                                .body(messages.getSuccess());
                    }else{
                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                                .contentType(MediaType.TEXT_PLAIN)
                                .body(result.getMessage());
                    }
                });
    }

    @PostMapping(value = "/withdraw")
    @ResponseBody
    @Operation(summary = "withdraw", security = @SecurityRequirement(name = "bearerAuth"))
    public Mono<ResponseEntity> withdraw(@RequestBody BasicOperationRequest request){
        return getUsernameFromSecurityContext().flatMap(name->
                        this.operationSvc.withdraw(name,request))
                .map(result->{
                    if(result.isSuccess()){
                        return ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.TEXT_PLAIN)
                                .body(messages.getSuccess());
                    }else{
                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                                .contentType(MediaType.TEXT_PLAIN)
                                .body(result.getMessage());
                    }
                });
    }

    @PostMapping(value = "/deposit")
    @ResponseBody
    @Operation(summary = "deposit", security = @SecurityRequirement(name = "bearerAuth"))
    public Mono<ResponseEntity> deposit(@RequestBody BasicOperationRequest request){
        return getUsernameFromSecurityContext().flatMap(name->
                        this.operationSvc.deposit(name,request))
                .map(result->{
                    if(result.isSuccess()){
                        return ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.TEXT_PLAIN)
                                .body(messages.getSuccess());
                    }else{
                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                                .contentType(MediaType.TEXT_PLAIN)
                                .body(result.getMessage());
                    }
                });
    }

    private Mono<String> getUsernameFromSecurityContext() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(context -> context.getAuthentication().getName());
    }
}

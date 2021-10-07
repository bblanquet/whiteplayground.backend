package whiteplayground.test.transfer.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.api.repository.CustomerRepository;
import whiteplayground.test.transfer.model.CustomerEntity;
import whiteplayground.test.transfer.security.*;
import whiteplayground.test.transfer.utils.response.Response;
import whiteplayground.test.transfer.utils.response.ResponseMessage;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private HashEncoder passwordEncoder;
    @Autowired
    private ResponseMessage messages;

    public Mono<CustomerEntity> findByUsername(String username) {
        return customerRepo.findByName(username);
    }

    public Mono<Boolean> existByUsername(String username) {
        return customerRepo.exitByName(username);
    }

    public Mono<Response<AuthenticateResponse>> signIn(AuthenticateRequest ar){
        return this.findByUsername(ar.getUsername())
                .filter(userDetails -> {
                    var hashedPassword= passwordEncoder.toSha256(ar.getPassword());
                    return hashedPassword.equals(userDetails.getPassword());
                })
                .map(userDetails -> {
                    var token = tokenManager.generateToken(userDetails);
                    var response = new AuthenticateResponse(token);
                    return Response.success(response);
                })
                .switchIfEmpty(Mono.just(Response.error(messages.getLoginError())));
    }

    public Mono<Response<AuthenticateResponse>> signUp(AuthenticateRequest request) {
        return existByUsername(request.getUsername())
                .flatMap(exist->{
                    if(exist){
                        return Mono.just(Response.error(String.format(messages.getAlreadyExist(),request.getUsername())));
                    }else{
                        return this.save(request);
                    }
                });
    }

    private Mono<Response<AuthenticateResponse>> save(AuthenticateRequest request) {
        var copy = new AuthenticateRequest(request.getUsername(), request.getPassword());
        copy.setPassword(passwordEncoder.toSha256(request.getPassword()));
        return this.customerRepo
                .save(CustomerEntity
                        .builder()
                        .name(copy.getUsername())
                        .password(copy.getPassword())
                        .build())
                    .flatMap(
                            savedItem ->
                                    signIn(request));
    }

    public Mono<Long> getId(String name) {
        return this.customerRepo.findByName(name).map(c->c.getId());
    }


}
package whiteplayground.test.transfer.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.api.repository.CustomerRepository;
import whiteplayground.test.transfer.model.Customer;
import whiteplayground.test.transfer.security.*;
import whiteplayground.test.transfer.utils.response.Response;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private HashEncoder passwordEncoder;

    public Mono<Customer> findByUsername(String username) {
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
                .switchIfEmpty(Mono.just(Response.error("wrong username or wrong password.")));
    }

    public Mono<Response<AuthenticateResponse>> signUp(AuthenticateRequest request) {
        return existByUsername(request.getUsername())
                .flatMap(exist->{
                    if(exist){
                        return Mono.just(Response.error("username already exists."));
                    }else{
                        return this.save(request);
                    }
                });
    }

    private Mono<Response<AuthenticateResponse>> save(AuthenticateRequest request) {
        var copy = new AuthenticateRequest(request.getUsername(), request.getPassword());
        copy.setPassword(passwordEncoder.toSha256(request.getPassword()));
        return this.customerRepo
                .save(new Customer(copy.getUsername(),copy.getPassword()))
                    .flatMap(
                            savedItem ->
                                    signIn(request));
    }
}
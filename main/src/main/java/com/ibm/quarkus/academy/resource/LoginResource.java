package com.ibm.quarkus.academy.resource;

import com.ibm.quarkus.academy.service.TokenService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/login")
@RequestScoped
public class LoginResource {

    @Inject
    TokenService tokenService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String create(@QueryParam("email") String email,
                         @QueryParam("username") String username,
                         @QueryParam("password") String password) {

        String token = tokenService.generate(email, username);

        return token;
    }
}

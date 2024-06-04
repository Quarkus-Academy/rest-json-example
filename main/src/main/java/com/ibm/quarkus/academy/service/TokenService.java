package com.ibm.quarkus.academy.service;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RequestScoped
public class TokenService {


    public String generate(String email, String username) {
        JwtClaimsBuilder builder = Jwt.claims();
        builder.subject(username);

        JsonObject custom = Json.createObjectBuilder()
                .add("hobbies", "football")
                .build();

        builder.upn(email);
        builder.audience("quarkus-academy");
        if("test@test.sk".equals(email)){
            builder.groups("DUMMY");
        } else {
            builder.groups("ADMIN");
        }
        builder.issuer("http://localhost:8081/quarkus-academy");
        builder.claim("date", custom);

        return builder.upn(username).sign();

    }
}

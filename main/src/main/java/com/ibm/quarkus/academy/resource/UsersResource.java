package com.ibm.quarkus.academy.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import com.ibm.quarkus.academy.dto.UserDto;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Path("/api/users")
public class UsersResource {

    @GET
    @Produces({"application/json"})
    @Authenticated
    public Response users() {
        UserDto user1 = UserDto.builder()
                .name("Adam")
                .surname("Seidel")
                .birthDate(LocalDate.of(1990, 1, 1)).build();
        UserDto user2 = UserDto.builder()
                .name("John")
                .surname("Doe")
                .birthDate(LocalDate.of(1990, 2, 1)).build();
        List<UserDto> users = List.of(user1, user2);
        log.info("Retrieved {} users", users.size());
        return Response.ok(users).build();
    }


    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        log.info("User with id {} was deleted.", id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response save(UserDto userDto) {
        log.info("User {} was saved.", userDto);
        userDto.setId("some-id");
        try {
            return Response.status(Response.Status.CREATED)
                    .entity(userDto)
                    .location(new URI(String.format("/user/%s", userDto.getId())))
                    .build();
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    @PUT
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response update(UserDto userDto) {
        log.info("User {} was updated.", userDto);
        return Response.ok(userDto).build();
    }

    @GET
    @Path("/{id}")
    @Produces({"application/json"})
    @RolesAllowed("ADMIN")
    public Response user(@PathParam("id") String id,
                         @QueryParam("omit-birthdate") boolean omitBirthdate) {
        UserDto userDto = UserDto.builder()
                .name("Adam")
                .surname("Seidel")
                .id(id)
                .birthDate(omitBirthdate ? null : LocalDate.of(1990, 1, 1))
                .build();
        log.info("User {} was found", userDto);
        return Response.ok(userDto).build();
    }
}

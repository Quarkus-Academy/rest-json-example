package com.ibm.quarkus.academy.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import com.ibm.quarkus.academy.dto.UserDto;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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
    @Operation(summary = "All users",
            description = "Returns all of the users")
    @Tag(name = "Users")
    @APIResponses({@APIResponse(
            responseCode = "200",
            description = "List of all users",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))
    )})
    public Response users() {
        UserDto user1 = UserDto.builder()
                .id("some-id-1")
                .name("Adam")
                .surname("Seidel")
                .birthDate(LocalDate.of(1990, 1, 1)).build();
        UserDto user2 = UserDto.builder()
                .id("some-id-2")
                .name("John")
                .surname("Doe")
                .birthDate(LocalDate.of(1990, 2, 1)).build();
        List<UserDto> users = List.of(user1, user2);
        log.info("Retrieved {} users", users.size());
        return Response.ok(users).build();
    }


    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete user",
            description = "Deletes user having the provided id")
    @Tag(name = "Users")
    @APIResponses({@APIResponse(
            responseCode = "204",
            description = "Confirmation if the user was deleted",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))
    )})
    public Response delete(@Parameter(description = "Id of the user to be deleted", example = "1", required = true) @PathParam("id") String id) {
        log.info("User with id {} was deleted.", id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @Operation(summary = "Save user",
            description = "Saves the user")
    @Tag(name = "Users")
    @APIResponses({@APIResponse(
            responseCode = "200",
            description = "Saved user",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))
    )})
    public Response save(@RequestBody(description = "Created user object", required = true,
            content = @Content(schema = @Schema(implementation = UserDto.class))) UserDto simpleDto) {
        log.info("User {} was saved.", simpleDto);
        simpleDto.setId("some-id");
        try {
            return Response.status(Response.Status.CREATED)
                    .entity(simpleDto)
                    .location(new URI(String.format("/user/%s", simpleDto.getId())))
                    .build();
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    @PUT
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @Operation(summary = "Update user",
            description = "Updates the user")
    @Tag(name = "Users")
    @APIResponses({@APIResponse(
            responseCode = "200",
            description = "Updated user",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))
    )})
    public Response update(@RequestBody(description = "Updated user object", required = true,
            content = @Content(schema = @Schema(implementation = UserDto.class))) UserDto userDto) {
        log.info("User {} was updated.", userDto);
        return Response.ok(userDto).build();
    }

    @GET
    @Path("/{id}")
    @Produces({"application/json"})
    @Operation(summary = "Get user",
            description = "Returns user with the provided id")
    @Tag(name = "Users")
    @APIResponses({@APIResponse(
            responseCode = "200",
            description = "User with the provided id",

            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))
    )})
    public Response user(@Parameter(description = "Id of the user", example = "1") @PathParam("id") String id,
                         @Parameter(description = "Flag to disable/enable birthday", example = "true") @QueryParam("omit-birthdate") Boolean omitBirthdate) {
        if ("1".equals(id)) {
            return Response.serverError().build();
        } else if ("2".equals(id)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        UserDto userDto = UserDto.builder()
                .name("Adam")
                .surname("Seidel")
                .id(id)
                .birthDate(omitBirthdate == null ? null : LocalDate.of(1990, 1, 1))
                .build();
        log.info("User {} was found", userDto);
        return Response.ok(userDto).build();
    }

}

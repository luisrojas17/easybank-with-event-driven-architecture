package com.easybank.profile.query.controller;

import com.easybank.profile.dto.ProfileDto;
import com.easybank.profile.query.FindProfileQuery;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class ProfileController {

    private final QueryGateway queryGateway;

    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> fetch(
            @RequestParam("mobileNumber")
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                    String mobileNumber) {

        FindProfileQuery findProfileQuery = new FindProfileQuery(mobileNumber);

        // To publish query object
        ProfileDto profileDto = queryGateway.query(findProfileQuery,
                ResponseTypes.instanceOf(ProfileDto.class)).join();

        return ResponseEntity.status(HttpStatus.OK).body(profileDto);
    }
}

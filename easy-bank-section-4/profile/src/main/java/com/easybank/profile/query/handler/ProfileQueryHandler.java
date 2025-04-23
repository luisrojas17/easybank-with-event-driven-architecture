package com.easybank.profile.query.handler;

import com.easybank.profile.dto.ProfileDto;
import com.easybank.profile.query.FindProfileQuery;
import com.easybank.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileQueryHandler {

    private final ProfileService profileService;

    // This method is invoking through QueryGateway instance which is injected by Controller.
    @QueryHandler
    public ProfileDto findProfile(FindProfileQuery findProfileQuery) {

        log.info("Processing FindProfileQuery.\n[{}]", findProfileQuery);

        return profileService.find(findProfileQuery.getMobileNumber());
    }
}

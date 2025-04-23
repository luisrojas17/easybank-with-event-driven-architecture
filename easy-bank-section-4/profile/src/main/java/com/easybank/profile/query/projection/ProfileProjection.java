package com.easybank.profile.query.projection;

import com.easybank.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.springframework.stereotype.Component;

/**
 * This class contains methods to process the events published by the aggregate component.
 * Each method is annotated with @EventHandler in order to listen to the events published by the aggregate component.
 */
@Slf4j
@Component
@ProcessingGroup("customer-group")
@RequiredArgsConstructor
public class ProfileProjection {

    private final ProfileService profileService;

    // To receive the event and store customer data updated into the reading database

}

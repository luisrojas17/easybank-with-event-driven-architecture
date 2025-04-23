package com.easybank.profile.service.impl;

import com.easybank.profile.dto.ProfileDto;
import com.easybank.profile.entity.ProfileEntity;
import com.easybank.profile.exception.ResourceNotFoundException;
import com.easybank.profile.mapper.ProfileMapper;
import com.easybank.profile.repository.ProfileRepository;
import com.easybank.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    public ProfileDto find(String mobileNumber) {
        ProfileEntity profileEntity =
                profileRepository.findByMobileNumberAndActiveSw(
                        mobileNumber, true)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Profile", "mobileNumber",
                                        mobileNumber)
        );
        ProfileDto profileDto =
                ProfileMapper.mapToDto(profileEntity, new ProfileDto());

        log.info("Profile gotten was: [{}].", profileDto);

        return profileDto;
    }

}

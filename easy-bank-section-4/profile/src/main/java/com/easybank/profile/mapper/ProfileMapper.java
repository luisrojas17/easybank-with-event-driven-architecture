package com.easybank.profile.mapper;

import com.easybank.profile.dto.ProfileDto;
import com.easybank.profile.entity.ProfileEntity;
import org.springframework.beans.BeanUtils;

public class ProfileMapper {

    public static ProfileDto mapToDto(ProfileEntity profileEntity, ProfileDto profileDto) {
        BeanUtils.copyProperties(profileEntity, profileDto);
        return profileDto;
    }

}

package com.easybank.profile.service;

import com.easybank.profile.dto.ProfileDto;

public interface ProfileService {

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Profile Details based on a given mobileNumber
     */
    ProfileDto find(String mobileNumber);

}

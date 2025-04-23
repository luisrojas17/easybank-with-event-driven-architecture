package com.easybank.profile.service.impl;

import com.easybank.common.event.AccountDataChangedEvent;
import com.easybank.common.event.CardDataChangedEvent;
import com.easybank.common.event.CustomerDataChangedEvent;
import com.easybank.common.event.LoanDataChangedEvent;
import com.easybank.profile.constants.ProfileConstants;
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

    @Override
    public void handler(AccountDataChangedEvent accountDataChangedEvent) {

        ProfileEntity profileEntity =
                profileRepository.findByMobileNumberAndActiveSw(
                        accountDataChangedEvent.getMobileNumber(), ProfileConstants.ACTIVE_SW)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Profile", "mobileNumber",
                                        accountDataChangedEvent.getMobileNumber()));

        profileEntity.setAccountNumber(accountDataChangedEvent.getAccountNumber());

        profileRepository.save(profileEntity);
    }

    @Override
    public void handler(CardDataChangedEvent cardDataChangedEvent) {

        ProfileEntity profileEntity =
                profileRepository.findByMobileNumberAndActiveSw(
                                cardDataChangedEvent.getMobileNumber(), ProfileConstants.ACTIVE_SW)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Profile", "mobileNumber",
                                        cardDataChangedEvent.getMobileNumber()));

        profileEntity.setCardNumber(cardDataChangedEvent.getCardNumber());

        profileRepository.save(profileEntity);
    }

    @Override
    public void handler(CustomerDataChangedEvent customerDataChangedEvent) {

        ProfileEntity profileEntity =
                profileRepository.findByMobileNumberAndActiveSw(
                        customerDataChangedEvent.getMobileNumber(), ProfileConstants.ACTIVE_SW)
                        .orElseGet(ProfileEntity::new);

        // To update the data in database according to customer microservice event.
        profileEntity.setName(customerDataChangedEvent.getName());
        profileEntity.setMobileNumber(customerDataChangedEvent.getMobileNumber());
        profileEntity.setActiveSw(customerDataChangedEvent.isActiveSw());

        profileRepository.save(profileEntity);

    }

    @Override
    public void handler(LoanDataChangedEvent loanDataChangedEvent) {

        ProfileEntity profileEntity =
                profileRepository.findByMobileNumberAndActiveSw(
                                loanDataChangedEvent.getMobileNumber(), ProfileConstants.ACTIVE_SW)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Profile", "mobileNumber",
                                        loanDataChangedEvent.getMobileNumber()));

        profileEntity.setLoanNumber(loanDataChangedEvent.getLoanNumber());

        profileRepository.save(profileEntity);
    }

}

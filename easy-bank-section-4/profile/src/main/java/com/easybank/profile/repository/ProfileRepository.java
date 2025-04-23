package com.easybank.profile.repository;

import com.easybank.profile.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {

    Optional<ProfileEntity> findByMobileNumberAndActiveSw(String mobileNumber, boolean active);

}

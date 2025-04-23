package com.easybank.profile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id", nullable = false)
    private long profileId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "mobile_number", length = 20, nullable = false)
    private String mobileNumber;

    @Column(name = "active_sw", nullable = false)
    private boolean activeSw = false;

    @Column(name = "account_number")
    private Long accountNumber;

    @Column(name = "card_number")
    private Long cardNumber;

    @Column(name = "loan_number")
    private Long loanNumber;
}

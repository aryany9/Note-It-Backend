package com.note.it.Entities;

import com.note.it.Entities.base.BaseAuditableUUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@Entity
@Builder
@Table()
@NoArgsConstructor
public class User extends BaseAuditableUUID {

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "is_verified", columnDefinition = "boolean default false")
    private Boolean isVerified;
    @Column(nullable = false, name="email_id")
    private String emailId;
}

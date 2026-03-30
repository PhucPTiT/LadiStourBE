package com.ladi.stour.entity;

import com.ladi.stour.common.BaseDocument;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseDocument {
    @Id
    private String id;

    private String username;
    private String password;
    private String email;
    private String fullName;
    private Boolean isActive = true;
    private String role = "USER"; // USER, ADMIN
}

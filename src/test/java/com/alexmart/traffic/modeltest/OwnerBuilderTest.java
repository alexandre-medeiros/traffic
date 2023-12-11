package com.alexmart.traffic.modeltest;

import com.alexmart.traffic.domain.model.Owner;
public class OwnerBuilderTest {

    private Long id;
    private String name;
    private String email;
    private String phone;

    public OwnerBuilderTest withId(Long id) {
        this.id = id;
        return this;
    }

    public OwnerBuilderTest withName(String name) {
        this.name = name;
        return this;
    }

    public OwnerBuilderTest withEmail(String email) {
        this.email = email;
        return this;
    }

    public OwnerBuilderTest withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Owner build() {
        return new Owner(id, name, email, phone);
    }
}

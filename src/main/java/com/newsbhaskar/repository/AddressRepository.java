package com.newsbhaskar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsbhaskar.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

}

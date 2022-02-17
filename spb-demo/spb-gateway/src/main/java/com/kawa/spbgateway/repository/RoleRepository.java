package com.kawa.spbgateway.repository;


import com.kawa.spbgateway.domain.Roles;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends ReactiveCrudRepository<Roles, String> {

}

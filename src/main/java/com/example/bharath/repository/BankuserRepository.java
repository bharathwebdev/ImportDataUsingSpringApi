package com.example.bharath.repository;

import com.example.bharath.model.BankUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankuserRepository  extends JpaRepository<BankUsers,Long> {


    List<BankUsers> findByEmail(String email);

}

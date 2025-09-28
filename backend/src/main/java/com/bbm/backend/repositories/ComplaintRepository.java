package com.bbm.backend.repositories;

import com.bbm.backend.models.Complaint;
import com.bbm.backend.utils.enums.ComplaintStatus;
import com.bbm.backend.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByStatus(ComplaintStatus status);
    List<Complaint> findByCustomer(Customer customer);
}

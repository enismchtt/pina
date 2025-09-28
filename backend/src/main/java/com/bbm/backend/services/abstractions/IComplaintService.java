package com.bbm.backend.services.abstractions;

import com.bbm.backend.models.Complaint;
import com.bbm.backend.utils.enums.ComplaintStatus;

import java.util.List;
import java.util.Optional;

public interface IComplaintService {
    List<Complaint> findAll();
    Optional<Complaint> findById(Long id);
    Complaint save(Complaint complaint);
    void deleteById(Long id);
    List<Complaint> findByStatus(ComplaintStatus status);
    List<Complaint> findByCustomerId(Long customerId);
    void updateComplaintStatus(Long complaintId, ComplaintStatus status);
    void resolveComplaint(Long complaintId, String resolution);
}

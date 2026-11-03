package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.AvailabilitySlot;

import java.util.List;

@Repository
public interface IAvailabilitySlotRepository
        extends JpaRepository<AvailabilitySlot, String> {

    List<AvailabilitySlot> findByTrainer_UserId(String userId);
}
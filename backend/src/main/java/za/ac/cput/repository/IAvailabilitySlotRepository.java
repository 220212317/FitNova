package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.AvailabilitySlot;

/*
 * Author: Phumelela Sakie (240040546)
 */

public interface IAvailabilitySlotRepository
        extends JpaRepository<AvailabilitySlot, String> {

}

package za.ac.cput.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import za.ac.cput.domain.AvailabilitySlot;
import za.ac.cput.domain.enums.SlotStatus;
import za.ac.cput.service.IAvailabilitySlotService;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvailabilitySlotController.class)
class AvailabilitySlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAvailabilitySlotService service;

    @Test
    void create() throws Exception {

        AvailabilitySlot slot = new AvailabilitySlot.Builder()
                .setSlotId("SLOT001")
                .setDate(LocalDate.of(2026, 8, 22))
                .setStartTime(LocalTime.of(9, 0))
                .setEndTime(LocalTime.of(10, 0))
                .setStatus(SlotStatus.AVAILABLE)
                .build();

        when(service.create(any(AvailabilitySlot.class)))
                .thenReturn(slot);

        String json = """
                {
                    "slotId": "SLOT001",
                    "date": "2026-08-22",
                    "startTime": "09:00:00",
                    "endTime": "10:00:00",
                    "status": "AVAILABLE"
                }
                """;

        mockMvc.perform(post("/availability-slots/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slotId").value("SLOT001"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void read() throws Exception {

        AvailabilitySlot slot = new AvailabilitySlot.Builder()
                .setSlotId("SLOT001")
                .setDate(LocalDate.of(2026, 8, 22))
                .setStartTime(LocalTime.of(9, 0))
                .setEndTime(LocalTime.of(10, 0))
                .setStatus(SlotStatus.AVAILABLE)
                .build();

        when(service.read("SLOT001"))
                .thenReturn(slot);

        mockMvc.perform(get("/availability-slots/read/SLOT001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slotId").value("SLOT001"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void readAll() throws Exception {

        mockMvc.perform(get("/availability-slots/all"))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {

        AvailabilitySlot slot = new AvailabilitySlot.Builder()
                .setSlotId("SLOT001")
                .setDate(LocalDate.of(2026, 8, 22))
                .setStartTime(LocalTime.of(9, 0))
                .setEndTime(LocalTime.of(10, 0))
                .setStatus(SlotStatus.AVAILABLE)
                .build();

        when(service.update(any(AvailabilitySlot.class)))
                .thenReturn(slot);

        String json = """
                {
                    "slotId": "SLOT001",
                    "date": "2026-08-22",
                    "startTime": "09:00:00",
                    "endTime": "10:00:00",
                    "status": "AVAILABLE"
                }
                """;

        mockMvc.perform(put("/availability-slots/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slotId").value("SLOT001"));
    }

    @Test
    void delete() throws Exception {

        when(service.delete("SLOT001"))
                .thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/availability-slots/delete/SLOT001"))
                .andExpect(status().isNoContent());
    }
}
package org.example.ticketbookingsystem;

import com.jayway.jsonpath.JsonPath;
import org.example.ticketbookingsystem.domain.Role;
import org.example.ticketbookingsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BookingFlowApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private String userToken;
    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        userToken = registerAndLogin("booker@test.com");
        adminToken = registerAndLogin("admin@test.com");
        promoteToAdmin("admin@test.com");
    }

    @Test
    void createEvent_listAvailableTickets_holdAndConfirm() throws Exception {
        Long eventId = createEvent(adminToken);
        Long ticketId = fetchFirstAvailableTicketId(userToken, eventId);

        String holdResponse = mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "ticketIds": [%d]
                                }
                                """.formatted(ticketId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        long bookingId = readLong(holdResponse, "$.id");

        mockMvc.perform(post("/api/v1/bookings/%d/confirm".formatted(bookingId))
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void holdSameTicketTwice_secondRequestFails() throws Exception {
        Long eventId = createEvent(adminToken);
        Long ticketId = fetchFirstAvailableTicketId(userToken, eventId);

        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "ticketIds": [%d]
                                }
                                """.formatted(ticketId)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "ticketIds": [%d]
                                }
                                """.formatted(ticketId)))
                .andExpect(status().isConflict());
    }

    @Test
    void createEvent_requiresAdminRole() throws Exception {
        mockMvc.perform(post("/api/v1/events")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventRequestBody()))
                .andExpect(status().isForbidden());
    }

    private String registerAndLogin(String email) throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "secret1",
                                  "firstName": "Nika",
                                  "lastName": "Test"
                                }
                                """.formatted(email)))
                .andExpect(status().isCreated());

        String loginResponse = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "secret1"
                                }
                                """.formatted(email)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(loginResponse, "$.token");
    }

    private void promoteToAdmin(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        });
    }

    private Long createEvent(String token) throws Exception {
        String response = mockMvc.perform(post("/api/v1/events")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventRequestBody()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalTickets").value(4))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return readLong(response, "$.id");
    }

    private Long fetchFirstAvailableTicketId(String token, Long eventId) throws Exception {
        String response = mockMvc.perform(get("/api/v1/events/%d/tickets/available".formatted(eventId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return readLong(response, "$[0].id");
    }

    private long readLong(String json, String path) {
        Number value = JsonPath.read(json, path);
        return value.longValue();
    }

    private String eventRequestBody() {
        return """
                {
                  "title": "Test Concert",
                  "description": "Demo event",
                  "eventDate": "2030-12-31T20:00:00",
                  "location": "Tbilisi Arena",
                  "basePrice": 50.0,
                  "rows": 2,
                  "seatsPerRow": 2
                }
                """;
    }
}

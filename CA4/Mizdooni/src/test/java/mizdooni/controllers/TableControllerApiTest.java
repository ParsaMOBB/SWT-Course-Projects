package mizdooni.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.beans.Transient;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mizdooni.exceptions.InvalidManagerRestaurant;
import mizdooni.exceptions.UserNotManager;
import mizdooni.model.Address;
import mizdooni.model.Restaurant;
import mizdooni.model.Table;
import mizdooni.model.User;
import mizdooni.response.ResponseException;
import mizdooni.service.RestaurantService;
import mizdooni.service.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TableControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private TableService tableService;

    @Autowired
    private ObjectMapper objectMapper;

    private User manager;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        reset(restaurantService, tableService);

        manager = new User("Mobed", "bb", "mobed@gmail.com", new Address("Iran", "Tehran", "Valfajr"), User.Role.manager);
        
        restaurant = new Restaurant("Little", manager, "Pizza", LocalTime.parse("08:00"), LocalTime.parse("22:00"), "Pizza midim, Napolitan!", new Address("Iran", "Tehran", "Vanak Park"), "Chara Nadarim ?");
    }

    @Test 
    void getTable_RestaurantIdInvalid_ShouldThrowResponseException() throws Exception {
        mockMvc.perform(get("/tables/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTables_RestaurantIdNotFound_ShouldThrowResponseException() throws Exception {
        when(restaurantService.getRestaurant(1)).thenReturn(null);

        mockMvc.perform(get("/tables/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("restaurant not found"));
    }

    @Test
    void getTables_TablesEmpty_ShouldReturnEmptyList() throws Exception {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);
        when(tableService.getTables(1)).thenReturn(List.of());

        mockMvc.perform(get("/tables/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("tables listed"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    void getTables_TablesNotEmpty_ShouldReturnTables() throws Exception {
        List<Table> tables = Arrays.asList(new Table(1, 1, 4), new Table(2, 1, 6));
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);
        when(tableService.getTables(1)).thenReturn(tables);

        mockMvc.perform(get("/tables/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("tables listed"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data[0].tableNumber").value(1))
                .andExpect(jsonPath("$.data[0].seatsNumber").value(4))
                .andExpect(jsonPath("$.data[1].tableNumber").value(2))
                .andExpect(jsonPath("$.data[1].seatsNumber").value(6));
    }

    @Test
    void addTable_RestaurantIdNotFound_ShouldThrowResponseException() throws Exception {
        when(restaurantService.getRestaurant(1)).thenReturn(null);

        Map<String, String> params = new HashMap<>();
        params.put("seatsNumber", "4");

        mockMvc.perform(post("/tables/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("restaurant not found"));
    }

    @Test
    void addTable_SeatsNumberMissing_ShouldThrowResponseException() throws Exception {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);

        Map<String, String> params = new HashMap<>();

        mockMvc.perform(post("/tables/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("parameters missing"));
    }

    @Test 
    void addTable_SeatsNumberInvalid_ShouldThrowResponseException() throws Exception {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);

        Map<String, String> params = new HashMap<>();
        params.put("seatsNumber", "abc");

        mockMvc.perform(post("/tables/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("bad parameter type"));
    }

    @Test
    void addTable_UserNotManager_ShouldThrowResponseException() throws Exception {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);
        doThrow(new UserNotManager()).when(tableService).addTable(1, 4);

        Map<String, String> params = new HashMap<>();
        params.put("seatsNumber", "4");

        mockMvc.perform(post("/tables/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("User is not a manager."));
    }

    @Test
    void addTable_InvalidManagerRestaurant_ShouldThrowResponseException() throws Exception {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);
        doThrow(new InvalidManagerRestaurant()).when(tableService).addTable(1, 4);

        Map<String, String> params = new HashMap<>();
        params.put("seatsNumber", "4");

        mockMvc.perform(post("/tables/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("The manager is not valid for this restaurant."));
    }

    @Test
    void addTable_TableAdded_ShouldReturnSuccess() throws Exception {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);

        Map<String, String> params = new HashMap<>();
        params.put("seatsNumber", "4");

        mockMvc.perform(post("/tables/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("table added"));
    }
}

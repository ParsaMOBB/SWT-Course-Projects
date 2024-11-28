package mizdooni.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import mizdooni.exceptions.DuplicatedRestaurantName;
import mizdooni.exceptions.InvalidWorkingTime;
import mizdooni.exceptions.UserNotManager;
import mizdooni.model.*;
import mizdooni.response.PagedList;
import mizdooni.response.ResponseException;
import mizdooni.service.RestaurantService;
import mizdooni.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.beans.Transient;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    private Restaurant restaurant;
    private User manager;

    @BeforeEach
    public void setUp() {
        reset(restaurantService);

        manager = new User("Mobed", "bb", "mobed@gmail.com", new Address("Iran", "Tehran", "Valfajr"), User.Role.manager);

        restaurant = new Restaurant("Little", manager, "Pizza", LocalTime.parse("08:00"), LocalTime.parse("22:00"), "Pizza midim, Napolitan!", new Address("Iran", "Tehran", "Vanak Park"), "Chara Nadarim ?");
    }

    @Test
    void getRestaurant_RestaurantIdNotFound_ShouldThrowResponseException() throws Exception {
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(null);

        mockMvc.perform(get("/restaurants/{restaurantId}", restaurant.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("restaurant not found"));
    }

    @Test 
    void getRestaurant_RestaurantIdFound_ShouldReturnRestaurant() throws Exception {
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);

        mockMvc.perform(get("/restaurants/{restaurantId}", restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("restaurant found"))
                .andExpect(jsonPath("$.data.name").value("Little"))
                .andExpect(jsonPath("$.data.type").value("Pizza"));
    }

    @Test
    void getRestaurants_RestaurantsEmpty_ShouldReturnEmptyList() throws Exception {
        when(restaurantService.getRestaurants(1, null)).thenReturn(new PagedList<>(List.of(), 1, 1));

        mockMvc.perform(get("/restaurants?page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("restaurants listed"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void getRestaurants_SingleRestaurantSinglePage_ShouldReturnRestaurants() throws Exception {
        int page = 1;
        List<Restaurant> restaurants = List.of(restaurant);
        PagedList<Restaurant> pagedRestaurants = new PagedList<>(restaurants, 1, 1);

        when(restaurantService.getRestaurants(eq(page), any()))
            .thenReturn(pagedRestaurants);

        mockMvc.perform(get("/restaurants").param("page", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("restaurants listed"))
            .andExpect(jsonPath("$.data.page").value(1))
            .andExpect(jsonPath("$.data.totalPages").value(1))
            .andExpect(jsonPath("$.data.pageList").isArray())
            .andExpect(jsonPath("$.data.pageList").isNotEmpty())
            .andExpect(jsonPath("$.data.pageList[0].name").value("Little"))
            .andExpect(jsonPath("$.data.pageList[0].type").value("Pizza"))
            .andExpect(jsonPath("$.data.pageList[0].address.country").value("Iran"))
            .andExpect(jsonPath("$.data.pageList[0].address.city").value("Tehran"))
            .andExpect(jsonPath("$.data.pageList[0].address.street").value("Vanak Park"));
    }

    @Test
    void getRestaurant_MultipleRestaurantSinglePage_ShouldReturnRestaurants() throws Exception {
        int page = 1;
        Restaurant restaurant2 = new Restaurant("Little 2", manager, "Pizza", LocalTime.parse("08:00"), LocalTime.parse("22:00"), "Pizza midim, Napolitan!", new Address("Iran", "Tehran", "Vanak Park"), "Chara Nadarim ?");
        Restaurant restaurant3 = new Restaurant("Little 3", manager, "Pizza", LocalTime.parse("08:00"), LocalTime.parse("22:00"), "Pizza midim, Napolitan!", new Address("Iran", "Tehran", "Vanak Park"), "Chara Nadarim ?");
        Restaurant restaurant4 = new Restaurant("Little 4", manager, "Pizza", LocalTime.parse("08:00"), LocalTime.parse("22:00"), "Pizza midim, Napolitan!", new Address("Iran", "Tehran", "Vanak Park"), "Chara Nadarim ?");
        List<Restaurant> restaurants = List.of(restaurant, restaurant2, restaurant3, restaurant4);
        PagedList<Restaurant> pagedRestaurants = new PagedList<>(restaurants, 1, 4);

        when(restaurantService.getRestaurants(eq(page), any()))
            .thenReturn(pagedRestaurants);
        
        mockMvc.perform(get("/restaurants?page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("restaurants listed"))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.pageList.size()").value(4))
                .andExpect(jsonPath("$.data.pageList").isArray())
                .andExpect(jsonPath("$.data.pageList").isNotEmpty())
                .andExpect(jsonPath("$.data.pageList[0].name").value("Little"))
                .andExpect(jsonPath("$.data.pageList[1].name").value("Little 2"));
    }

    @Test
    void getManagerRestaurants_ManagerIdNotFound_ShouldThrowResponseException() throws Exception {
        when(restaurantService.getManagerRestaurants(1)).thenReturn(List.of());

        mockMvc.perform(get("/restaurants/manager/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("manager restaurants listed"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void getManagerRestaurants_ManagerIdFound_ShouldReturnRestaurants() throws Exception {
        when(restaurantService.getManagerRestaurants(manager.getId())).thenReturn(List.of(restaurant, restaurant));

        mockMvc.perform(get("/restaurants/manager/{managerId}", manager.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("manager restaurants listed"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data[0].name").value("Little"))
                .andExpect(jsonPath("$.data[0].type").value("Pizza"))
                .andExpect(jsonPath("$.data[0].type").value("Pizza"));
    }

    @Test 
    void addRestaurant_MissingParameters_ShouldThrowResponseException() throws Exception {
        Map<String, Object> params = new HashMap<>();

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("parameters missing"));
    }

    @Test 
    void addRestaurant_RestaurantAdded_ShouldReturnRestaurantId() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "New Little");
        params.put("type", "Pizza");
        params.put("startTime", "08:00");
        params.put("endTime", "22:00");
        params.put("description", "Pizza midim, Napolitan!");
        params.put("address", new Address("Iran", "Tehran", "Vanak Park"));
        params.put("image", "Chara Nadarim ?");

        when(restaurantService.addRestaurant(anyString(), anyString(), any(), any(), anyString(), any(), anyString())
        ).thenReturn(7);

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("restaurant added"))
                .andExpect(jsonPath("$.data").value(7));
    }

    @Test 
    void addRestaurant_AddDuplicateRestaurant_ShouldThrowResponseException() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Little");
        params.put("type", "Pizza");
        params.put("startTime", "08:00");
        params.put("endTime", "22:00");
        params.put("description", "Pizza midim, Napolitan!");
        params.put("address", new Address("Iran", "Tehran", "Vanak Park"));
        params.put("image", "Chara Nadarim ?");

        doThrow(new ResponseException(HttpStatus.BAD_REQUEST, "restaurant name is taken"))
            .when(restaurantService)
            .addRestaurant(anyString(), anyString(), any(), any(), anyString(), any(), anyString());

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("restaurant name is taken"));
    }

    @Test 
    void addRestaurant_ManagerNotFound_ShouldThrowResponseException() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Little");
        params.put("type", "Pizza");
        params.put("startTime", "08:00");
        params.put("endTime", "22:00");
        params.put("description", "Pizza midim, Napolitan!");
        params.put("address", new Address("Iran", "Tehran", "Vanak Park"));
        params.put("image", "Chara Nadarim ?");

        doThrow(new ResponseException(HttpStatus.NOT_FOUND, "manager not found"))
                .when(restaurantService)
                .addRestaurant(anyString(), anyString(), any(), any(), anyString(), any(), anyString());

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("manager not found"));
    }

    @Test 
    void addRestaurant_InvalidWorkingTime_ShouldThrowResponseException() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Little");
        params.put("type", "Pizza");
        params.put("startTime", "08:00");
        params.put("endTime", "22:00");
        params.put("description", "Pizza midim, Napolitan!");
        params.put("address", new Address("Iran", "Tehran", "Vanak Park"));
        params.put("image", "Chara Nadarim ?");

        doThrow(new ResponseException(HttpStatus.BAD_REQUEST, "invalid working time"))
                .when(restaurantService)
                .addRestaurant(anyString(), anyString(), any(), any(), anyString(), any(), anyString());

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("invalid working time"));
    }

    @Test 
    void validateRestaurantName_NameTaken_ShouldThrowResponseException() throws Exception {
        when(restaurantService.restaurantExists("Little")).thenReturn(true);

        mockMvc.perform(get("/validate/restaurant-name?data=Little"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("restaurant name is taken"));
    }

    @Test 
    void validateRestaurantName_NameAvailable_ShouldReturnOKResponse() throws Exception {
        when(restaurantService.restaurantExists("Little")).thenReturn(false);

        mockMvc.perform(get("/validate/restaurant-name?data=Little"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("restaurant name is available"));
    }

    @Test 
    void getRestaurantTypes_ShouldReturnRestaurantTypes() throws Exception {
        Set<String> types = Set.of("Pizza", "Burger", "Kebab");
        when(restaurantService.getRestaurantTypes()).thenReturn(types);

        mockMvc.perform(get("/restaurants/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("restaurant types"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.hasItem("Pizza")))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.hasItem("Burger")))
                .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.hasItem("Kebab")));
    }

    @Test 
    void getRestaurantLocations_ShouldReturnRestaurantLocations() throws Exception {
        Map<String, Set<String>> locations = Map.of("Iran", Set.of("Tehran", "Shiraz"), "Turkey", Set.of("Istanbul", "Ankara"));
        when(restaurantService.getRestaurantLocations()).thenReturn(locations);

        mockMvc.perform(get("/restaurants/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("restaurant locations"))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.Iran").isArray())
                .andExpect(jsonPath("$.data.Iran").isNotEmpty())
                .andExpect(jsonPath("$.data.Iran").value(org.hamcrest.Matchers.hasItem("Tehran")))
                .andExpect(jsonPath("$.data.Iran").value(org.hamcrest.Matchers.hasItem("Shiraz")))
                .andExpect(jsonPath("$.data.Turkey").isArray())
                .andExpect(jsonPath("$.data.Turkey").isNotEmpty())
                .andExpect(jsonPath("$.data.Turkey").value(org.hamcrest.Matchers.hasItem("Istanbul")))
                .andExpect(jsonPath("$.data.Turkey").value(org.hamcrest.Matchers.hasItem("Ankara")));
    }
}

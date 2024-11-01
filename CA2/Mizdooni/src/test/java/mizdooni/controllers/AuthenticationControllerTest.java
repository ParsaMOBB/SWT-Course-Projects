package mizdooni.controllers;

import mizdooni.model.Address;
import mizdooni.model.User;
import mizdooni.response.Response;
import mizdooni.response.ResponseException;
import mizdooni.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController authController;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User("Arshia", "aa", "arshiya.ataei81@gmail.com", new Address("Iran", "Tehran", "Amirabad"), User.Role.manager);;
        user2 = new User("Mobed", "bb", "mobed@gmail.com", new Address("Iran", "Tehran", "Valfajr"), User.Role.manager);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUser_whenUserLoggedIn_thenReturnUser() {
        when(userService.getCurrentUser()).thenReturn(user1);

        Response response = authController.user();

        assertEquals("current user", response.getMessage());
        assertEquals(user1, response.getData());
        verify(userService, times(1)).getCurrentUser();
    }

    @Test
    void testUser_whenNoUserLoggedIn_thenThrowUnauthorized() {
        when(userService.getCurrentUser()).thenReturn(null);

        ResponseException exception = assertThrows(ResponseException.class, () -> authController.user());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("no user logged in", exception.getMessage());
    }

    @Test
    void testLogin_whenValidCredentials_thenReturnSuccessResponse() {
        String username = "arshia";
        String password = "aa";
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        when(userService.login(username, password)).thenReturn(true);
        when(userService.getCurrentUser()).thenReturn(user1);

        Response response = authController.login(params);

        assertEquals("login successful", response.getMessage());
        assertNotNull(response.getData());
        verify(userService, times(1)).login(username, password);
    }

    @Test
    void testLogin_whenInvalidCredentials_thenThrowUnauthorized() {
        String username = "arshia";
        String password = "bb";
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        when(userService.login(username, password)).thenReturn(false);

        ResponseException exception = assertThrows(ResponseException.class, () -> authController.login(params));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("invalid username or password", exception.getMessage());
    }

    @Test
    void testSignup_whenValidData_thenReturnSuccessResponse() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "arshia");
        params.put("password", "aa");
        params.put("email", "arshiya.ataei81@gmail.com");
        params.put("role", "client");
        Map<String, String> address = new HashMap<>();
        address.put("country", "Iran");
        address.put("city", "Tehran");
        address.put("address", "Amirabad");
        params.put("address", address);
        try {
            doNothing().when(userService).signup(anyString(), anyString(), anyString(), any(Address.class), any(User.Role.class));
        } catch (Exception e){}
        when(userService.login("arshia", "aa")).thenReturn(true);
        when(userService.getCurrentUser()).thenReturn(user1);
        Response response = authController.signup(params);

        assertEquals("signup successful", response.getMessage());
        assertNotNull(response.getData());
        try {
            verify(userService, times(1)).signup(anyString(), anyString(), anyString(), any(Address.class), any(User.Role.class));
        } catch (Exception e){}
        verify(userService, times(1)).login("arshia", "aa");
    }

    @Test
    void testValidateUsername_whenUsernameExists_thenThrowConflict() {
        String username = "existingUser";
        when(userService.usernameExists(username)).thenReturn(true);

        ResponseException exception = assertThrows(ResponseException.class, () -> authController.validateUsername(username));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("username already exists", exception.getMessage());
    }

    @Test
    void testLogout_whenUserLoggedIn_thenReturnSuccessResponse() {
        when(userService.logout()).thenReturn(true);

        Response response = authController.logout();

        assertEquals("logout successful", response.getMessage());
        verify(userService, times(1)).logout();
    }

    @Test
    void testLogout_whenNoUserLoggedIn_thenThrowUnauthorized() {
        when(userService.logout()).thenReturn(false);

        ResponseException exception = assertThrows(ResponseException.class, () -> authController.logout());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("no user logged in", exception.getMessage());
    }
}

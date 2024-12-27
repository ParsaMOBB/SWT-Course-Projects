Feature: addReservation

  Scenario: Adding a reservation
    Given a first user
    And a restaurant
    When the user adds a reservation to the restaurant
    Then the reservation list of the user should contain the reservation

  Scenario: Adding multiple reservations to the same restaurant
    Given a first user
    And a restaurant
    When the user adds a reservation to the restaurant
    And the user adds a reservation to the restaurant
    Then the reservation list of the user should contain 2 reservations

  Scenario: Checking if a restaurant is reserved
    Given a first user
    And a restaurant
    When the user adds a reservation to the restaurant
    Then the user should have the restaurant reserved

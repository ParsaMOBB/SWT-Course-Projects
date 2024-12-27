Feature: addReservation

  Scenario: Adding a reservation
    Given a user with username "arshia" and email "arshia@gmail.com"
    And a restaurant named "Little Italy" managed by user "mobed"
    When the user adds a reservation to the restaurant
    Then the reservation list of the user should contain the reservation

  Scenario: Adding multiple reservations to the same restaurant
    Given a user with username "arshia" and email "arshia@gmail.com"
    And a restaurant named "Little Italy" managed by user "mobed"
    When the user adds a reservation to the restaurant
    And the user adds a reservation to the restaurant
    Then the reservation list of the user should contain 2 reservations

  Scenario: Checking if a restaurant is reserved
    Given a user with username "arshia" and email "arshia@gmail.com"
    And a restaurant named "Little Italy" managed by user "mobed"
    When the user adds a reservation to the restaurant
    Then the user should have the restaurant reserved

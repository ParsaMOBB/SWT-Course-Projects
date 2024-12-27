Feature: User reservations management
  As a user
  I want to manage my reservations
  So that I can keep track of my bookings with restaurants

  Scenario: Adding a reservation
    Given a user with username "arshia" and email "arshia@gmail.com"
    And a restaurant named "Little Italy" managed by user "mobed"
    When the user adds a reservation to the restaurant
    Then the reservation list of the user should contain the reservation

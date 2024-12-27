Feature: Restaurant review and rating management
  As a user
  I want to leave reviews and see average ratings for a restaurant
  So that I can provide feedback and check restaurant performance

  Scenario: Adding a review to a restaurant
    Given a user with username "arshia" and email "arshia@gmail.com"
    And a restaurant named "Little Italy" managed by user "mobed"
    And a review with food 4, service 5, ambiance 4, and overall 4
    When the user adds the review to the restaurant
    Then the restaurant should have the review in its reviews list

  Scenario: Calculating the average rating of a restaurant
    Given a user with username "arshia" and email "arshia@gmail.com"
    And a restaurant named "Little Italy" managed by user "mobed"
    And a review with food 4, service 5, ambiance 4, and overall 4
    And a review with food 3, service 4, ambiance 3, and overall 3
    When the average rating is calculated for the restaurant
    Then the average rating should be food 3, service 4, ambiance 3, and overall 3

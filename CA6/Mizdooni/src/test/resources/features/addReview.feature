Feature: Restaurant review
  As a user
  I want to leave reviews for a restaurant
  So that I can provide feedback and check restaurant performance

  Scenario: Adding a review to a restaurant
    Given a user with username "arshia" and email "arshia@gmail.com"
    And a restaurant named "Little Italy" managed by user "mobed"
    And a review with food 4, service 5, ambiance 4, and overall 4
    When the user adds the review to the restaurant
    Then the restaurant should have the review in its reviews list
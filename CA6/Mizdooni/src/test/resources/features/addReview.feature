Feature: addReview

  Scenario: Adding a review to a restaurant
    Given a user with username "arshia" and email "arshia@gmail.com"
    And a restaurant named "Little Italy" managed by user "mobed"
    And a review with food 4, service 5, ambiance 4, and overall 4
    When the user adds the review to the restaurant
    Then the restaurant should have the review in its reviews list

  Scenario: Adding multiple reviews from different users
    Given a user with username "arshia" and email "arshia@gmail.com"
    And a restaurant named "Little Italy" managed by user "mobed"
    And a review with food 4, service 5, ambiance 4, and overall 4
    When the user adds the review to the restaurant
    And a user with username "john" and email "john@example.com"
    And a review with food 3, service 4, ambiance 3, and overall 3
    And the user adds the review to the restaurant
    Then the restaurant should have 2 reviews in its reviews list

  Scenario: Replacing a review by the same user
    Given a user with username "arshia" and email "arshia@gmail.com"
    And a restaurant named "Little Italy" managed by user "mobed"
    And a review with food 4, service 5, ambiance 4, and overall 4
    When the user adds the review to the restaurant
    And a review with food 2, service 3, ambiance 2, and overall 2
    And the user adds the review to the restaurant
    Then the restaurant should only have 1 review
    And the review in the list should have food 2, service 3, ambiance 2, and overall 2

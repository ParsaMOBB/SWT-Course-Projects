Feature: getAverageRating

Scenario: Calculating the average rating with two reviews
    Given a first user
    And a restaurant
    And a review with food 4, service 5, ambiance 4, and overall 4
    When the user adds the review to the restaurant
    Given a second user
    And a review with food 3, service 4, ambiance 3, and overall 3
    And the user adds the review to the restaurant
    And the average rating is calculated for the restaurant
    Then the average rating should be food 3.5, service 4.5, ambiance 3.5, and overall 3.5

Scenario: User Change its Review and the Average didn't change
    Given a first user
    And a restaurant
    And a review with food 4, service 5, ambiance 4, and overall 4
    When the user adds the review to the restaurant
    And a review with food 3, service 4, ambiance 3, and overall 3
    And the user adds the review to the restaurant
    And the average rating is calculated for the restaurant
    Then the average rating should be food 3, service 4, ambiance 3, and overall 3

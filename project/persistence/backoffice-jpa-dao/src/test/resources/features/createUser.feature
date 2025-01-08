Feature: Create a new user in the database
  Connect to the database and create a new user

  Scenario: Create a new user
    Given I want to create the following user 'maxmin13' with first name 'Max' and last name 'Min', born the day 12 of the month 4 in the year 1999
    Then I create it
    Then I search for 'maxmin13' user in the database
    When I check whether it's there
    Then I should be told "Yes"
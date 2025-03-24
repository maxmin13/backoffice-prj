@user
Feature: Search for a user in the database
  Connect to the database and search for user

  @deleteUsers
  Scenario: search and find a user by account name
    Given I search for 'arth14' user account name in the database
    When I check if the user 'maxmin14' is there
    Then I should be told "nope"  
    Given I want to create the following user
      | arth14 | Arth1 | Garfunkel1 | 1999 September 23 | Legal |
    Then I create it   
    Then I search for 'arth14' user account name in the database
    And I check if the user 'maxmin14' is there
    Then I should be told "yes" 

          
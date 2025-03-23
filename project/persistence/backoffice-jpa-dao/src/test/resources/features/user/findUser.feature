@user
Feature: Search for a user in the database
  Connect to the database and search for user

  Scenario: search and find a user by account name
    Given I search for 'arth14' user account name in the database
    When I check if the user 'maxmin14' is there
    Then I should be told "nope"  
    Given I want to create the following user
      | arth14 | Arth1 | Garfunkel1 | 1999 September 23 | Legal |
    Then I create it   
    And I wait a little
    Then I search for 'arth14' user account name in the database
    And I check if the user 'maxmin14' is there
    Then I should be told "yes" 
    Then I search for 'arth14' user account name in the database
    And I check if the user 'maxmin14' is there
    Then I should be told 'yes'  
    
  Scenario: search and not find a user by account name
    Given I search for 'arth14' user account name in the database
    When I check if the user 'maxmin14' is there
    Then I should be told "nope"  
    Given I want to create the following user
      | arth14 | Arth2 | Garfunkel2 | 2011 September 23 | Legal |
    But I wait a little
    And I create it  
    And I check if a 'data integrity violation' error have been raised
    Then I search for 'arth14' user account name in the database
    And I check if the user 'maxmin14' is there
    Then I should be told "yes" 
    But the user should be
 			| arth14 | Arth1 | Garfunkel1 | 1999 September 23 | Legal |
          
@user
Feature: Search for a user in the database
  Connect to the database and search for user

  Scenario: search and find a user by account name
    Given I search for 'maxmin14' user account name in the database
    When I check whether the user it's there
    Then I should be told "Nope"  
    Given I want to create the following user
      | maxmin14 | Max1 | Min1 | 1999 September 23 | Legal |
    Then I create it   
    And I wait a little
    And I search for 'Max1' user first name in the database
    When I check whether the user it's there
    Then I should be told 'Yes'  
    
  Scenario: search and not find a user by account name
    Given I search for 'maxmin14' user account name in the database
    When I check whether the user it's there
    Then I should be told "Nope"  
    Given I want to create the following user
      | maxmin14 | Max2 | Min2 | 2011 September 23 | Legal |
    But I wait a little
    And I create it   
    And a create 'data integrity violation' error should have been raised
    And I search for 'Max2' user first name in the database
    When I check whether the user it's there
    Then I should be told 'Nope' 
 
          
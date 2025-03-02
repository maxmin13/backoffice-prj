@user
Feature: create the same user in two concurrent processes
  Connect to the database and create the same user in two parallel scenarios

  Scenario: create a user 'maxmin13' 
    Given I create a database transaction
    And the transaction isolation level is the default
    And I start the database transaction
 		When I search for 'Max1' user first name in the database
    And I check whether the user it's there
    Then I should be told 'Nope'        
    Given I want to create the following user
      | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |
    Then I create it
    And I wait a little
    Then I commit the database transaction
    And a transaction 'data integrity violation' error should have been raised
    Then I search for 'Max1' user first name in the database
    When I check whether the user it's there
    Then I should be told 'Nope'
    
  Scenario: create another user 'maxmin13'
    Given I create a database transaction
    And the transaction isolation level is the default
    And I start the database transaction
    When I search for 'Max2' user first name in the database
    And I check whether the user it's there
    Then I should be told 'Nope'     
    Given I want to create the following user
      | maxmin13 | Max2 | Min2 | 2001 August 12 | Accounting |
    Then I create it
    Then I commit the database transaction
    And I search for 'Max2' user first name in the database
    When I check whether the user it's there
    Then I should be told 'Yes'    
    And the user found should be
      | maxmin13 | Max2 | Min2 | 2001 August 12 | Accounting |
    
       
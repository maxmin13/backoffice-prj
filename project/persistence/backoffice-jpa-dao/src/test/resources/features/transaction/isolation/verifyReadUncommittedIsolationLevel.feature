@transaction
Feature: verify read uncommitted transaction isolation level
  Connect to the database and use read uncommitted isolation level transactions

  Scenario: verify dirty reads with read uncommitted isolation level
    Given I create a default database transaction
    And I set the transaction propagation to 'requires new'
    And I set the transaction isolation to 'repeatable read'
    And I start the database transaction
    When I search for 'maxmin13' user account name in the database
    And I check if the user 'maxmin13' is there
    Then I should be told 'nope'          
    Given I want to create the following user
      | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |
    Then I create it
    When I search for 'maxmin13' user account name in the database
    And I check if the user 'maxmin13' is there
    Then I should be told 'nope'          
    
    Given I create a default database transaction
    And I set the transaction propagation to 'requires new'
    And I set the transaction isolation to 'repeatable read'
    And I start the database transaction
    When I search for 'maxmin13' user account name in the database
    And I check if the user 'maxmin13' is there
    Then I should be told 'nope'  
    
    Given I create a default database transaction
    And I set the transaction propagation to 'requires new'
    And I set the transaction isolation to 'read uncommitted'
    And I start the database transaction
    When I search for 'maxmin13' user account name in the database
    And I check if the user 'maxmin13' is there
    Then I should be told 'yes'  

    #Then I rollback the 'first' database transaction
  #  And I rollback the 'second' database transaction
  #  And I rollback the 'third' database transaction
    
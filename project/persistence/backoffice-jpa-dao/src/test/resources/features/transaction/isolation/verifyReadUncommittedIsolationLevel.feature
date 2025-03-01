@transaction
Feature: verify read uncommitted transaction isolation level
  Connect to the database and use read uncommitted isolation level transactions

  Scenario: verify dirty reads with read uncommitted isolation level
    Given I create a database transaction
    And I set the transaction isolation to 'read uncommitted'
    And I start the database transaction
    
    And I rollback the database transaction
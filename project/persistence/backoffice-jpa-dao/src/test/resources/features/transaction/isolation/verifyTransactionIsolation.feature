@transaction
Feature: verify transaction isolation levels
  Connect to the database with different transaction isolation levels.
  Update an entity in a open transaction, 
  flush the changes to the database by executing a Query on the entity. 

  @deleteUsers
  Scenario: verify 'read uncommitted', 'read committed', 'repeatable read', 'serializable' transaction isolation levels
  
    Given I want to create the following user
        | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |
     Then I create the user
     When I search for 'maxmin13' user account name in the database
      And I check if the user 'maxmin13' is there
     Then I should be told 'yes'  
      And the user should be
        | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal | 
     
    Given I want to update the user 
     Then I create a database transaction
        | trx1 | repeatable read | requires new |
      And I start the transaction 'trx1'     
     Then I update the user
        | maxmin13 | Max2 | Min2 | 1999 September 23 | Legal |
      And I search for 'maxmin13' user account name in the database
     When I check if the user 'maxmin13' is there
     Then I should be told 'yes'  
      And the user should be
        | maxmin13 | Max2 | Min2 | 1999 September 23 | Legal | 
      
    Given I create a database transaction
        | trx2 | read uncommitted | requires new |
      And I start the transaction 'trx2'
     When I search for 'maxmin13' user account name in the database
      And I check if the user 'maxmin13' is there
     Then I should be told 'yes'  
      And the user should be
        | maxmin13 | Max2 | Min2 | 1999 September 23 | Legal |  
     Then I rollback the transaction 'trx2'
      
    Given I create a database transaction
        | trx3 | read committed | requires new |
      And I start the transaction 'trx3'
     When I search for 'maxmin13' user account name in the database
      And I check if the user 'maxmin13' is there
     Then I should be told 'yes'  
      And the user should be
        | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal | 
     Then I rollback the transaction 'trx3'
      
    Given I create a database transaction
        | trx4 | repeatable read | requires new |
      And I start the transaction 'trx4'
     When I search for 'maxmin13' user account name in the database
      And I check if the user 'maxmin13' is there
     Then I should be told 'yes'  
      And the user should be
        | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal | 
     Then I rollback the transaction 'trx4'
      
    Given I create a database transaction
        | trx5 | serializable | requires new |
      And I start the transaction 'trx5'
     When I search for 'maxmin13' user account name in the database
     Then I check if a 'query timeout' error have been raised
     Then I should be told 'yes'  
     Then I rollback the transaction 'trx5' 
     
     Then I rollback the transaction 'trx1' 
      
@ignore
@transaction
Feature: verify transaction isolation levels
  Connect to the database with different transaction isolation levels.
  Update an entity in a open transaction, no commit but the changes are flushed to the database by executing a 'Query',
  verify if the uncommitted changes are visible from another transaction.
  
  @deleteUsers
  Scenario: verify 'read uncommitted' transaction isolation level
  
    Given I want to create the following user
        | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |
     Then I create the user
     Then I check if an error has been raised
      And I should be told 'nope'
      
    Given I want to update the user 
     Then I create a database transaction
        | trx1 | repeatable read | requires new |
      And I start the transaction 'trx1'     
     Then I update the user
        | maxmin13 | Max2 | Min2 | 1999 September 23 | Legal |
     Then I check if an error has been raised
      And I should be told 'nope' 
      
    Given I create a database transaction
        | trx2 | read uncommitted | requires new |
      And I start the transaction 'trx2'
     When I search for 'maxmin13' user account name in the database
      And I check if the user 'maxmin13' is there
     Then I should be told 'yes'  
      And the user should be
        | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |      
     Then I rollback the transaction 'trx2'  
     
     # back to repeatable read trx1
     When I search for 'maxmin13' user account name in the database
     Then the user should be
         | maxmin13 | Max2 | Min2 | 1999 September 23 | Legal |  
     
     Then I rollback the transaction 'trx1' 
         
  @deleteUsers
  Scenario: verify 'read committed' transaction isolation level
  
    Given I want to create the following user
        | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |
     Then I create the user
     Then I check if an error has been raised
      And I should be told 'nope'
      
    Given I want to update the user 
     Then I create a database transaction
        | trx1 | repeatable read | requires new |
      And I start the transaction 'trx1'     
     Then I update the user
        | maxmin13 | Max2 | Min2 | 1999 September 23 | Legal |
     Then I check if an error has been raised
      And I should be told 'nope' 
      
    Given I create a database transaction
        | trx2 | read committed | requires new |
      And I start the transaction 'trx2'
     When I search for 'maxmin13' user account name in the database
      And I check if the user 'maxmin13' is there
     Then I should be told 'yes'  
      And the user should be
        | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |   
     Then I rollback the transaction 'trx2'  
     
     # back to repeatable read trx1
     When I search for 'maxmin13' user account name in the database
     Then the user should be
        | maxmin13 | Max2 | Min2 | 1999 September 23 | Legal |  
      
     Then I rollback the transaction 'trx1'   
     
  @deleteUsers
  Scenario: verify 'repeatable read' transaction isolation level
  
    Given I want to create the following user
        | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |
     Then I create the user
     Then I check if an error has been raised
      And I should be told 'nope'
      
    Given I want to update the user 
     Then I create a database transaction
        | trx1 | repeatable read | requires new |
      And I start the transaction 'trx1'     
     Then I update the user
        | maxmin13 | Max2 | Min2 | 1999 September 23 | Legal |
     Then I check if an error has been raised
      And I should be told 'nope' 
      
    Given I create a database transaction
        | trx2 | repeatable read | requires new |
      And I start the transaction 'trx2'
     When I search for 'maxmin13' user account name in the database
      And I check if the user 'maxmin13' is there
     Then I should be told 'yes'  
      And the user should be
        | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |         
     Then I rollback the transaction 'trx2'
     
     # back to repeatable read trx1
     When I search for 'maxmin13' user account name in the database
     Then the user should be
        | maxmin13 | Max2 | Min2 | 1999 September 23 | Legal |  
     
     Then I rollback the transaction 'trx1'  
         
  @deleteUsers
  Scenario: verify 'serializable' transaction isolation level
  
    Given I want to create the following user
        | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |
     Then I create the user
     Then I check if an error has been raised
      And I should be told 'nope'
      
    Given I want to update the user 
     Then I create a database transaction
        | trx1 | repeatable read | requires new |
      And I start the transaction 'trx1'     
     Then I update the user
        | maxmin13 | Max2 | Min2 | 1999 September 23 | Legal |
     Then I check if an error has been raised
      And I should be told 'nope'   
           
    Given I create a database transaction
        | trx2 | serializable | requires new |
      And I start the transaction 'trx2'
     When I search for 'maxmin13' user account name in the database
      Then the user should be
         | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |     
     Then I rollback the transaction 'trx2' 
     
     # back to repeatable read trx1
     When I search for 'maxmin13' user account name in the database
      Then the user should be
         | maxmin13 | Max2 | Min2 | 1999 September 23 | Legal |     
        
     Then I rollback the transaction 'trx1'                 
      
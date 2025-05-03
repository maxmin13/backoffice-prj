@transaction
Feature: verify dirty reads
      Connect to the database with read committed isolation level, verify that uncommitted changes by other
      transactions are visible.
      Uncommitted changes are flushed to the database by executing a 'Query'. 

  @deleteUsers
  Scenario: verify dirty reads are possible with read committed isolation level
  
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
     Then I rollback the transaction 'trx1'
      
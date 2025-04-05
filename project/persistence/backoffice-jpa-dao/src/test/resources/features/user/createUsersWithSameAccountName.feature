@user
Feature: create the same user twice
  Connect to the database and create the same user twice

  @deleteUsers
  Scenario: create a user 'maxmin13' 
    Given I create a default transaction 'trx1'
    And I start the transaction 'trx1'
 		When I search for 'maxmin13' user account name in the database
    And I check if the user 'maxmin13' is there
    Then I should be told 'nope'          
    Given I want to create the following user
      | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |
    Then I create the user
    And I commit the transaction 'trx1'
    When I verify if the 'commit' action was successful
    Then I should be told 'yes' 
    When I search for 'maxmin13' user account name in the database
    And I check if the user 'maxmin13' is there
    Then I should be told 'yes'    
    And the user should be
       | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |
    Given I create a default transaction 'trx1'
    And I start the transaction 'trx1' 
    And I want to create the following user
      | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |
    Then I create the user
    And I commit the transaction 'trx1'
    And I verify if the 'commit' action was successful
    Then I should be told 'nope' 
    And I check if a 'data integrity violation' error have been raised
    Then I should be told 'yes' 
    
    
    
         
      
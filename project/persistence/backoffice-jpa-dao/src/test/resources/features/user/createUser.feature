@create_user_feature
Feature: create a new user in the database
  Connect to the database and create a new user

  @Ignore
  Scenario: create a new user 
    Given I want to create the following user
      | maxmin13 | Max | Min | 1999 September 23 |
    Then I create it
    Then I search for 'maxmin13' user in the database
    When I check whether the user it's there
    Then I should be told 'Yes'
    
  Scenario: create a new user and rollback the transaction
    Given I start a database transaction
    Given I want to create the following user
      | maxmin13 | Max | Min | 1999 September 23 |
    Then I create it
    Then I search for 'maxmin13' user in the database
    When I check whether the user it's there
    Then I should be told 'Yes'    
    Then I rollback the database transaction
    Then I search for 'maxmin13' user in the database
    When I check whether the user it's there
    Then I should be told 'Nope'    
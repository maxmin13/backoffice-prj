@create_user_feature
Feature: create a new user in the database
  Connect to the database and create a new user

  Scenario: create a new user and commit the transaction 
    Given I start a database transaction
    Given I want to create the following user
      | maxmin13 | Max1 | Min1 | 1999 September 23 | Legal |
    Then I create it
    Then I commit the database transaction
    Then I search for 'maxmin13' user in the database
    When I check whether the user it's there
    Then I should be told 'Yes'
    
  Scenario: create a new user and rollback the transaction
    Given I start a database transaction
    Given I want to create the following user
      | maxmin13 | Max2 | Min2 | 2001 August 12 | Accounting |
    Then I create it
    Then I search for 'maxmin13' user in the database
    When I check whether the user it's there
    Then I should be told 'Yes'    
    Then I rollback the database transaction
    Then I search for 'maxmin13' user in the database
    When I check whether the user it's there
    Then I should be told 'Nope' 
       
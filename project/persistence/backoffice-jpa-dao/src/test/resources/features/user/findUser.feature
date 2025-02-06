@find_user_feature
Feature: Search for a user in the database
  Connect to the database and search for user

	@Ignore
  Scenario: serch a user twice by account name in the same transaction
    Given I start a database transaction
    Given I search for 'maxmin13' user in the database
    When I check whether the user it's there
    Then I should be told "Nope"  
    Given I search for 'maxmin13' user in the database
    When I check whether the user it's there
    Then I should be told "Nope" 
    Then I commit the database transaction
   
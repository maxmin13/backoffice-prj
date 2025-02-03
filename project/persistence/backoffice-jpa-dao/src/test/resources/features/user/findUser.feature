@find_user_feature
Feature: Search for a user in the database
  Connect to the database and search for user

  Scenario: serch a user by account name
    Given I search for 'maxmin13' user in the database
    When I check whether it's there
    Then I should be told "Nope"   
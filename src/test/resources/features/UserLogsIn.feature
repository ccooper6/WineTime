Feature: Logging in as a user
  Scenario: User does already have an account and wants to log in
    Given The user with name 'testUser' has an account with username 'testName' and password 'testPassword'
    When The user logs in with username 'testName' and password 'testPassword'
    Then The user is logged in as 'testUser'

  Scenario: User does already have an account and wants to log in, logs in with wrong password
    Given The user with name 'testUser' has an account with username 'testName' and password 'testPassword'
    When The user logs in with username 'testName' and password 'wrongPassword'
    Then The user isn't logged in

  Scenario: User does already have an account and wants to log in, logs in with wrong username
    Given The user with name 'testUser' has an account with username 'testName' and password 'testPassword'
    When The user logs in with username 'wrongName' and password 'TestPassword'
    Then The user isn't logged in

  Scenario: User doesn't have an account and tries to log in
    Given The user with name 'testUser' doesn't have an account
    When The user logs in with username 'wrongName' and password 'TestPassword'
    Then The user isn't logged in


Feature: Logging in as a user
  Scenario: CT_Login_1: User does already have an account and logs in with correct credentials (AT_19)
    Given The user with name 'testUser' has an account with username 'testName' and password 'testPassword1'
    When The user logs in with username 'testName' and password 'testPassword1'
    Then The user is logged in as 'testUser'

  Scenario: CT_Login_2: User does already have an account and wants to log in, logs in with wrong password (AT_20)
    Given The user with name 'testUser' has an account with username 'testName' and password 'testPassword1'
    When The user logs in with username 'testName' and password 'wrongPassword1'
    Then The user isn't logged in

  Scenario: CT_Login_3: User does already have an account and wants to log in, logs in with wrong username (AT_20)
    Given The user with name 'testUser' has an account with username 'testName' and password 'testPassword1'
    When The user logs in with username 'wrongName' and password 'testPassword1'
    Then The user isn't logged in

  Scenario: CT_Login_4: User doesn't have an account and tries to log in (AT_21)
    Given The user with name 'testUser' doesn't have an account
    When The user logs in with username 'wrongName' and password 'testPassword1'
    Then The user isn't logged in


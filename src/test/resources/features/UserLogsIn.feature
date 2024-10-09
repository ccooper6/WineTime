# Created by caleb at 17/09/2024
# Updated by isaac at 09/10/2024
Feature: Logging in as a user
  Scenario: User does already have an account and wants to log in
    Given The user with name "name" does already have an account associated to the username "username" and password "password"
    When The user logs in with the given credentials
    Then The user is logged in successfully

  Scenario: User does already have an account and wants to log in, logs in with wrong password
    Given The user with name "name" does already have an account associated to the username "username" and password "password"
    When The user logs in with the wrong password
    Then The user isn't logged in successfully

  Scenario: User does already have an account and wants to log in, logs in with wrong username
    Given The user with name "name" does already have an account associated to the username "username" and password "password"
    When The user logs in with the wrong username
    Then The user isn't logged in successfully

  Scenario: User doesn't have an account and tries to log in
    Given The user with name "name" doesn't have an account associated to the username "username" and password "password"
    When The user logs in with the assumed credentials
    Then The user isn't logged in successfully

  Scenario: User has an account and tries to reregister with same username
    Given The user with name "name" does already have an account associated to the username "username" and password "password"
    When The user tries to re register with the same username
    Then There will be an error trying to log in
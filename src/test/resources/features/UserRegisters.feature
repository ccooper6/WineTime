# Created by caleb at 17/09/2024
Feature: Registering as a user
  Scenario: User doesn't currently have an account
    Given The user with name "name" doesn't currently have an account associated to the username "username" and password "password1"
    When The user registers with the given credentials
    Then The user is registered successfully and details are stored in the database

  Scenario: User has an account and tries to reregister with same username
    Given The user with name "name" does already have an account associated to the username "username" and password "password1"
    When The user tries to re register with the same username
    Then There will be an error trying to log in

  Scenario: User registers with short password
    Given The user with name "name" doesn't currently have an account associated to the username "username" and password "pass123"
    When The user registers with the given credentials
    Then There will be an error trying to log in

  Scenario: User registers with no digits in password
    Given The user with name "name" doesn't currently have an account associated to the username "username" and password "password"
    When The user registers with the given credentials
    Then There will be an error trying to log in

  Scenario: User registers with short password
    Given The user with name "name" doesn't currently have an account associated to the username "username" and password "12345678"
    When The user registers with the given credentials
    Then There will be an error trying to log in
# Created by caleb at 17/09/2024
Feature: Logging in as a user
  Scenario: User does already have an account and wants to log in
    Given The user with name "name" does already have an account associated to the username "username" and password "password"
    When The user logs in with the given credentials
    Then The user is logged in successfully
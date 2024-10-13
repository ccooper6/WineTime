Feature: Registering as a user
  Scenario: CT_Register_1: User doesn't currently have an account
    Given The user with name "name" doesn't currently have an account associated to the username "username" and password "password1"
    When The user registers with the given credentials
    Then The user is registered successfully and details are stored in the database

  Scenario: CT_Register_2: User has an account and tries to reregister with same username
    Given The user with name "name" does already have an account associated to the username "username" and password "password1"
    When The user tries to re register with the same username
    Then The user will not be registered due to duplicate user

  Scenario: CT_Register_3: User registers with short password
    Given The user with name "name" doesn't currently have an account associated to the username "username" and password "pass123"
    When The user registers with the given credentials
    Then The user will not be registered due to error in credentials

  Scenario: CT_Register_4: User registers with no digits in password
    Given The user with name "name" doesn't currently have an account associated to the username "username" and password "password"
    When The user registers with the given credentials
    Then The user will not be registered due to error in credentials

  Scenario: CT_Register_5: User registers with short password
    Given The user with name "name" doesn't currently have an account associated to the username "username" and password "12345678"
    When The user registers with the given credentials
    Then The user will not be registered due to error in credentials

  Scenario: CT_Register_6: User registers with no name
    Given The user with name "" doesn't currently have an account associated to the username "username" and password "12345678"
    When The user registers with the given credentials
    Then The user will not be registered due to error in credentials

  Scenario: CT_Register_7: User registers with no username
    Given The user with name "name" doesn't currently have an account associated to the username "" and password "12345678"
    When The user registers with the given credentials
    Then The user will not be registered due to error in credentials

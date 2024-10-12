Feature: Participate in Challenge
  Scenario: User starts the variety challenge
    Given the user has no active challenges
    When the user starts the variety challenge
    Then 5 wines of different variety are displayed on the profile


  Scenario: User starts the Years challenge
    Given the user has no active challenges
    When the user starts time the travellers challenge
    Then 5 wines of different vintage are displayed on the profile


  Scenario: User starts the red challenge
    Given the user has no active challenges
    When the user starts the red challenge
    Then 5 different red are displayed on the profile


  Scenario: User starts the white challenge
    Given the user has no active challenges
    When the user starts the white challenge
    Then 5 different white are displayed on the profile


  Scenario: User starts the rose challenge
    Given the user has no active challenges
    When the user starts the rose challenge
    Then 5 different rose are displayed on the profile


  Scenario: User has submitted logs for each of the challenge wines
    Given user has an active challenge
    When the user completes challenge
    Then challenge is removed from the users active challenges



  Scenario: Other users challenge not affecting each others challenges
    Given User 1 and User 2 have accounts
    When User 1 starts variety challenge
    And User 2 starts reds challenge
    Then User 1 active challenge variety challenge













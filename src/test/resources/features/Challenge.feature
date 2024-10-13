Feature: Participate in Challenge
  Scenario: CT_Challenge_1: User starts the Variety challenge (AT_45)
    Given the user has no active challenges
    When the user starts the variety challenge
    Then 5 wines of different variety are displayed on the profile

  Scenario: CT_Challenge_2: User starts the Years challenge (AT_45)
    Given the user has no active challenges
    When the user starts time the travellers challenge
    Then 5 wines of different vintage are displayed on the profile

  Scenario: CT_Challenge_3: User starts the Red challenge (AT_45)
    Given the user has no active challenges
    When the user starts the red challenge
    Then 5 different red are displayed on the profile

  Scenario: CT_Challenge_4: User starts the White challenge (AT_45)
    Given the user has no active challenges
    When the user starts the white challenge
    Then 5 different white are displayed on the profile

  Scenario: CT_Challenge_5: User starts the Rose challenge (AT_45)
    Given the user has no active challenges
    When the user starts the rose challenge
    Then 5 different rose are displayed on the profile

  Scenario: CT_Challenge_6: User has submitted logs for each of the challenge wines (AT_47)
    Given user has an active challenge
    When the user completes challenge
    Then challenge is removed from the users active challenges

  Scenario: CT_Challenge_7: Other users challenge not affecting each others challenges (AT_45)
    Given User 1 and User 2 have accounts
    When User 1 starts variety challenge
    And User 2 starts reds challenge
    Then User 1 active challenge variety challenge













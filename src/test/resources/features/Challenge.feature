# Created by Lydia at 09/10/2024


Feature: Start Variety Challenge
  Scenario: User starts the variety challenge
    Given the user has no active challenges
    When the user clicks the 'start challenge' button next to the variety challenge.
    Then 5 wines of different variety are displayed on the profile



Feature: Start Years Challenge
  Scenario: User starts the Years challenge
    Given the user has no active challenges
    When the user clicks the 'start challenge' button next to the time travellers challenge.
    Then 5 wines of different vintage are displayed on the profile



Feature: Start Reds Challenge
  Scenario: User starts the red challenge
    Given the user has no active challenges
    When the user clicks the 'start challenge' button next to the red challenge.
    Then 5 different red are displayed on the profile



Feature: Start Whites Challenge
  Scenario: User starts the white challenge
    Given the user has no active challenges
    When the user clicks the 'start challenge' button next to the white challenge.
    Then 5 different white are displayed on the profile


Feature: Start Rose Challenge
  Scenario: User starts the rose challenge
    Given the user has no active challenges
    When the user clicks the 'start challenge' button next to the rose challenge.
    Then 5 different rose are displayed on the profile


Feature: Challenge completed
  Scenario: User has submitted logs for each of the challenge wines
    Given user has an active challenge
    When the user completes challenge
    Then challenge is removed from the users active challenges














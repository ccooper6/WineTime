#Created by Yuhao on 10/10/2024
Feature: Get Recommended Wines Feature
  Scenario: User likes 3 of the 5 tags of a wine
    Given The database is reset
    And User 1 reviews tags [2013, Etna, White Blend] from wine 1 with rating 5
    When User 1 gets their recommended wines
    Then User 1's recommended wines should contain tags [2013, Etna, White Blend] but not wines [1]
    And User 1's recommended wines should be sorted by number of liked tags: [2013, Etna, White Blend]

  Scenario: User has no liked tags
    Given The database is reset
    When User 1 gets their recommended wines
    Then User 1's recommended wines should be empty

  Scenario: User has disliked tags
    Given The database is reset
    And User 1 reviews tags [2013, Etna, White Blend] from wine 1 with rating 3
    And User 1 reviews tags [2013, Italy] from wine 7 with rating 1
    When User 1 gets their recommended wines
    Then User 1's recommended wines should contain tags [Etna, White Blend] but not tags [2013]
    And User 1's recommended wines should be sorted by number of liked tags: [Etna, White Blend]

  Scenario: 2 Users gets their separate recommended wines
    Given The database is reset
    And User 1 reviews tags [2013, Etna, White Blend] from wine 1 with rating 5
    And User 2 reviews tags [Rainstorm, pinot Gris] from wine 3 with rating 3
    And User 2 reviews tags [Etna, White Blend] from wine 1 with rating 1
    When User 1 gets their recommended wines
    And User 2 gets their recommended wines
    Then User 1's recommended wines should contain tags [2013, Etna, White Blend] but not wines [1]
    And User 2's recommended wines should contain tags [Rainstorm, Pinot Gris] but not wines [3]

  Scenario: Recommended wines avoid what the user has reviewed
    Given The database is reset
    And User 1 reviews tags [2013, Etna, White Blend] from wine 1 with rating 5
    And User 1 has tried wines [3, 7, 14]
    When User 1 gets their recommended wines
    Then User 1's recommended wines should contain tags [2013, Etna, White Blend] but not wines [1, 3, 7, 14]

  Scenario: Recommended wines for a non existent user
    Given The database is reset
    When User -1 gets their recommended wines
    Then User -1's recommended wines should be empty
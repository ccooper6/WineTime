# Created by yzh428 at 15/09/2024
Feature: Quiz result feature
  Scenario: User searches for New Zealand wine using tags
    Given Wines are stored correctly
    When The user searches for wines with "tags" "New Zealand"
    Then The wine list should have 1276 wines and all wines should have "New Zealand" in their "tags"

  Scenario: User searches for Pinot Noir wines in Oregon wine using tags
    Given Wines are stored correctly
    When The user searches for wines with "tags" "Oregon, Pinot Noir"
    Then The wine list should have 2557 wines and all wines should have "Oregon, Pinot Noir" in their "tags"

  Scenario: User searches for sweet using tags
    Given Wines are stored correctly
    When The user searches for wines with "tags" "Sweet"
    Then The wine list should have 0 wines and all wines should have "Sweet" in their "tags"

  Scenario: User searches for all wines with "Sweet" in the name
    Given Wines are stored correctly
    When The user searches for wines with "name" "Sweet"
    Then The wine list should have 181 wines and all wines should have "Sweet" in their "name"

  Scenario: User searches for all wines with "Non-existent Wine" in the name
    Given Wines are stored correctly
    When The user searches for wines with "name" "Non-existent Wine"
    Then The wine list should have 0 wines and all wines should have "Non-existent Wine" in their "name"

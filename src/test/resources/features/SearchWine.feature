# Created by yzh428 at 15/09/2024
Feature: Quiz result feature
#  Scenario: User searches for New Zealand wine using tags
#    Given Wines are stored correctly
#    When The user searches for wines with "tags" "New Zealand"
#    Then The wine list should have 1276 wines and all wines should have "New Zealand" in their "tags"
#
#  Scenario: User searches for Pinot Noir wines in Oregon wine using tags
#    Given Wines are stored correctly
#    When The user searches for wines with "tags" "Oregon, Pinot Noir"
#    Then The wine list should have 2557 wines and all wines should have "Oregon, Pinot Noir" in their "tags"
#
#  Scenario: User searches for sweet using tags
#    Given Wines are stored correctly
#    When The user searches for wines with "tags" "Sweet"
#    Then The wine list should have 0 wines and all wines should have "Sweet" in their "tags"
#
#  Scenario: User searches for all wines with "Sweet" in the name
#    Given Wines are stored correctly
#    When The user searches for wines with "name" "Sweet"
#    Then The wine list should have 181 wines and all wines should have "Sweet" in their "name"
#
#  Scenario: User searches for all wines with "Non-existent Wine" in the name
#    Given Wines are stored correctly
#    When The user searches for wines with "name" "Non-existent Wine"
#    Then The wine list should have 0 wines and all wines should have "Non-existent Wine" in their "name"

  #with filtering..
  Scenario: User searches for a wine with "Sweet" in the name and default filters applied
    Given Wines are stored correctly
    When The user searches for wines with query:"Sweet", country:"", winery:"", variety:"", minpoints:80, maxpoints:100, minyear:1821, maxyear:2017"
    Then The wine list should have 150 wines and all wines should have "Sweet" in their name

  Scenario: User searches for a wine with "Non-existent Wine" in the name and default filters applied
    Given Wines are stored correctly
    When The user searches for wines with query:"Non-existent Wine", country:"", winery:"", variety:"", minpoints:80, maxpoints:100, minyear:1821, maxyear:2017"
    Then The wine list should have 0 wines and all wines should have "Non-existent Wine" in their name

  Scenario: User searches for a wine with "Sauvignon Blanc" in the name and "New Zealand" as their country
    Given Wines are stored correctly
    When The user searches for wines with query:"Sauvignon Blanc", country:"New Zealand", winery:"", variety:"", minpoints:80, maxpoints:100, minyear:1821, maxyear:2017"
    Then The wine list should have 495 wines and all wines should have "Sauvignon Blanc" in their name and "New Zealand" in their tags

  Scenario: User searches for a wine with "Te Koko" in the name and "Cloudy Bay" as their winery
    Given Wines are stored correctly
    When The user searches for wines with query:"Te Koko", country:"", winery:"Cloudy Bay", variety:"", minpoints:80, maxpoints:100, minyear:1821, maxyear:2017"
    Then The wine list should have 3 wines and all wines should have "Te Koko" in their name and "Cloudy Bay" in their tags

  Scenario: User searches for a wine with "Estate" in the name and "Sauvignon Blanc" as their variety
    Given Wines are stored correctly
    When The user searches for wines with query:"Estate", country:"", winery:"", variety:"Sauvignon Blanc", minpoints:80, maxpoints:100, minyear:1821, maxyear:2017"
    Then The wine list should have 273 wines and all wines should have "Estate" in their name and "Sauvignon Blanc" in their tags

    #TODO Add to wine builder
#  Scenario: User searches for a wine with "Estate" in the name and 86 - 93 as their points range
#    Given Wines are stored correctly
#    When The user searches for wines with query:"Estate", country:"", winery:"", variety:"", minpoints:86, maxpoints:93, minyear:1821, maxyear:2017"
#    Then The wine list should have 181 wines and all wines should have "Estate" in their name and tags between "points" 86 and 93

  Scenario: User searches for a wine with "Estate" in the name and 2002 - 2007 as their year range
    Given Wines are stored correctly
    When The user searches for wines with query:"Estate", country:"", winery:"", variety:"", minpoints:80, maxpoints:100, minyear:2002, maxyear:2007"
    Then The wine list should have 822 wines and all wines should have "Estate" in their name and tags between "year" 2002 and 2007

  #TODO filter by price
#  Scenario: User searches for a wine with "Sweet" in the name and $26-35 as their price range
#    Given Wines are stored correctly
#    When The user searches for wines with query:"Sweet", country:"", winery:"", variety:"", minpoints:0, maxpoints:100, minyear:1821, maxyear:2017"
#    Then The wine list should have 181 wines and all wines should have "Sweet" in their name
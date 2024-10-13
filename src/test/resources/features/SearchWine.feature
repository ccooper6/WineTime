Feature: Wine Search feature
  Scenario: CT_Search_1: User searches for a wine with "Sweet" in the name and default filters applied (AT_2.1)
    Given Wines are stored correctly
    When The user searches for wines with query:"Sweet", country:"", winery:"", variety:"", minpoints:80, maxpoints:100, minyear:1821, maxyear:2017"
    Then The wine list should have 181 wines and all wines should have "Sweet" in their name

  Scenario: CT_Search_2: User searches for a wine with "Non-existent Wine" in the name and default filters applied (AT_3)
    Given Wines are stored correctly
    When The user searches for wines with query:"Non-existent Wine", country:"", winery:"", variety:"", minpoints:80, maxpoints:100, minyear:1821, maxyear:2017"
    Then The wine list should have 0 wines and all wines should have "Non-existent Wine" in their name

  Scenario: CT_Search_3: User searches for a wine with "Sauvignon Blanc" in the name and "New Zealand" as their country (AT_2)
    Given Wines are stored correctly
    When The user searches for wines with query:"Sauvignon Blanc", country:"New Zealand", winery:"", variety:"", minpoints:80, maxpoints:100, minyear:1821, maxyear:2017"
    Then The wine list should have 495 wines and all wines should have "Sauvignon Blanc" in their name and "New Zealand" in their tags

  Scenario: CT_Search_4: User searches for a wine with "Te Koko" in the name and "Cloudy Bay" as their winery (AT_2)
    Given Wines are stored correctly
    When The user searches for wines with query:"Te Koko", country:"", winery:"Cloudy Bay", variety:"", minpoints:80, maxpoints:100, minyear:1821, maxyear:2017"
    Then The wine list should have 3 wines and all wines should have "Te Koko" in their name and "Cloudy Bay" in their tags

  Scenario: CT_Search_5: User searches for a wine with "Estate" in the name and "Sauvignon Blanc" as their variety (AT_2)
    Given Wines are stored correctly
    When The user searches for wines with query:"Estate", country:"", winery:"", variety:"Sauvignon Blanc", minpoints:80, maxpoints:100, minyear:1821, maxyear:2017"
    Then The wine list should have 273 wines and all wines should have "Estate" in their name and "Sauvignon Blanc" in their tags

  Scenario: CT_Search_6: User searches for a wine with "Estate" in the name and 86 - 93 as their points range (AT_2)
    Given Wines are stored correctly
    When The user searches for wines with query:"Estate", country:"", winery:"", variety:"", minpoints:86, maxpoints:93, minyear:1821, maxyear:2017"
    Then The wine list should have 4847 wines and all wines should have "Estate" in their name and tags between "points" 86 and 93

  Scenario: CT_Search_7: User searches for a wine with "Estate" in the name and 2002 - 2007 as their year range (AT_2)
    Given Wines are stored correctly
    When The user searches for wines with query:"Estate", country:"", winery:"", variety:"", minpoints:80, maxpoints:100, minyear:2002, maxyear:2007"
    Then The wine list should have 827 wines and all wines should have "Estate" in their name and tags between "year" 2002 and 2007

  Scenario: CT_Search_8: User searches for a wine with "Sweet" in the name and $26-35 as their price range (AT_2)
    Given Wines are stored correctly
    When The user searches for wines with query:"Sweet", country:"", winery:"", variety:"", minpoints:0, maxpoints:100, minyear:1821, maxyear:2017"
    Then The wine list should have 181 wines and all wines should have "Sweet" in their name
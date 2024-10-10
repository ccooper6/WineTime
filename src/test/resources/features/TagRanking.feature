Feature: User can view their top 5 liked and disliked tags
  Scenario: User has not reviewed any wines
    Given User 1 has no reviews
    When The profile is viewed for User 1
    Then Both pie charts are not displayed

  Scenario: User positively reviews a wine
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 5
    And The profile is viewed for User 1
    Then The top 5 liked pie chart is shown with the tags [2011, Riesling, Germany, Mosel, Fritz Haag]
    And The top 5 disliked pie chart is greyed out

  Scenario: User negatively review a wine
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 1
    And The profile is viewed for User 1
    Then The top 5 disliked pie chart is shown with the tags [2011, Riesling, Germany, Mosel, Fritz Haag]
    And The top 5 liked pie chart is greyed out

  Scenario: User reviews a wine, but has not selected enough tags
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2013, France, Rose, Red] with a rating of 1
    And The profile is viewed for User 1
    Then Both pie charts are not displayed

  Scenario: User positively reviews multiple wines with different tags, only top 5 tags are shown
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 3
    And User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Not Fritz Haag] with a rating of 5
    And The profile is viewed for User 1
    Then The top 5 liked pie chart is shown with the tags [2011, Riesling, Germany, Mosel, Not Fritz Haag]
    And The top 5 disliked pie chart is greyed out

  Scenario: User negatively reviews multiple wines with different tags, only top 5 disliked tags are shown
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 2
    And User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Not Fritz Haag] with a rating of 1
    And The profile is viewed for User 1
    Then The top 5 disliked pie chart is shown with the tags [2011, Riesling, Germany, Mosel, Not Fritz Haag]
    And The top 5 liked pie chart is greyed out

  Scenario: User positively reviews a wine, but then later deletes their review
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 3
    And User 1 deletes the review for wine #1
    And The profile is viewed for User 1
    Then Both pie charts are not displayed

  Scenario: User negatively reviews a wine, but then later deletes their review
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 1
    And User 1 deletes the review for wine #1
    And The profile is viewed for User 1
    Then Both pie charts are not displayed

  Scenario: Really making sure the pie charts are not shown when there is 0 reviews
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 1
    And User 1 reviewed wine #2 with tags [2012, France, Germany, Mosel, Fritz Haag] with a rating of 5
    And User 1 reviewed wine #3 with tags [Europe, Malaysia, Sparkling] with a rating of 3
    And User 1 reviewed wine #4 with tags [2011, Riesling, Germany, Mosel, Pinot Noir] with a rating of 2
    And User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 4
    When User 1 deletes the review for wine #1
    And User 1 deletes the review for wine #2
    And User 1 deletes the review for wine #3
    And User 1 deletes the review for wine #4
    Then Both pie charts are not displayed

  Scenario: Other users reviews not affecting each other's pie charts
    Given User 1 is on the app
    When User 2 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 1
    And User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 5
    When The profile is viewed for User 1
    Then The top 5 liked pie chart is shown with the tags [2011, Riesling, Germany, Mosel, Fritz Haag]
    And The top 5 disliked pie chart is greyed out
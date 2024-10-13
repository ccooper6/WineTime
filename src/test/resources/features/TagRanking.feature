Feature: User can view their top 5 liked and disliked tags
  Scenario: CT_PieCharts_1: User has not reviewed any wines (AT_52)
    Given User 1 has no reviews
    When The profile is viewed for User 1
    Then Both pie charts are not displayed

  Scenario: CT_PieCharts_2: User positively reviews a wine (AT_51)
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 5
    And The profile is viewed for User 1
    Then The top 5 liked pie chart is shown with the tags [2011, Riesling, Germany, Mosel, Fritz Haag]
    And The top 5 disliked pie chart is greyed out

  Scenario: CT_PieCharts_3: User negatively review a wine (AT_51)
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 1
    And The profile is viewed for User 1
    Then The top 5 disliked pie chart is shown with the tags [2011, Riesling, Germany, Mosel, Fritz Haag]
    And The top 5 liked pie chart is greyed out

  Scenario: CT_PieCharts_4: User reviews a wine, but has not selected enough tags (AT_52)
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2013, France, Rose, Red] with a rating of 1
    And The profile is viewed for User 1
    Then Both pie charts are not displayed

  Scenario: CT_PieCharts_5: User positively reviews multiple wines with different tags, only top 5 tags are shown (AT_51)
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 3
    And User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Not Fritz Haag] with a rating of 5
    And The profile is viewed for User 1
    Then The top 5 liked pie chart is shown with the tags [2011, Riesling, Germany, Mosel, Not Fritz Haag]
    And The top 5 disliked pie chart is greyed out

  Scenario: CT_PieCharts_6: User negatively reviews multiple wines with different tags, only top 5 disliked tags are shown (AT_51)
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 2
    And User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Not Fritz Haag] with a rating of 1
    And The profile is viewed for User 1
    Then The top 5 disliked pie chart is shown with the tags [2011, Riesling, Germany, Mosel, Not Fritz Haag]
    And The top 5 liked pie chart is greyed out

  Scenario: CT_PieCharts_7: User positively reviews a wine, but then later deletes their review (AT_52)
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 3
    And User 1 deletes the review for wine #1
    And The profile is viewed for User 1
    Then Both pie charts are not displayed

  Scenario: CT_PieCharts_8: User negatively reviews a wine, but then later deletes their review (AT_52)
    Given User 1 is on the app
    When User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 1
    And User 1 deletes the review for wine #1
    And The profile is viewed for User 1
    Then Both pie charts are not displayed

  Scenario: CT_PieCharts_9: Really making sure the pie charts are not shown when there is 0 reviews (AT_52)
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

  Scenario: CT_PieCharts_10: Other users reviews not affecting each other's pie charts (AT_51)
    Given User 1 is on the app
    When User 2 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 1
    And User 1 reviewed wine #1 with tags [2011, Riesling, Germany, Mosel, Fritz Haag] with a rating of 5
    When The profile is viewed for User 1
    Then The top 5 liked pie chart is shown with the tags [2011, Riesling, Germany, Mosel, Fritz Haag]
    And The top 5 disliked pie chart is greyed out
#here to test that values are being properly updated as well as reviews being properly deleted
Feature: The new review page where all the user reviews are displayed
  Scenario: User partially selects tags when reviewing wine
   Given User 1 is logged in
   When User 1 reviews wine #1 with tags [2011, Riesling, Germany]
   And selects the tags [2011]
   And rates it a 3 and submits the review
   Then User 1 relationship with the tags [2011] is 1
   And User 1 has no relationship with the tags [Riesling, Germany]
   And User 1 has 1 review with wine #1 with description ""

  Scenario: User does not select any tag when reviewing wine
    Given User 1 is logged in
    When User 1 reviews wine #1 with tags [2011, Riesling, Germany]
    And rates it a 3 and submits the review
    Then User 1 relationship with the tags [2011, Riesling, Germany] is 1
    And User 1 has 1 review with wine #1 with description ""

  Scenario: User updates value of review
    Given User 1 is logged in
    When User 1 reviews wine #1 with tags [2011, Riesling, Germany]
    And rates it a 3 and submits the review
    And later edits the review for wine #1 with tags [2011, Riesling, Germany]
    And selects the tags [2011]
    And rates it a 1 and submits the review
    Then User 1 relationship with the tags [2011] is -2
    And User 1 relationship with the tags [Riesling, Germany] is 0
    And User 1 has 1 review with wine #1 with description ""

  Scenario: User deletes a review
    Given User 1 is logged in
    When User 1 reviews wine #1 with tags [2011, Riesling, Germany]
    And rates it a 3 and submits the review
    And User 1 deletes the review for wine #1
    Then User 1 relationship with the tags [2011, Riesling, Germany] is 0
    And User 1 has no review with wine #1

  Scenario: User views a review with no tags selected
    Given User 1 is logged in
    When User 1 reviews wine #1 with tags [2011, Riesling, Germany]
    And enters the description "I like this wine"
    And rates it a 3 and submits the review
    Then User 1 can see the review for wine #1 in the review page
    And 3 out of five stars are filled
    And no tags are indicated to be liked
    And description says "I like this wine"
    
  Scenario: User views a review with a few tags selected
    Given User 1 is logged in
    When User 1 reviews wine #1 with tags [2011, Riesling, Germany]
    And selects the tags [2011]
    And enters the description "I like this wine"
    And rates it a 3 and submits the review
    Then User 1 can see the review for wine #1 in the review page
    And tags liked are [2011]
    And 3 out of five stars are filled

  Scenario: Editing a review will update the review display
    Given User 1 is logged in
    When User 1 reviews wine #1 with tags [2011, Riesling, Germany]
    And rates it a 3 and submits the review
    And later edits the review for wine #1 with tags [2011, Riesling, Germany]
    And selects the tags [2011]
    And rates it a 5 and submits the review
    Then User 1 can see the review for wine #1 in the review page
    And tags liked are [2011]
    And 5 out of five stars are filled
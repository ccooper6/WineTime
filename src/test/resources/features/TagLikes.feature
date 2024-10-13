# Created by caleb at 10/10/2024
Feature: Liking/Disliking a tag or multiple tags (AT_25)
  Scenario: CT_Tag_1: Liking a tag
    Given The user with name "name", username "username" and password "password1" is currently logged in. Their user id is 1
    When The user is in the wine logging popup with the wine with id 1 which has the tags [Red Wine, France, Sweet]
    And The user likes or dislikes the tag "Red Wine" with a review rating of 5
    Then The "Red Wine" tag is liked or disliked successfully within the database

  Scenario: CT_Tag_2: Disliking a tag (AT_25)
    Given The user with name "name", username "username" and password "password1" is currently logged in. Their user id is 1
    When The user is in the wine logging popup with the wine with id 1 which has the tags [Red Wine, France, Sweet]
    And The user likes or dislikes the tag "Red Wine" with a review rating of 1
    Then The "Red Wine" tag is liked or disliked successfully within the database

  Scenario: CT_Tag_3: Liking no tags (AT_25)
    Given The user with name "name", username "username" and password "password1" is currently logged in. Their user id is 1
    When The user is in the wine logging popup with the wine with id 2 which has the tags [White Wine Germany, Bitter]
    And The user likes no tags with a review rating of 5
    Then All the tags are liked successfully within the database

  Scenario: CT_Tag_4: Liking multiple tags (AT_25)
    Given The user with name "name", username "username" and password "password1" is currently logged in. Their user id is 1
    When The user is in the wine logging popup with the wine with id 3 which has the tags [CalebIsCool, Italy, 2006]
    And The user likes the tags [CalebIsCool, Italy] with a review rating of 5
    Then Only the liked tags are liked successfully within the database

  Scenario: CT_Tag_5: First liking no tags, then editing the review to only select one tag (AT_32)
    Given The user with name "name", username "username" and password "password1" is currently logged in. Their user id is 1
    When The user is in the wine logging popup with the wine with id 4 which has the tags [CalebIsCool, Italy, 1969, Cooooool]
    And The user likes no tags with a review rating of 5
      # simulate editing the review
    And The user edits to only like the tags [CalebIsCool, Cooooool] with a review rating of 5
    Then Only the liked tags are liked successfully within the database

  Scenario: CT_Tag_6: First liking tags, then editing the review to only select no tags (AT_32)
    Given The user with name "name", username "username" and password "password1" is currently logged in. Their user id is 1
    When The user is in the wine logging popup with the wine with id 4 which has the tags [CalebIsCool, Italy, 1969, Cooooool]
    And The user likes the tags [CalebIsCool, Cooooool] with a review rating of 5
      # simulate editing the review
    And The user likes no tags with a review rating of 5
    Then All the tags are liked successfully within the database
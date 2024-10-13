Feature: Quiz result feature
  Scenario: CT_Quiz_1: User has completed the quiz answering only first option, leading to a wine between 1990 and 1999 from the US (AT_42)
    Given The user has completed the quiz with answers 1, 1, 1, 1, 1
    When The user gets their recommended wine
    Then The wine matches the users answers - vintage is between 1990 and 1999 and country is "US"

  Scenario: CT_Quiz_2: User has completed the quiz answering only second option, leading to a wine between 2000 and 2004 from New Zealand (AT_42)
    Given The user has completed the quiz with answers 2, 2, 2, 2, 2
    When The user gets their recommended wine
    Then The wine matches the users answers - vintage is between 2000 and 2004 and country is "New Zealand"

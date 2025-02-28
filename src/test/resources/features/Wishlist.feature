Feature: Adding and removing wines from the wishlist
  Scenario: CT_WishList_1: The user has no wines in their wishlist and adds a wine to their wishlist (AT_35)
    Given The user 1 is logged in and has 0 wines in their wishlist
    When The user 1 adds a wine 1 to their wishlist
    Then The user 1 wishlist will be 1 long, and contain the wine 1

  Scenario: CT_WishList_2: The user has 1 wine in their wishlist and removes it (AT_37)
    Given The user 1 is logged in and has 1 wines in their wishlist, with wine 1 in their wishlist
    When The user 1 removes a wine 1 from their wishlist
    Then The user 1 wishlist will be 0 long

  Scenario: CT_WishList_3: The user has a wishlist and adds multiple wines to it (AT_35)
    Given The user 1 is logged in and has wine 1, wine 2, wine 3 to their wishlist
    When The user 1 gets their wishlist
    Then The user 1 wishlist will be of length 3 and contain wine 1, wine 2, wine 3

  Scenario: CT_WishList_4: There are two users in the database, and one users wishlist doesn't effect the other ones wishlist (AT_35)
    Given The users 1 and 2 are logged in with empty wishlists
    When The user 1 adds wine 1 to their wishlist and user 2 adds wine 2 to their wishlist
    Then User 1 wishlist will only contain wine 1 and user 2 wishlist will only contain wine 2

  Scenario: CT_WishList_5: There are two users in the database both have the same wine in their wishlist (AT_37)
    Given Both users 1 and 2 have the same wine 1 in their wishlist
    When User 1 removes wine 1 from their wishlist
    Then User 2 will still have wine 1 in their wishlist and User 1 won't
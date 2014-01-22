Feature: Navigation

  Scenario: When the application starts the first selected section should be Ads
    Given My app is running
	Then the Action Bar title should be "Ads"

  Scenario: Pressing Action Bar's home button should open a navigation drawer
    Given My app is running
    When I touch the "Ads" text
    Then I wait for 2 seconds
    And the navigation drawer should be open

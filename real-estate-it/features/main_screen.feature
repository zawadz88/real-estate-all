Feature: Navigation

  Scenario: When the application starts the first selected section should be Ads
    Given My app is running
	Then I should see "Ads"
	And take picture

  Scenario: Pressing Action Bar's home button should open a navigation drawer
    Given My app is running
    When I touch the "Ads" text
    Then I wait for 2 seconds
    And the view with id "drawer_layout" should have property "drawerOpen" = "true"
	And take picture

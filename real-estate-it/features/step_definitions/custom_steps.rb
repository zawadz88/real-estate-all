Then /^the navigation drawer should be open$/ do
  performAction( 'assert_view_property', 'drawer_layout', 'drawerOpen', 'true' )
end

Then /^the Action Bar title should be "([^\"]*)"$/ do |text|
  title = performAction('call_activity_method', 'getActionBarTitle')['bonusInformation'][0]
  raise "The Action Bar title is #{title}" unless( text == title )
end
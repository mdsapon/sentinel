#Author: Doug Noël
@example @dropdown
Feature: Dropdown Tests
  Testing the Table Page Object Functionality
	
  @22 @PrimeNG
  Scenario: 22 Prime NG Dropdown
    Given I am on the Prime NG Dropdown Page
    When I select New York from the City Dropdown
    	And I verify the City Dropdown has the text "New York" selected
    	And I select the 2nd option from the City Dropdown
    Then I verify the City Dropdown has the text "Rome" selected
      And I verify the City Dropdown has the value selected for the City Dropdown
      But I verify the City Dropdown does not have the text "New York" selected

  @84 @MaterialUI
  Scenario: 84 Material UI Select
    Given I am on the Material UI Select Page
    When I select Twenty from the Age Dropdown
    	And I verify the Age Dropdown has the text "Twenty" selected
    	And I select the 3rd option from the Age Dropdown
    Then I verify the Age Dropdown has the text "Thirty" selected
      And I verify the Age Dropdown has the value selected for the Age Dropdown
      But I verify the Age Dropdown does not have the text "Twenty" selected
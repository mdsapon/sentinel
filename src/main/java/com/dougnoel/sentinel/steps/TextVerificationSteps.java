package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static com.dougnoel.sentinel.elements.ElementFunctions.getElementAsSelectElement;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.cucumber.java.en.Then;

public class TextVerificationSteps {
	
	private static final Logger log = LogManager.getLogger(TextVerificationSteps.class.getName()); // Create a logger.
	
    /**
     * Verifies the element has text by asserting item contains text
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the title header is not empty</li>
     * <li>I verify the textbox is empty</li>
     * <li>I verify the Provider dropdown is not empty</li>
     * </ul>
     * @param elementName String name of the element to verify.
     * @param assertion Sting if this is not empty, we expect it to be true.
     */
    @Then("^I verify (?:the|a|an) (.*?)( is not)?(?: is)? empty?$")
    public static void verifyElementTextIsEmpty(String elementName, String assertion) {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = SentinelStringUtils.format("Expected the element {} to {}be empty.",
                elementName, (negate ? "not " : ""));
        if (negate) {
            assertFalse(expectedResult, getElement(elementName).getText().isEmpty());
        } else {
            assertTrue(expectedResult, getElement(elementName).getText().isEmpty());
        }
    }
    
    /**
     * Used to verify that an element contains certain text. It takes an element
     * name and then an optional "does not", which if present means that the method
     * will look for the text to not exist. Then it will look for the words has|have
     * to do an exact match or contain(s) to do a partial match. It uses the text
     * contained in double quotes for matching.
     * <p>
     * NOTE: If "URL" (without the quotes) is passed in all caps in place of the
     * element name, This step will check for the text to exist in the current page
     * URL.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the header div contains the text "Header"</li>
     * <li>I verify the state dropdown does not contain the text "VA"</li>
     * <li>I verify the paragraph div has the text "This is my example text."</li>
     * <li>I verify the street name textbox does not have the text "P.O. Box"</li>
     * <li>I verify the url http://google.com has the text "google"</li>
     * </ul>
     * 
     * @param elementName String The name of the element to be evaluated as defined in the page object.
     * @param assertion String Evaluated as a boolean, where null = false and any text = true.
     * @param matchType String whether we are doing an exact match or a partial match
     * @param text String The text to verify exists in the element.
     */
    @Then("^I verify the (.*?)( does not)? (has|have|contains?) the text \"([^\"]*)\"$")
    public static void verifyElementTextContains(String elementName, String assertion, String matchType, String text) {
        boolean negate = !StringUtils.isEmpty(assertion);
        String negateText = negate ? "not " : "";
        boolean partialMatch = matchType.contains("contain");
        String partialMatchText = partialMatch ? "contain" : "exactly match";
        
        if (elementName.contains("URL")) {
            verifyURLTextContains(text);
        } else {
            String elementText = getElement(elementName).getText();
            String expectedResult = SentinelStringUtils.format(
                    "Expected the {} element to {}{} the text {}. The element contained the text: {}",
                    elementName, negateText, partialMatchText, text, elementText
                            .replace("\n", " "));
            log.trace(expectedResult);
            if (partialMatch) {
                if (negate) {
                    assertFalse(expectedResult, elementText.contains(text));
                } else {
                    assertTrue(expectedResult, elementText.contains(text));
                }
            } else {
                if (negate) {
                    assertFalse(expectedResult, StringUtils.equals(elementText, text));
                } else {
                    assertTrue(expectedResult, StringUtils.equals(elementText, text));
                }
            }
        }
    }
    
    /**
     * Helper function for i_verify_the_element_contains_the_text() for checking
     * text in the URL.
	 *
     * @param text String text to verify in the URL
     */
    private static void verifyURLTextContains(String text) {
        String currentUrl = PageManager.getCurrentUrl();
        String expectedResult = SentinelStringUtils.format("Expected the URL {} to contain the text \"{}\".", currentUrl, text);
        log.trace(expectedResult);
        assertTrue(expectedResult, currentUrl.contains(text));
    }
    
    /**
     * Used to verify that the selected option of a select element contains certain
     * text. It takes an element name and then an optional "does not", which if
     * present means that the method will look for the text to not exist. Then it
     * will look for any of the words has|have and uses the text contained in double
     * quotes for matching.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the City Dropdown has the text "New York" selected</li>
     * <li>I verify the Area Select Box has the text "This is my example text."
     * selected</li>
     * <li>I verify the cola radio button does not have the text "Root beer"
     * selected</li>
     * </ul>
     * 
     * @param elementName String Name of the Element to verify
     * @param assertion String if empty we expect this to be false
     * @param textToMatch String Text to match
     */
    @Then("^I verify the (.*?)( does not)? (?:has|have) the text \"([^\"]*)\" selected$")
    public static void verifySelectionTextContains(String elementName, String assertion, String textToMatch) {
        boolean negate = !StringUtils.isEmpty(assertion);
        String selectedText = getElementAsSelectElement(elementName).getSelectedText();
        String expectedResult = SentinelStringUtils.format(
                "Expected the the selection for the {} element to {}contain the text \"{}\". The element contained the text: \"{}\".",
                elementName, (negate ? "not " : ""), textToMatch, selectedText.replace("\n", " "));
        log.trace(expectedResult);
        if (negate) {
            assertFalse(expectedResult, selectedText.contains(textToMatch));
        } else {
            assertTrue(expectedResult, selectedText.contains(textToMatch));
        }
    }
    
    /**
     * Used to verify that the selected option of a select element contains the text used
     * in an element. It takes an element name and then an optional "does not", which if
     * present means that the method will look for the text to not exist. Then it
     * will look up the value set for the second element. This second element may be the 
     * same element or a different one. For example, you might select the first item in a
     * drop down and not know what you selected. Using this method, you can check to make
     * sure that the value appears correctly. Alternately, you might enter a value in a
     * text box and want to verify that it was programmatically selected in a drop down.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the City Dropdown has the value selected for the City Dropdown</li>
     * <li>I verify the City Dropdown has the value entered for the City Field</li>
     * <li>I verify the Shipping City Dropdown does not have the value used for the Billing City Dropdown</li>
     * </ul>
     * 
     * @param elementName String Name of the Element to verify
     * @param assertion String if empty we expect this to be false
     * @param key String the key to retrieve the text to match from the configuration manager
     */
    @Then("^I verify the (.*?)( does not)? (?:has|have) the value (?:entered|selected|used) for the (.*?)$")
    public static void verifySelectionTextContainsStoredValue(String elementName, String assertion, String key) {
        String textToMatch = ConfigurationManager.getValue(key);
        boolean negate = !StringUtils.isEmpty(assertion);
        String selectedText = getElementAsSelectElement(elementName).getSelectedText();
        String expectedResult = SentinelStringUtils.format(
                "Expected the the selection for the {} element to {}contain the text \"{}\". The element contained the text: \"{}\".",
                elementName, (negate ? "not " : ""), textToMatch, selectedText.replace("\n", " "));
        log.trace(expectedResult);
        if (negate) {
            assertFalse(expectedResult, selectedText.contains(textToMatch));
        } else {
            assertTrue(expectedResult, selectedText.contains(textToMatch));
        }
    }
}

package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

import io.cucumber.java.en.Then;

/**
 * Methods used to defined basic validations
 *
 */
public class VerificationSteps {
	
    private static final Logger log = LogManager.getLogger(VerificationSteps.class.getName()); // Create a logger.
    
    /**
     * Verifies the given element exists. The given element string is made lower case 
     * and whitespaces are replaced with underscores, then it is sent the isDisplayed
     * event and returns true or false. It also has an assertion variable which is set
     * if the words "does not" are used, and it looks for a negative assertion. The word 
     * "does" can be used optionally, and was added to support the use of a Scenario Outline which
     * switches between "does" and "does not" in its tests.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify a submit button exists</li>
     * <li>I verify the Plan Dropdown does not exist</li>
     * <li>I verify an area map does exist</li>
     * </ul>
     * <b>Scenario Outline Example:</b>
     * <p>
     * I verify the &lt;element&gt; &lt;Assertion&gt; exists
     * <p>
     * Examples:<br>
     *   | element         | Assertion |<br>
     *   | first dropdown  | does      |<br>
     *   | second dropdown | does not  |
     * 
     * @param elementName String Element to check
     * @param assertion String "does" or does not" for true or false
     */
    @Then("^I verify (?:the|a|an) (.*?)( does not)?(?: does)? exists?$")
    public static void verifyElementExists(String elementName, String assertion) {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = SentinelStringUtils.format("Expected the element {} to {}exist.",
                elementName, (negate ? "not " : ""));
        if (negate) {
            // We need a different assertion here because checking to see if something does
            // exist takes 10 seconds to come back with a failure when we want it to come
            // back much faster.
            assertTrue(expectedResult, getElement(elementName).doesNotExist());
        } else {
            assertTrue(expectedResult, getElement(elementName).isDisplayed());
        }
    }
    
    /**
     * Verifies the whether the given element is selected or not. Intended to be used with check boxes and radio buttons.
     * Using the optional word not will check to make sure the item is not checked/selected.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the Yes check box is checked</li>
     * <li>I verify the Female Radio Button is selected</li>
     * <li>I verify the I would like fries with that Check box is not selected</li>
     * </ul>
     * @param elementName String Element to check
     * @param assertion String "" or " not" for true or false
     */
    @Then("^I verify the (.*) is( not)? (?:checked|selected)$")
    public static void verifyElementIsSelected(String elementName, String assertion) {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = SentinelStringUtils.format("Expected the element {} to {} selected.",
                elementName, (negate ? "not be" : "be"));
        if (negate) {
            assertFalse(expectedResult, getElement(elementName).isSelected());
        } else {
            assertTrue(expectedResult, getElement(elementName).isSelected());
        }
    }
    
    /**
     * Verifies an element has an attribute by asserting the element for the given elementName has the given class attribute.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the table has the attribute "table-striped"</li>
     * <li>I verify the textbox does not have the attribute "listStyleClass"</li>
     * <li>I verify the div has the attribute "container"</li>
     * </ul>
     * @param elementName String element to inspect
     * @param assertion String if not null we expect this be true
     * @param attribute String class attribute to verify
     */
    @Then("^I verify (?:the|a|an) (.*) (?:has)?(does not have)? the attribute (.*)$")
    public static void verifyElementHasAttribute(String elementName, String assertion, String attribute) {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = SentinelStringUtils.format("Expected the element {} to {}have the attribute \"{}\".",
                elementName, (negate ? "" : "not "), attribute);
        log.trace(expectedResult);
        if (negate) {
            assertFalse(expectedResult, getElement(elementName).hasClass(attribute));
        } else {
            assertTrue(expectedResult, getElement(elementName).hasClass(attribute));
        }
    }
    
    /**
     * Verifies an element is active by asserting the element for the given element name has class attribute 'active'
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the menu item for the current page is active</li>
     * <li>I verify the second li element in the accordion is active</li>
     * <li>I verify the password field is not active</li>
     * </ul>
     * @param elementName String  the element to inspect for the active class
     * @param assertion String if not null we expect this be true
     */
    @Then("^I verify (?:the|a|an) (.*) is( not)? active$")
    public static void verifyElementIsActive(String elementName, String assertion) {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = SentinelStringUtils.format("Expected the element {} to {}be active.",
                elementName, (negate ? "" : "not "));
        log.trace(expectedResult);
        if (negate) {
            assertFalse(expectedResult, getElement(elementName).hasClass("active"));
        } else {
            assertTrue(expectedResult, getElement(elementName).hasClass("active"));
        }
    }
    
    /**
     * Verifies an element is enabled by asserting the element found for the given element name is enabled
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the submit button is not enabled</li>
     * <li>I verify the home link is enabled</li>
     * <li>I verify the link to open a pdf is enabled</li>
     * </ul>
     * @param elementName String the name of the element
     * @param assertion String if not null we expect this be true
     */
    @Then("^I verify (?:the|a|an) (.*?) is( not)? enabled$")
    public static void verifyElementIsEnabled(String elementName, String assertion) {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = SentinelStringUtils.format("Expected the element {} to {}be enabled.",
                elementName, (negate ? "not " : ""));
        log.trace(expectedResult);
        int waitTime = (negate) ? 1 : 10; // If we expect it to fail, only wait a second, otherwise wait the normal 10
                                          // seconds TODO: Make this even shorter than a second.
        assertTrue(expectedResult, negate != getElement(elementName).isEnabled(waitTime));
    }
    
    /**
     * Verifies an element is enabled by asserting the element for the given element name is hidden
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the iframe is hidden</li>
     * <li>I verify the document link is not hidden</li>
     * <li>I verify the data for the user's current plan is not hidden</li>
     * </ul>
     * @param elementName String the name of the element
     * @param assertion String if the assertion is not empty we expect it to be hidden
     */
    @Then("^I verify (?:the|a|an) (.*?) is( not)? hidden$")
    public static void verifyElementIsHidden(String elementName, String assertion) {
        boolean negate = !StringUtils.isEmpty(assertion); // is hidden = empty, so negate is false
        String expectedResult = SentinelStringUtils.format("Expected the element {} to be {}.", elementName, (negate ? "visible"
                : "hidden"));
        log.debug(expectedResult);
        int waitTime = (negate) ? 10 : 1; // If we expect it to fail, only wait a second, otherwise wait the normal 10
                                          // seconds
        assertTrue(expectedResult, negate == getElement(elementName).isDisplayed(waitTime));
    }
    
    /**
     * Redirects to the given pageName
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I am redirected to the home page</li>
     * <li>I am redirected to the login page</li>
     * <li>I am shown the member portal pop-up overlay</li>
     * </ul>
     * @param pageName String the name of the place to redirect to
     * @throws InterruptedException if the thread gets interrupted while sleeping
     */
    @Then("^I am (?:redirected to|shown) the (.*) (?:(?:P|p)age|(?:O|o)verlay)$")
    public static void redirectedToPage(String pageName) throws InterruptedException {
        pageName = pageName.replaceAll("\\s", "") + "Page";
        PageManager.setPage(pageName);
        PageManager.waitForPageLoad();
    }
    
    /**
     * Opens the given pageName in a new window
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I see a new tab open with the Home page</li>
     * <li>I see a new tab open with the Login page</li>
     * <li>I see a new tab open with the Google Maps page</li>
     * </ul>
     * @param pageName String the page to open
     * @throws InterruptedException if the thread gets interrupted while sleeping
     */
    @Then("^I see a new tab open with the (.*) Page$")
    public static void openNewTab(String pageName) throws InterruptedException {
        PageManager.switchToNewWindow();
        pageName = pageName.replaceAll("\\s", "") + "Page";
        PageManager.setPage(pageName);
        PageManager.waitForPageLoad();
    }
       
    /**
     * Verifies that a URL loads in a new window when a named link is clicked.|<p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I open the google link in a new tab and verify the url that loads is http://google.com</li>
     * <li>I open the ESPN link in a new tab for verfiy the url is http://go.espn.com</li>
     * <li>I open the Pharmacy Benefits link in a new window and verify the url is https://testsite.com/pharamacy_benefits</li>
     * </ul>
     * @param linkName String The text of the link. NOT a PageElement object name.
     * @param url String the URL expected to be loaded.
     */
    @Then("^I open the (.*?) link in a new tab and verify the URL that loads is (.*?)$")
    public static void openLinkAndVerifyURL(String linkName, String url) {
            WebDriverFactory.getWebDriver().findElement(By.linkText(linkName)).sendKeys(Keys.RETURN);
            PageManager.switchToNewWindow();
            String newUrl = PageManager.getCurrentUrl();
            assertTrue(newUrl.contains(url));
    }
    
    /**
     * Identifies an iframe and switches to it, if it exists.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I should see the Third Party iframe</li>
     * <li>I should see the Provider Search iframe</li>
     * <li>I should see the Facebook iframe</li>
     * </ul>
     * @see com.dougnoel.sentinel.steps.VerificationSteps#verifyElementExists(String, String)
     * @see com.dougnoel.sentinel.pages.PageManager#switchToIFrame()
     * @param iFrameName String the name of the iframe element on the page object.
     */
    @Then("^I should see the (.*) iframe$")
    public static void switchToIFrame(String iFrameName) {

        // Make sure the content is loaded in the i frame
        VerificationSteps.verifyElementExists(iFrameName, "");
        PageManager.switchToIFrame();

    }
}
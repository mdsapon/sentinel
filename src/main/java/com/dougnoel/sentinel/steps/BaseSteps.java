package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.configurations.TimeoutManager;
import com.dougnoel.sentinel.exceptions.SentinelException;
import com.dougnoel.sentinel.pages.PageManager;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

/**
 * Methods used to define basic operations. Other step files can extend or
 * include this one to leverage these actions.
 * 
 * Functionality in this class includes clicking given elements, entering text, selecting items, verifying elements exist, 
 * contains given text, or are active, enabled, or hidden, verifying table columns or table rows have given text, 
 * unique text, text for stored values, navigating results, 
 * and basic browser interaction.
 */
public class BaseSteps {
    private static final Logger log = LogManager.getLogger(BaseSteps.class.getName()); // Create a logger.
    
    @Before
    public static void before(Scenario scenario) {
        log.trace("Scenario ID: {} Scenario Name: {}", scenario.getId(), scenario.getName());
    }
    
    /**
     * Clicks the element that matches the given elementName as defined on the current Page object. The page object and driver object are defined by the
     * WebDriverFactory and PageFactory objects. The derived Page Object (extends
     * Page) should define a method named [element name]_[element type] returning a PageElement object (e.g. login_button).
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I click a login button</li>
     * <li>I click the Login button</li>
     * <li>I click an Operation Button</li>
     * </ul>
     * 
     * @param elementName String the name of the element to click
     */
    @When("^I click (?:the|a|an) (.*?)$")
    public static void click(String elementName) {
        getElement(elementName).click();
    }

    /**
     * Waits for the sum of the given number of seconds and fractions of sections.
     * This step is used to add in an implicit wait time using a Cucumber step.
     * Ideally this should be used to determine if you just need to wait for
     * something to load. Once that is determined, you should be fixing the
     * following method to improve its own wait functionality. In the cases where
     * you have to impose a wait, you should use the smallest amount of time
     * possible. This method can wait as small amount a time as 1 millisecond using
     * decimals. This method deals with whole numbers as well as fractions of a
     * second using decimals. Furthermore, you can put a reason at the end of the
     * step for clarity and the method will ignore that extra text when matching.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I wait 1 second</li>
     * <li>I wait 3 seconds</li>
     * <li>I wait 0.2 seconds</li>
     * <li>I wait 0.5 seconds before continuing</li>
     * <li>I wait 0.001 second to ensure the element is visible before checking
     * it</li>
     * </ul>
     * 
     * @param seconds double the number of seconds to wait
     */
    @When("^I wait (\\d{1,2}(?:[.,]\\d{1,4})?) seconds?(?:.*)$")
    public static void wait(double seconds) {
    	Double totalWaitTime = Double.valueOf(ConfigurationManager.getValue("totalWaitTime"));
    	ConfigurationManager.setValue("totalWaitTime", String.valueOf(seconds + totalWaitTime));
        TimeoutManager.wait(seconds);
        log.warn("Passed {} seconds, waiting {} milliseconds. Waits should only be used for special circumstances. If you are seeing this message a lot then you should probably be logging a bug ticket to get the framework fixed at: http://https://github.com/dougnoel/sentinel/issues", seconds, (seconds * 1000));
    }

    /**
     * Loads a page based on the environment you are currently testing. The url is set in the page object yaml file.
     * Refer to the documentation in the sentinel.example project for more information. You cannot load a URL
     * directly, because once there you would not be able to do anything.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I navigate to the Login Page</li>
     * <li>I am on the Main page</li>
     * <li>I remain on the popup page</li>
     * <li>I navigate to the Documents page using the argument ?docYear=2003</li>
     * <li>I am on the Home Page using the arguments ?firstname=bob{@literal &}lastname=smith</li>
     * </ul>
     * @param pageName String Page Object Name
     * @param hasArguments boolean indicates whether there is a query string to add to the usual URL
     * @param arguments String the literal string to append to the default URL.
     * @throws SentinelException this exists so that any uncaught exceptions result in the test failing
     */
    @Given("^I (?:navigate to|am on|remain on) the (.*?) (?:P|p)age( using the arguments? )?(.*?)?$")
    public static void navigateToPage(String pageName, String hasArguments, String arguments) throws SentinelException  {   	
    	pageName = pageName.replaceAll("\\s", "") + "Page";
        PageManager.setPage(pageName);
        String baseUrl = ConfigurationManager.getUrl();
        if (hasArguments != null) {
            baseUrl += arguments;
        }
        log.debug("Loading {} for the {} in the {} environment.", baseUrl, pageName, ConfigurationManager.getEnvironment());
        PageManager.openPage(baseUrl);
    }
    
    /**
     * Overloaded method for navigating to a page object's url based on the current environment, 
     * so that nulls do not need to be passed. Intended for use in creating complex Cucumber steps definitions.
     * @param pageName String Page Object Name
     * @throws SentinelException this exists so that any uncaught exceptions result in the test failing
     */
    public static void naviagteToPage(String pageName) throws SentinelException {
    	navigateToPage(pageName, null, null);
    }
    
    /**
     * Overloaded method for navigating to a page object's url based on the current environment, and taking a 
     * query string argument it will append to the request. Used so that nulls do not need to be passed. Intended 
     * for use in creating complex Cucumber steps definitions.
     * @param pageName String Page Object Name
     * @param arguments String the literal string to append to the default URL.
     * @throws SentinelException this exists so that any uncaught exceptions result in the test failing
     */
    public static void naviagteToPage(String pageName, String arguments) throws SentinelException {
    	navigateToPage(pageName, "has arguments", arguments);
    }
 
    /**
     * Closes the child browser tab or window
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I close the browser tab</li>
     * <li>I close the browser window"</li>
     * </ul>
     */
    @When("^I close the browser (?:tab|window)$")
    public static void closeBrowserWindow() {
        PageManager.closeChildWindow();
    }
    
    /**
     * Navigates through basic browser actions: forward, back and refresh
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I press the browser forward button</li>
     * <li>I press the browser refresh button</li>
     * <li>I press the browser back button</li>
     * </ul>
     * @see com.dougnoel.sentinel.pages.PageManager#navigateBack()
     * @see com.dougnoel.sentinel.pages.PageManager#navigateForward()
     * @see com.dougnoel.sentinel.pages.PageManager#refresh()
     * @param option String the browser action
     */
    @When("^I press the browser (back|forward|refresh) button$")
    public static void pressBrowserButton(String option) {
        switch (option) {
        case "back":
            PageManager.navigateBack();
            break;
        case "forward":
            PageManager.navigateForward();
            break;
        default:
            PageManager.refresh();
            break;
        }
    }
    
}
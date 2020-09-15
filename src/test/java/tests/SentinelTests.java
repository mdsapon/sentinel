package tests;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.dougnoel.sentinel.exceptions.SentinelException;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true
	, features = "src/test/java/features"
	, glue = { "stepdefinitions", "com.dougnoel.sentinel.steps" }
	, plugin = {"json:target/cucumber.json",
			"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
	, strict = true
//    , tags = { "not @new-tours" }
    , tags = { "@MaterialUI" }
)

public class SentinelTests {
    private static final Logger log = LogManager.getLogger(SentinelTests.class); // Create a logger.
    
    @BeforeClass
    public static void setUpBeforeClass() throws IOException, SentinelException {
        System.setProperty("org.freemarker.loggerLibrary", "none");
        System.setProperty("env", "dev"); // Set the environment (dev/qa/stage/prod/etc)
        System.setProperty("PrimeNGDropdown.getOption.ByString", "//li[@aria-label=\"{}\"]");
        System.setProperty("PrimeNGDropdown.getOption.ByIndex", "//p-dropdownitem[{}]/li");
        System.setProperty("PrimeNGDropdown.getOption.GetText", "aria-label");
        System.setProperty("PrimeNGDropdown.getOption.GetSelectedText", "//span[contains(@class, 'p-dropdown-label')]");
        System.setProperty("MaterialUISelect.getOption.ByString", "//div[contains(@class, 'MuiPopover-root')]/div[3]/ul/li[contains(text(),'{}')]");
        System.setProperty("MaterialUISelect.getOption.ByIndex", "//div[contains(@class, 'MuiPopover-root')]/div[3]/ul/li[{}]");
        WebDriverFactory.instantiateWebDriver();
    }

    @AfterClass
    public static void tearDownAfterClass() throws SentinelException {
        log.info("Driver: {}", WebDriverFactory.getWebDriver());
        if (System.getProperty("leaveBrowserOpen", "false") == "false") {
        	PageManager.quit();
        }
    }
}
package tests;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.exceptions.SentinelException;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true
	, features = "src/test/java/features"
	, glue = { "stepdefinitions", "com.dougnoel.sentinel.steps", "steps" }
	, plugin = {"json:target/cucumber.json",
			"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
	, strict = true
//  , tags = { "@22" }
)

public class SentinelTests {
    private static final Logger log = LogManager.getLogger(SentinelTests.class); // Create a logger.
    
    @BeforeClass
    public static void setUpBeforeClass() throws IOException, SentinelException {
        WebDriverFactory.instantiateWebDriver();
    }

    @AfterClass
    public static void tearDownAfterClass() throws SentinelException {
        String totalWaitTime = ConfigurationManager.getValue("totalWaitTime");
        if (totalWaitTime != null) {
        	log.warn("This test took {} total seconds longer due to explicit waits. Sentinel handles dynamic waits. If you have a reason for adding explicit waits, you should probably be logging a bug ticket to get the framework fixed at: http://https://github.com/dougnoel/sentinel/issues");
        }
    	log.info("Driver: {}", WebDriverFactory.getWebDriver());
        if (System.getProperty("leaveBrowserOpen", "false") == "false") {
        	PageManager.quit();
        }
    }
}
package com.dougnoel.sentinel.pages;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.elements.Checkbox;
import com.dougnoel.sentinel.elements.PageElement;
import com.dougnoel.sentinel.elements.Textbox;
import com.dougnoel.sentinel.elements.dropdowns.Dropdown;
import com.dougnoel.sentinel.elements.dropdowns.MaterialUISelect;
import com.dougnoel.sentinel.elements.dropdowns.PrimeNGDropdown;
import com.dougnoel.sentinel.elements.dropdowns.SelectElement;
import com.dougnoel.sentinel.elements.radiobuttons.PrimeNGRadioButton;
import com.dougnoel.sentinel.elements.radiobuttons.Radiobutton;
import com.dougnoel.sentinel.elements.tables.NGXDataTable;
import com.dougnoel.sentinel.elements.tables.Table;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.exceptions.ElementNotFoundException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

/**
 * Page class to contain a URL and the elements on the page.
 * <p>
 * <b>TO DO:</b>
 * <ul>
 * <li>Turn this into an abstract class.</li>
 * <li>Create a PageFactory</li>
 * <li>Abstract out the driver creation to allow multiple drivers to be created
 * at once</li>
 * </ul>
 */
public class Page {
	
	protected static final SelectorType CLASS = SelectorType.CLASS;
	protected static final SelectorType CSS = SelectorType.CSS;
	protected static final SelectorType ID = SelectorType.ID;
	protected static final SelectorType NAME = SelectorType.NAME;
	protected static final SelectorType PARTIALTEXT = SelectorType.PARTIALTEXT;
	protected static final SelectorType TEXT = SelectorType.TEXT;
	protected static final SelectorType XPATH = SelectorType.XPATH;    

    protected WebDriver driver;

    protected URL url;
    
    protected Map<String,PageElement> elements;
    
    private String pageName;

    /**
     * Initializes a WebDriver object for operating on page elements, and sets the
     * base URL for the page.
     */
    public Page() {
    	this.pageName = this.getClass().getSimpleName();
        driver = WebDriverFactory.getWebDriver();
        elements = new HashMap<>();
    }
    
    public Page(String pageName) {
    	this.pageName = pageName;
        driver = WebDriverFactory.getWebDriver();
        elements = new HashMap<>();
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getName() {
        return pageName;
    }

	public PageElement getElement(String elementName) {
        String normalizedName = elementName.replaceAll("\\s+", "_").toLowerCase();
        return elements.computeIfAbsent(normalizedName, name -> createElement(name));
	}
	
	private Map<String, String> findElement(String elementName, String pageName) {
		Map<String, String> elementData = ConfigurationManager.getElement(elementName, pageName);
		if (elementData == null) {
			for (String page : ConfigurationManager.getPageParts(pageName)) {
				elementData = findElement(elementName, page);
				if (elementData != null) {
					break;
				}
			}
		}
		return elementData;
	}
	
	private PageElement createElement(String elementName) {
		Map<String, String> elementData = findElement(elementName, getName());
		
		if (elementData == null) {
			String errorMessage = SentinelStringUtils.format("Data for the element {} could not be found in the {}.yml file.", elementName, this.getName());
			throw new ElementNotFoundException(errorMessage);
		}
		
		String elementType = null;
		if (elementData.containsKey("elementType")) {
			elementType = elementData.get("elementType");
		}
		else {
			elementType = "Element";
		}

		if ("Checkbox".equalsIgnoreCase(elementType)) {
			return new Checkbox(elementName, elementData);
		}
		if ("Textbox".equalsIgnoreCase(elementType)) {
			return new Textbox(elementName, elementData);
		}
		if ("Dropdown".equalsIgnoreCase(elementType)) {
			return new Dropdown(elementName, elementData);
		}
		if ("MaterialUISelect".equalsIgnoreCase(elementType)) {
			return new MaterialUISelect(elementName, elementData);
		}
		if ("PrimeNGDropdown".equalsIgnoreCase(elementType)) {
			return new PrimeNGDropdown(elementName, elementData);
		}
		if ("SelectElement".equalsIgnoreCase(elementType)) {
			return new SelectElement(elementName, elementData);
		}
		if ("PrimeNGRadioButton".equalsIgnoreCase(elementType)) {
			return new PrimeNGRadioButton(elementName, elementData);
		}
		if ("Radiobutton".equalsIgnoreCase(elementType)) {
			return new Radiobutton(elementName, elementData);
		}
		if ("NGXDataTable".equalsIgnoreCase(elementType)) {
			return new NGXDataTable(elementName, elementData);
		}
		if ("Table".equalsIgnoreCase(elementType)) {
			return new Table(elementName, elementData);
		}
		// This allows people to call their element type whatever they want without needing a child class to implement it.
		return new PageElement(elementType, elementName, elementData);
	}
}

package com.dougnoel.sentinel.elements;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.elements.dropdowns.Dropdown;
import com.dougnoel.sentinel.elements.dropdowns.MaterialUISelect;
import com.dougnoel.sentinel.elements.dropdowns.PrimeNGDropdown;
import com.dougnoel.sentinel.elements.dropdowns.SelectElement;
import com.dougnoel.sentinel.elements.radiobuttons.PrimeNGRadioButton;
import com.dougnoel.sentinel.elements.radiobuttons.Radiobutton;
import com.dougnoel.sentinel.elements.tables.NGXDataTable;
import com.dougnoel.sentinel.elements.tables.Table;
import com.dougnoel.sentinel.exceptions.ElementTypeMismatchException;
import com.dougnoel.sentinel.exceptions.MalformedSelectorException;
import com.dougnoel.sentinel.exceptions.NoSuchSelectorException;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public class ElementFunctionsTests {
	private static WebDriver driver;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("env", "dev");
		driver = WebDriverFactory.instantiateWebDriver();
		PageManager.setPage("Elements");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		driver.close();
	}

	@Test
	public void createDefaultElement() {
		PageElement element = ElementFunctions.getElement("generic");
		assertTrue("Expecting PageElement class.", element instanceof PageElement);
		assertEquals("Expecting Default Name.", "generic", element.getName());
	}
	
	@Test
	public void createCheckBox() {
		PageElement element = ElementFunctions.getElementAsCheckbox("checkbox");
		assertTrue("Expecting Checkbox class.", element instanceof Checkbox);
		assertEquals("Expecting Checkbox Name.", "checkbox", element.getName());
	}
	
	@Test(expected = ElementTypeMismatchException.class)
	public void failToCreateCheckBox() {
		ElementFunctions.getElementAsCheckbox("generic");
	}

	@Test
	public void createTextBox() {
		PageElement element = ElementFunctions.getElementAsTextbox("textbox");
		assertTrue("Expecting Textbox class.", element instanceof Textbox);
		assertEquals("Expecting Textbox Name.", "textbox", element.getName());
	}
	
	@Test(expected = ElementTypeMismatchException.class)
	public void failToCreateTextBox() {
		ElementFunctions.getElementAsTextbox("generic");
	}

	@Test
	public void createDropDown() {
		PageElement element = ElementFunctions.getElementAsDropdown("dropdown");
		assertTrue("Expecting Dropdown class.", element instanceof Dropdown);
		assertEquals("Expecting Dropdown Name.", "dropdown", element.getName());
	}
	
	@Test(expected = ElementTypeMismatchException.class)
	public void failToCreateDropDown() {
		ElementFunctions.getElementAsDropdown("generic");
	}
	
	@Test
	public void createMaterialUISelect() {
		PageElement element = ElementFunctions.getElement("material_ui_select");
		assertTrue("Expecting MaterialUISelect class.", element instanceof MaterialUISelect);
		assertEquals("Expecting MaterialUISelect Name.", "material_ui_select", element.getName());
	}

	@Test
	public void createPrimeNGDropdown() {
		PageElement element = ElementFunctions.getElement("prime_ng_dropdown");
		assertTrue("Expecting PrimeNGDropdown class.", element instanceof PrimeNGDropdown);
		assertEquals("Expecting PrimeNGDropdown Name.", "prime_ng_dropdown", element.getName());
	}
	
	@Test
	public void createSelect() {
		PageElement element = ElementFunctions.getElementAsSelectElement("select");
		assertTrue("Expecting SelectElement class.", element instanceof SelectElement);
		assertEquals("Expecting SelectElement Name.", "select", element.getName());
	}
	
	@Test(expected = ElementTypeMismatchException.class)
	public void failToCreateSelect() {
		ElementFunctions.getElementAsSelectElement("generic");
	}
	
	@Test
	public void createPrimeNGRadioButton() {
		PageElement element = ElementFunctions.getElement("prime_ng_radio_button");
		assertTrue("Expecting PrimeNGRadioButton class.", element instanceof PrimeNGRadioButton);
		assertEquals("Expecting PrimeNGRadioButton Name.", "prime_ng_radio_button", element.getName());
	}
	
	@Test
	public void createRadioButton() {
		PageElement element = ElementFunctions.getElementAsRadiobutton("radiobutton");
		assertTrue("Expecting Radiobutton class.", element instanceof Radiobutton);
		assertEquals("Expecting Radiobutton Name.", "radiobutton", element.getName());
	}
	
	@Test(expected = ElementTypeMismatchException.class)
	public void failToCreateRadioButton() {
		ElementFunctions.getElementAsRadiobutton("generic");
	}
	
	@Test
	public void createNGXDataTable() {
		PageElement element = ElementFunctions.getElement("ngx_data_table");
		assertTrue("Expecting NGXDataTable class.", element instanceof NGXDataTable);
		assertEquals("Expecting NGXDataTable Name.", "ngx_data_table", element.getName());
	}
	
	@Test
	public void createTable() {
		PageElement element = ElementFunctions.getElementAsTable("table");
		assertTrue("Expecting Table class.", element instanceof Table);
		assertEquals("Expecting Table Name.", "table", element.getName());
	}
	
	@Test(expected = ElementTypeMismatchException.class)
	public void failToCreateTable() {
		ElementFunctions.getElementAsTable("generic");
	}

	@Test(expected = NoSuchSelectorException.class)
	public void badSelector() {
		ElementFunctions.getElement("bad_selector").click();
		
	}
	
	@Test(expected = MalformedSelectorException.class)
	public void creationFailure() {
		ElementFunctions.getElement("bad_element").click();
		
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.seleniumtesting;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.openqa.selenium.Keys;

/**
 *
 * @author lasse
 */
public class SeleniumtestingSuite {

	private static WebDriver WEBDRIVER;

	private static final String
		SELENIUM_DRIVER_CONFIG = "webdriver.chrome.driver",
		SELENIUM_DRIVER_PATH = "/home/lasse/Downloads/chromedriver_linux64/chromedriver",
		URI = "localhost:3000";

	@org.junit.BeforeClass
	public static void setUpClass() {
		System.setProperty(SELENIUM_DRIVER_CONFIG, SELENIUM_DRIVER_PATH);
		WEBDRIVER = new ChromeDriver();
		WEBDRIVER.get(URI);
	}

	@org.junit.AfterClass
	public static void tearDownClass() {
		WEBDRIVER.get(URI + "/reset");
		WEBDRIVER.quit();
	}

	/* Expect 5 rows in the table */
	@org.junit.Test
	public void test1() {
		WebDriverWait		wait;
		WebElement		tbody;
		List<WebElement>	rows;

		wait = new WebDriverWait(WEBDRIVER, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id")));

		tbody = WEBDRIVER.findElement(By.id("tbodycars"));
		rows = tbody.findElements(By.tagName("tr"));

		assertThat(rows.size(), is(5));
	}

	/* Write 2002 in filter text, expect two rows */
	@org.junit.Test
	public void test2() {
		WebDriverWait		wait;
		WebElement		filterInput, tbody;
		List<WebElement>	rows;
		
		wait = new WebDriverWait(WEBDRIVER, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("filter"))));
		
		filterInput = WEBDRIVER.findElement(By.id("filter"));
		filterInput.sendKeys("2002");
		
		tbody = WEBDRIVER.findElement(By.id("tbodycars"));
		rows = tbody.findElements(By.tagName("tr"));
		
		assertThat(rows.size(), is(2));
	}
	
	/* Clear input field and expect five entries in list */
	@org.junit.Test
	public void test3() {
		WebElement		element, tbody;
		List<WebElement>	rows;
		
		element = WEBDRIVER.findElement(By.id("filter"));
		element.sendKeys(Keys.CONTROL, Keys.BACK_SPACE);
		
		tbody = WEBDRIVER.findElement(By.id("tbodycars"));
		rows = tbody.findElements(By.tagName("tr"));
		
		assertThat(rows.size(), is(5));
	}
	
	/* Click sort and expect sorted table */
	@org.junit.Test
	public void test4() {
		WebElement		sorting, tbody, tableTop, tableBottom;
		List<WebElement>	rows;
		
		sorting = WEBDRIVER.findElement(By.id("h_year"));
		sorting.click();
		
		tbody = WEBDRIVER.findElement(By.id("tbodycars"));
		rows = tbody.findElements(By.tagName("tr"));
		
		tableTop = rows.get(0);
		tableBottom = rows.get(rows.size()-1);
		
		assertThat(tableTop.findElements(By.tagName("td")).get(0).getText(), is("938"));
		assertThat(tableBottom.findElements(By.tagName("td")).get(0).getText(), is("940"));
	}
	
	/* Edit bottom car to say cool car */
	@org.junit.Test
	public void test5() {
		WebElement		tbody, editLink, description, savebtn;
		List<WebElement>	rows;
		
		tbody = WEBDRIVER.findElement(By.id("tbodycars"));
		rows = tbody.findElements(By.tagName("tr"));
		
		editLink = rows.get(0).findElements(By.tagName("td")).get(7).findElements(By.tagName("a")).get(0);
		editLink.click();
		
		description = WEBDRIVER.findElement(By.id("description"));
		description.click();
		description.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
		description.sendKeys("Cool car");
		
		savebtn = WEBDRIVER.findElement(By.id("save"));
		savebtn.click();
		
		/* Retarded piece of shit resets the sort */
		
		rows = tbody.findElements(By.tagName("tr"));
		assertThat(rows.get(1).findElements(By.tagName("td")).get(5).getText(), is("Cool car"));
	}
	
	/* Try to create and save new empty car */
	@org.junit.Test
	public void test6() {
		WEBDRIVER.findElement(By.id("new")).click();
		WEBDRIVER.findElement(By.id("save")).click();
		
		assertThat(WEBDRIVER.findElement(By.id("submiterr")), is(notNullValue()));
	}
	
	/* Make new shitty entry */
	@org.junit.Test
	public void test7() {
		List<WebElement>	tbody;
		
		WEBDRIVER.findElement(By.id(("new"))).click();
		
		WEBDRIVER.findElement(By.id("year")).sendKeys("2008");
		WEBDRIVER.findElement(By.id("registered")).sendKeys("2002-5-5");
		WEBDRIVER.findElement(By.id("make")).sendKeys("Kia");
		WEBDRIVER.findElement(By.id("model")).sendKeys("Rio");
		WEBDRIVER.findElement(By.id("description")).sendKeys("As new");
		WEBDRIVER.findElement(By.id("price")).sendKeys("31000");
		
		WEBDRIVER.findElement(By.id("save")).click();
		
		tbody = WEBDRIVER.findElement(By.id("tbodycars")).findElements(By.tagName("tr"));
		assertThat(tbody.size(), is(6));
		
	}

//	@org.junit.Before
//	public void setUp() throws Exception {
//	}
//
//	@org.junit.After
//	public void tearDown() throws Exception {
//	}
}

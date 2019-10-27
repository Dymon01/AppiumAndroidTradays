package tests;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import tests.I;

@Listeners(BaseClass.class)
public class LastYearOutputTest extends BaseClass {

	@Test
	public void a_setLanguageEnglTest() throws Exception {
		TestManager.setLanguageEnglIfSelectedRus();
	}

	@Test
	public void b_setFilterTest() throws Exception {
		TestManager.setFilter("Medium", "Switzerland");
	}

	@Test
	public void c_countryImportanceCheckTest() throws Exception {
		TestManager.eventCountryImportanceCheck();
	}

	@Test
	public void d_LastYearOutputTest() throws Exception {
		TestManager.LastYearOutput();
	}

	
}

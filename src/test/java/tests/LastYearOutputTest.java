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
public class  LastYearOutputTest extends BaseClass {
	
	@Test
	public void a_setLanguageEnglTest() throws Exception {
		I.wait(15,"country_flag");
		setLanguageEnglIfSelectedRus();
	}
	
	@Test
	public void b_setFilterTest() throws Exception {
		setFilter("Medium", "Switzerland");
	}
	
	@Test
	public void c_countryImportanceCheckTest() throws Exception {
		eventCountryImportanceCheck();
	}
	
	@Test
	public void d_LastYearOutputTest() throws Exception {
		LastYearOutput();
		}
	
	public void eventCountryImportanceCheck() throws Exception {
		I.clickSimple("country_flag");
		String countryCheck = I.getXpathValue("//*[contains(@text, 'Country')]/following-sibling::android.widget.TextView[1]", "text");
		assert(countryCheck.equals("Switzerland"));
		String importanceCheck = I.getXpathValue("//*[contains(@resource-id, 'event_importance')]", "text");
		assert(importanceCheck.equals("Medium"));
		return;
	}
	
	public void LastYearOutput() throws Exception {
		I.click("History");
		I.see("2018");
		List<MobileElement> elements = driver.findElements(By.xpath("//*[contains(@resource-id, 'history_table')]/descendant::android.widget.TextView"));
		for (int i = 0; i < elements.size(); i++) {
			 if (elements.get(i).getText().contains("2018")) {
				 break;
			 }
			 if (i % 4 == 0 && i > 4 && elements.get(i-4).getText().equals(elements.get(i).getText())) {
				 System.out.println("-- error ? -> duplicate date [ "+elements.get(i).getText()+" ] ");
				 assert(!elements.get(i-4).getText().equals(elements.get(i).getText()));
			 }
			 if (i % 4 == 0) {
				 System.out.print("| "+elements.get(i).getText() +" | ");
			 } else if (i % 4 == 3  ) {
				 System.out.println(elements.get(i).getText() +" | ");
			 } else {
				 System.out.print(elements.get(i).getText() +" | ");
			 }
			}
		return;
		}
	
	public void setLanguageEnglIfSelectedRus() throws Exception {
		I.clickSimple("bottom_bar_settings");
		String lang = I.getXpathValue("//*[contains(@text, 'Language')]/following-sibling::android.widget.FrameLayout[1]/android.widget.TextView[1]", "text");
		System.out.println(lang);
		if (!lang.equals("English")) {
			driver.findElement(By.xpath("//*[contains(@text, 'Language')]/../android.widget.TextView[1]")).click();
			I.click("Английский");
			I.clickSimple("bottom_bar_settings");
			String eng = I.getXpathValue("//*[contains(@text, 'Language')]/following-sibling::android.widget.FrameLayout[1]/android.widget.TextView[1]", "text");
			System.out.println(eng);
		}
		return;
	}
	
	public void setFilter(String importance, String country) throws Exception {
		I.clickSimple("bottom_bar_calendar");
		I.clickSimple("menu_filter");
		driver.findElement(By.xpath("//*[contains(@text, 'Importance')]/following-sibling::android.widget.Button[1]")).click();
		String cbi = driver.findElement(By.xpath("//*[contains(@text, 'Holidays')]/../..//android.widget.CheckBox")).getAttribute("checked");
		if (!cbi.equals("true")) {
			I.click(importance);
		} else {
			driver.findElement(By.xpath("//*[contains(@text, 'Importance')]/following-sibling::android.widget.Button[1]")).click();
			I.click(importance);
		}
		driver.findElement(By.xpath("//*[contains(@text, 'Country')]/following-sibling::android.widget.Button[1]")).click();
		String cbc = driver.findElement(By.xpath("//*[contains(@text, 'Australia')]/../..//android.widget.CheckBox")).getAttribute("checked");
		if (!cbc.equals("true")) {
			I.click(country);
		} else {
			driver.findElement(By.xpath("//*[contains(@text, 'Country')]/following-sibling::android.widget.Button[1]")).click();
			I.click(country);
		}
		driver.findElement(By.xpath("//*[@class='android.widget.ImageButton' and @index='0']")).click();
		return;
	}
	

}

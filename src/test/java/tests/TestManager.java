package tests;

import java.util.List;

import io.appium.java_client.MobileElement;

public class TestManager extends BaseClass {
	public static void eventCountryImportanceCheck() throws Exception {
		I.clickSimple("country_flag");
		String countryCheck = I.getValue(Locators.COUNTRY, "text");
		assert (countryCheck.equals("Switzerland"));
		String importanceCheck = I.getValue(Locators.EVENT_IMPORTANCE, "text");
		assert (importanceCheck.equals("Medium"));
		return;
	}

	public static void LastYearOutput() throws Exception {
		I.click("History");
		I.see("2018");
		List<MobileElement> elements = driver.findElements(Locators.HISTORY_TABLE);
		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i).getText().contains("2018")) {
				break;
			}
			if (i % 4 == 0 && i > 4 && elements.get(i - 4).getText().equals(elements.get(i).getText())) {
				System.out.println("-- error ? -> duplicate date [ " + elements.get(i).getText() + " ] ");
				assert (!elements.get(i - 4).getText().equals(elements.get(i).getText()));
			}
			if (i % 4 == 0) {
				System.out.print("| " + elements.get(i).getText() + " | ");
			} else if (i % 4 == 3) {
				System.out.println(elements.get(i).getText() + " | ");
			} else {
				System.out.print(elements.get(i).getText() + " | ");
			}
		}
		return;
	}

	public static void setLanguageEnglIfSelectedRus() throws Exception {
		I.wait(15, "country_flag");
		I.clickSimple("bottom_bar_settings");
		String lang = I.getValue(Locators.LANGUAGE, "text");
		System.out.println(lang);
		if (!lang.equals("English")) {
			driver.findElement(Locators.LANGUAGE_SET).click();
			I.click("Английский");
			I.clickSimple("bottom_bar_settings");
			String eng = I.getValue(Locators.LANGUAGE, "text");
			System.out.println(eng);
		}
		return;
	}

	public static void setFilter(String importanceValue, String countryValue) throws Exception {
		I.clickSimple("bottom_bar_calendar");
		I.clickSimple("menu_filter");
		I.click(Locators.IMPORTANCE_SELECT_ALL);
		String cbi = I.getValue(Locators.HOLIDAYS_CHECKBOX, "checked");
		if (!cbi.equals("true")) {
			I.click(importanceValue);
		} else {
			I.click(Locators.IMPORTANCE_SELECT_ALL);
			I.click(importanceValue);
		}
		I.click(Locators.COUNTRY_SELECT_ALL);
		String cbc = I.getValue(Locators.AUSTRALIA_CHECKBOX, "checked");
		if (!cbc.equals("true")) {
			I.click(countryValue);
		} else {
			I.click(Locators.COUNTRY_SELECT_ALL);
			I.click(countryValue);
		}
		I.click(Locators.BACK_BUTTON);
		return;
	}

}

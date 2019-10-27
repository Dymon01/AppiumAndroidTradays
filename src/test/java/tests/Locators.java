package tests;

import org.openqa.selenium.By; 

public class Locators {	
	public static final By COUNTRY = By.xpath("//*[contains(@text, 'Country')]/following-sibling::android.widget.TextView[1]");
	public static final By EVENT_IMPORTANCE = By.xpath("//*[contains(@resource-id, 'event_importance')]");
	public static final By HISTORY_TABLE = By.xpath("//*[contains(@resource-id, 'history_table')]/descendant::android.widget.TextView");
	public static final By LANGUAGE = By.xpath("//*[contains(@text, 'Language')]/following-sibling::android.widget.FrameLayout[1]/android.widget.TextView[1]");
	public static final By LANGUAGE_SET = By.xpath("//*[contains(@text, 'Language')]/../android.widget.TextView[1]");
	public static final By IMPORTANCE_SELECT_ALL = By.xpath("//*[contains(@text, 'Importance')]/following-sibling::android.widget.Button[1]");
	public static final By HOLIDAYS_CHECKBOX = By.xpath("//*[contains(@text, 'Holidays')]/../..//android.widget.CheckBox");
	public static final By COUNTRY_SELECT_ALL = By.xpath("//*[contains(@text, 'Country')]/following-sibling::android.widget.Button[1]");
	public static final By AUSTRALIA_CHECKBOX = By.xpath("//*[contains(@text, 'Australia')]/../..//android.widget.CheckBox");
	public static final By BACK_BUTTON = By.xpath("//*[@class='android.widget.ImageButton' and @index='0']");
}

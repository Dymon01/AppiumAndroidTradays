package tests;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class I extends BaseClass {
	static final int X = 16;
	static final int Y = 789;
	static final int Xoffset = 26;
	static final int Yoffset = 9;

	public static void scrollTouchAction() {
		scrollTouchAction("");
	}

	public static void scrollTouchAction(String vol) {
		if (vol == "max") {
			new TouchAction(driver).press(PointOption.point(16, 789))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(16, 9))
					.release().perform();
		} else {
			new TouchAction(driver).press(PointOption.point(16, 550))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(16, 200))
					.release().perform();
		}

	}

	public static String request(String locator) {
		String LOCATOR = "//*[contains(@label, '" + locator + "') or contains(@name, '" + locator
				+ "') or contains(@value, '" + locator + "') or contains(@text, '" + locator
				+ "') or contains(@resource-id, '" + locator + "') or contains(@content-desc, '" + locator
				+ "') or contains(@type, '" + locator + "')]";
		return LOCATOR;
	}

	public static String getXpathValue(String xpath, String attributte) {
		String value = driver.findElement(By.xpath(xpath)).getAttribute(attributte);
		return value;
	}

	public static String getValue(By locator, String attributte) {
		String value = driver.findElement(locator).getAttribute(attributte);
		return value;
	}

	public static String getValue(String locator) {

		ArrayList<String> list = null;
		MobileElement element = (MobileElement) driver.findElement(By.xpath(request(locator)));

		see(locator);
		String contentDescription = element.getAttribute("contentDescription");
		String text = element.getAttribute("text");
		String[] array = new String[] { contentDescription, text, locator };
		list = new ArrayList<String>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
			list.remove(locator);
			list.remove("null");

		}
		return list.toString().replace("[", "").replace("]", "");

	}

	public static I see(String locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			scrollTouchAction();
			try {
				I.wait(1, locator);
			} catch (Exception e) {
				System.out.println(locator + " <<<<- retry to see ");
				scrollTouchAction();
				I.wait(1, locator);
			}
		} catch (Exception e4) {
			System.out.println(locator + " <<<<- scrollIntoView to see ");
			((FindsByAndroidUIAutomator) driver).findElementByAndroidUIAutomator(
					"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""
							+ locator + "\").instance(0))");
		}

		return null;
	}
	
	
	public static I clickSimple(String locator) throws IOException {
		try {
			RemoteWebElement element = (RemoteWebElement) driver.findElement(MobileBy.xpath(request(locator)));
			Point loc = element.getLocation();
			String locs = loc.toString();

			String[] parts = locs.split(",");
			String xp = parts[0];
			String yp = parts[1];
			xp = xp.replace("(", "").replace(" ", "");
			yp = yp.replace(")", "").replace(" ", "");

			int Xoffset = Integer.parseInt(xp);
			int Yoffset = Integer.parseInt(yp);
			System.out.println(
					"<<<-" + locator + "- at x:" + Xoffset + " y:" + Yoffset + "|| clickSimple                 ");
			element.click();
		} catch (Exception e) {
			System.out.println(locator + "<<<<- clickSimple fail ");
			I.getValue(locator);
		}
		return null;

	}

	public static I wait(int sec, String locator) throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, sec);
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.xpath(request(locator))));
		return null;

	}

	public static I click(By locator) throws IOException {
		driver.findElement(locator).click();
		return null;
	}

	public static I clickExecuteScript(String locator) throws IOException {
		MobileElement element = (MobileElement) driver.findElement(MobileBy.xpath(request(locator)));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		String elementID = ((RemoteWebElement) element).getId();
		HashMap<String, String> scrollObject = new HashMap<String, String>();
		scrollObject.put("toVisible", "true");
		scrollObject.put("element", elementID);
		driver.executeScript("mobile:scroll", scrollObject);
		element.click();
		return null;

	}

	public static I clickResourceId(String locator) throws IOException {
		try {
			((FindsByAndroidUIAutomator) driver).findElementByAndroidUIAutomator(
					"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().resourceId(\""
							+ releaseBundle + ".debug:id/" + locator + "\").instance(0))")
					.click();
		} catch (Exception e) {
			driver.findElement(By.xpath("//*[contains(@resource-id, '" + locator + "')]")).click();
			;
		}
		return null;
	}

	public static I clickDescription(String locator) throws IOException {
		((FindsByAndroidUIAutomator) driver).findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().descriptionContains(\""
						+ locator + "\").instance(0))")
				.click();
		return null;
	}

	public static I clickByLocation(String locator) throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.xpath(request(locator))));

		RemoteWebElement element = (RemoteWebElement) driver.findElement(MobileBy.xpath(request(locator)));
		Point loc = element.getLocation();
		String locs = loc.toString();

		String[] parts = locs.split(",");
		String xp = parts[0];
		String yp = parts[1];
		xp = xp.replace("(", "").replace(" ", "");
		yp = yp.replace(")", "").replace(" ", "");

		int Xoffset = Integer.parseInt(xp);
		int Yoffset = Integer.parseInt(yp);
		TouchAction touchAction = new TouchAction((PerformsTouchActions) driver);
		touchAction.press(PointOption.point(Xoffset + 5, Yoffset + 5)).perform();
		System.out.println("<<<-" + locator + "- at x:" + Xoffset + " y:" + Yoffset + "||                   ");
		return null;

	}

	public static I click(String locator) throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, 18);
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""
						+ locator + "\").instance(0))")));

		RemoteWebElement element = (RemoteWebElement) driver.findElement(MobileBy.xpath(request(locator)));
		Point loc = element.getLocation();
		String locs = loc.toString();

		String[] parts = locs.split(",");
		String xp = parts[0];
		String yp = parts[1];
		xp = xp.replace("(", "").replace(" ", "");
		yp = yp.replace(")", "").replace(" ", "");

		int Xoffset = Integer.parseInt(xp);
		int Yoffset = Integer.parseInt(yp);
		System.out.println("<<<-" + locator + "- at x:" + Xoffset + " y:" + Yoffset + "| ");
		try {
			element.click();
		} catch (Exception e) {
			System.out.println("<<<-click- fail| ");
			TouchAction touchAction = new TouchAction((PerformsTouchActions) driver);
			touchAction.press(PointOption.point(Xoffset + 5, Yoffset + 5)).perform();
			System.out.println("<<<-TouchAction- pass| ");

		}
		return null;
	}

	public static I type(String text) throws Exception {
		return type(text, true);
	}

	public static I type(String text, boolean flag) throws Exception {

		Thread.sleep(1000);

		((RemoteWebDriver) driver).getKeyboard().sendKeys(text);
		Thread.sleep(1200);
		if (flag) {
			driver.hideKeyboard();
		}
		Thread.sleep(1500);
		try {
			Thread.sleep(1500);
		} catch (Exception e) {
			try {
				System.out.println("[[[[click done fail]]]]]");
			} catch (Exception e3) {
			}
		}

		return null;
	}

}
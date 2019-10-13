package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.lang.reflect.Method;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class BaseClass extends TestListenerAdapter{
	
	protected static String releaseBundle = "net.metaquotes.economiccalendar";
	protected static AppiumDriverLocalService service;
	protected static String device =  System.getProperty("device");
	protected static AppiumDriver driver;
	protected static String testCaseName;
	
	@BeforeSuite
	public void setup() throws Exception{
		int min = 8100;
		int max = 9800;
		int port = ThreadLocalRandom.current().nextInt(min, max + 1);
		int wport = ThreadLocalRandom.current().nextInt(min, max + 1);
		int bpport = ThreadLocalRandom.current().nextInt(min, max + 1);		
		startAppium( port,  wport,  bpport);
		startAndroidDriver( port, bpport );
		try {
			executeCommand("/bin/rm -rf "
					+ System.getProperty("user.dir") + "/device_logs/TC_*" +  ".*");
			System.out.println("      ----------[ Build Info ]--------- ");
			executeCommand("~/Library/Android/sdk/platform-tools/adb -s " + device
					+ " shell dumpsys package "+releaseBundle+" | grep versionName");
			executeCommand("~/Library/Android/sdk/platform-tools/adb -s " + device
					+ " shell dumpsys package "+releaseBundle+" | grep versionCode");
			System.out.println("      --------------------------------- ");
		} catch (Exception e1) {
		}
	}
	
   @BeforeMethod
    public void handleTestMethodName(Method method) throws Exception {
         testCaseName = method.getName(); 
         logStart(testCaseName);
   }
	   
	@BeforeTest
	public void logTest() throws Exception{
		//logTestStart(testCaseName);
	}
	
	@Override
	public void onTestFailure(ITestResult testResult) {
		logStep("_______________Failure_________________\n");
		getScreenshot("<<fail_"+ testCaseName);
		String log = driver.getPageSource();
		System.out.println("-------------------PageSource---------------------");
		System.out.println(log);

	}
	
	
	
	@Override
	public void onTestSuccess(ITestResult testResult){
		logStep("_______________Success_________________\n");
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		logStep("_________________________________________");
	}

	@AfterMethod
	protected void logStop() throws Exception {
		Runtime.getRuntime()
		.exec(new String[] { "/bin/sh", "-c", "/bin/kill -INT $(ps aux | grep '[a]db -s "+device+" logcat -v time' | awk '{print $2}')" });
	}
	
	@AfterSuite
	protected void tearDown() throws Exception {
			service.stop();		
	}
	public String executeCommand(String cmd) throws Exception {

		Process proc = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c",
				cmd });
		BufferedReader stdInput = new BufferedReader(new 
			     InputStreamReader(proc.getInputStream()));
			String s = null;
			while ((s = stdInput.readLine()) != null) {
			    System.out.println(s);
			    return s;
			}
		return s;

	}

	protected void startAndroidDriver(int port, int bpport) throws Exception {

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", getProp("ro.build.version.release"));
		capabilities.setCapability("automationName", "uiautomator2");
		capabilities.setCapability("newCommandTimeout", "500");
		capabilities.setCapability("systemPort", bpport);		
		capabilities.setCapability("deviceName", getProp("ro.product.model"));
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("appPackage", releaseBundle);
		capabilities.setCapability("appActivity", "net.metaquotes.ui.MainActivity");

		driver = new AndroidDriver(new URL("http://127.0.0.1:" + String.valueOf(port) + "/wd/hub"), capabilities);
		System.out.println("Launched Driver On "+System.getProperty("device")+" on port-"+ String.valueOf(port));

		driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);

	}
	
	protected static void startAppium(int port, int wport, int bpport) throws InterruptedException {
		DesiredCapabilities cap = new DesiredCapabilities();
		
		AppiumServiceBuilder builder;
		
		//Set Capabilities
		cap.setCapability("noReset", "false");
		cap.setCapability("wdaLocalPort", wport);
		cap.setCapability("bp", bpport);
		cap.setCapability("webkit-debug-proxy-port", bpport);
	
		//Build the Appium service
		builder = new AppiumServiceBuilder();
		builder.withIPAddress("127.0.0.1");
		builder.usingPort(port);
		builder.withCapabilities(cap);
		builder.withArgument(GeneralServerFlag.LOG_TIMESTAMP);
		builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");
		//Start the server with the builder
		service = AppiumDriverLocalService.buildService(builder);
		service.start();

	}

	public StringBuffer getProp(String arg) {

		StringBuffer version = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime()
					.exec(new String[] { "/bin/sh", "-c", "adb -s "+device+" shell getprop "+ arg});
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				version.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(version);
		return version;
	}
	
	public static void getScreenshot(String comment) {
		
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "/device_logs/TC_"+ comment +"_.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		
		}
	}
	
    protected static void write(String message) {
        Date now = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");// + System.lineSeparator()
        System.out.println(timeFormat.format(now) + " " + message);
    }
    
    public static void logStep(String message) {
        String text = ("Step: " + message);
        write(text);
    }
	
    public void logStart(String arg) throws Exception {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("_yyyy_MM_dd-HH_mm");
		String testName = arg + new String(formater.format(calendar.getTime()));
			try {
				Runtime.getRuntime()
						.exec(new String[] { "/bin/sh", "-c", "~/Library/Android/sdk/platform-tools/adb -s "+device+" logcat -c" });
				Runtime.getRuntime()
						.exec(new String[] { "/bin/sh", "-c",
								"~/Library/Android/sdk/platform-tools/adb -s "+device+" logcat -v time > " + System.getProperty("user.dir")
										+ "/device_logs/TC_"+(String) testName+".log" });
			} catch (IOException e) {
				e.printStackTrace();
			}
        String message = "-------------[ " +arg + " ]-------------";
        write(message);
    }
}

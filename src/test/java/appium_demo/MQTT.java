package appium_demo;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidBatteryInfo;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

public class MQTT {

		
	    private AndroidDriver<WebElement> driverEspresso;
	    private AndroidDriver<WebElement> driverUIAutomator;
	    private BackDoorImp backDoor;

	    @Before
	    public void setUp() throws IOException {
	    	backDoor = new BackDoorImp();
	    }

	    @After
	    public void tearDown() {
	        
	    	
	    }

	    /*
	     *Performing color picker validation 
	     *1. Open the Dashboard app
	     *2. Add any panel which has the option to select custom color (example RGBLed)
	     *3. Click on the image which shows default color.
	     *4. Validate the default selected color in the color picker view
	     *5. Change the default color
	     *6. Validate selected color is as per step 5.
	     */
	    @Test
	    public void test_02_ColorPicker() throws MalformedURLException, InterruptedException
	    {
	    	driverEspresso = createDriver("Espresso", "com.ravendmaster.linearmqttdashboard", ".activity.MainActivity","4723", "udid");
	    	
	    	//Clicking on Edit pay mode option
	    	WebElement editPlayMode = driverEspresso.findElement(MobileBy.id("Edit_play_mode"));
	    	editPlayMode.click();
	    	
	    	//Clicking on Add new widget menu
	    	WebElement addNewWidget = driverEspresso.findElement(MobileBy.id("Add_new_widget"));
	    	addNewWidget.click();
	    	
	    	//Clicking on color topic image
	    	WebElement imageColor = driverEspresso.findElement(MobileBy.id("color_topic"));
	    	imageColor.click();
	    	
	    	//Validate the default selected color from Color picker view
	    	WebElement colorPicker = driverEspresso.findElement(MobileBy.id("color_picker"));

	    	
	    	int color = Integer.parseInt(backDoor.executeViaBackdoor(
	    			backDoor.getBackdoorObjGetMethod(colorPicker, "element", "getColor"), 
	    			driverEspresso,"mobile: backdoor"));
	    	String beforeColor = getHexColor(color);
	    	System.out.println("Color picker selected actual color before = " + beforeColor);
	    	
	    	//Change the default color and select another one
	    	backDoor.executeViaBackdoorAction(
	    			backDoor.getBackdoorObjMobileClick(colorPicker, "element", "TOP_LEFT"), 
	    			driverEspresso,"mobile: clickAction");
	    	
	    	imageColor.click();
	
	    	
	    	//Validate the user selected color from Color picker view
	    	//Refinding the element since we moved out of color picker once.
	    	colorPicker = driverEspresso.findElement(MobileBy.id("color_picker"));
	    	color = Integer.parseInt(backDoor.executeViaBackdoor(
	    			backDoor.getBackdoorObjGetMethod(colorPicker, "element", "getColor"), 
	    			driverEspresso,"mobile: backdoor"));
	    	
	    	
	    	String afterColor = getHexColor(color);
	    	System.out.println("Color picker selected actual color after = " + afterColor);
	    	
	        
	    	driverEspresso.quit();
	    }
	    
	    /*
	     * Main validation use case - will perform the following
	     * 1. Opens the Dashboard app and validate the panel states on device1
	     * 2. Opens the Publisher app and perform Switch on operation by publishing payload 1 to broker on device2.
	     * 3. Alert app will trigger incoming notification and same will be verified on device2;
	     * 4. Opens the dashboard app and validate the states of the panel elements on device1
	     * 5. Opens the Publisher app and perform Switch off operation by publishing payload 0 to broker on device2.
	     * 6. Alert app should not trigger incoming notification and same will be verified on device2;
	     * 7. Opens the Dashboard app and validate the states of the panel elements on device1
	     */
	    @Test  
	    public void test_01_MQTTPannelUpdates() throws MalformedURLException, InterruptedException
	    {
	     	driverEspresso = createDriver("Espresso", "com.ravendmaster.linearmqttdashboard", ".activity.MainActivity","4723","udid_device1");
	      	
		driverUIAutomator =  createDriver("UIAutomator2","com.app.vetru.mqttdashboard",".ui.Activity_Main","4723","udid_device2");
			
		testEspressBefore(driverEspresso);
			
		testBatteryInfo(driverUIAutomator);
			
		testUIAutomatorPublishMsgSwitchOn(driverUIAutomator);
			
		testUIAutomatorValidateNotification(driverUIAutomator);
	    		    	
	    	testEspressoAfter(driverEspresso);	
	    	
	    	
	    	driverUIAutomator.launchApp();
	    	
	    	testUIAutomatorPublishMsgSwitchOff(driverUIAutomator);
	    	
	    	testUIAutomatorValidateNotification(driverUIAutomator);
	    	
	    	testBatteryInfo(driverUIAutomator);
	    	
	    	testEspressBefore(driverEspresso);
	    	    	
	    	driverUIAutomator.quit();
	    	
	    	driverEspresso.quit();
	    } 

	    /*
	     * This method will initiate MQTT publishing to broker and publish a pay load with value 1
	     * which will trigger switch on in the MQTT dashboard app
	     * */
	    private void testUIAutomatorPublishMsgSwitchOn(AndroidDriver<WebElement> driver) throws InterruptedException 
	    {
	    	System.out.println("*********************************************************");
	    	System.out.println("MQTT Publisher publising on topic");
	    	System.out.println("*********************************************************");
	    	List<WebElement> switchOn = driver.findElements(MobileBy.id("baseLayout"));
	        switchOn.get(0).click();
	        switchOn.get(0).click();
	    	Thread.sleep(2000);
	        System.out.println("Published to hall topic - successfully with payload 1 will perform switch on");
	    }
	    
	    /*
	     * This method will get the battery information from the device
	     */
	    
	    private void testBatteryInfo(AndroidDriver<WebElement> driver) throws InterruptedException 
		{
	    	final AndroidBatteryInfo batteryInfo = driver.getBatteryInfo();
	    	System.out.println("System batter info = " +batteryInfo.getLevel());
		}
	    
	    /*
	     * This method will initiate MQTT publishing to broker and publish a pay load with value 0
	     * which will trigger switch on in the MQTT dashboard app
	     * */
	    private void testUIAutomatorPublishMsgSwitchOff(AndroidDriver<WebElement> driver) throws InterruptedException 
	    {
	    	System.out.println("*********************************************************");
	    	System.out.println("MQTT Publisher publising on topic");
	    	System.out.println("*********************************************************");
	    	List<WebElement> switchOn = driver.findElements(MobileBy.id("baseLayout"));
	        switchOn.get(1).click();
	        switchOn.get(1).click();
	        Thread.sleep(2000);
	        System.out.println("Published to hall topic - successfully with payload 0 - will perform switch off");
	    }
	    
	    
	    /*
	     * This method will validate the incoming alert from status bar after receiving any incoming
	     * notification via MQTT broker
	     * */
	    private void  testUIAutomatorValidateNotification(AndroidDriver<WebElement> driver)
	    {
	    	System.out.println("*********************************************************");
	    	System.out.println("MQTT notifier - notification detected");
	    	System.out.println("*********************************************************");
	    	//Opening the notification tray
	    	driver.openNotifications();

	    	List<WebElement> notificationTitles = driver.findElementsById("android:id/title");
	    	
	    	
	    	for(int i=0; i<notificationTitles.size(); i++)
	    	{
	    		String alertText = notificationTitles.get(i).getText();
	    		if(alertText.contains("MQTT Alert"))
	    		{
	    			if(!alertText.contains("service"))
	    			{
	    				notificationTitles.get(i).click();
	    				System.out.println("\nMQTT Alert detected\n");
	    			}
	    			else
	    			{
	    				System.out.println("\nMQTT Alert not detected\n");
	    		       
	    			}
	    			break;
	    		}
	    		
	    	}
	    	//Clicking on Home to close the notification tray or Alert app
	    	driver.pressKey(new KeyEvent(AndroidKey.HOME));


	    }
	
	    /*
	     * This test will validate the state of the MQTT Dashboard before receiving any incoming
	     * notification via MQTT broker
	     * */
	    
	    private void testEspressBefore(AndroidDriver<WebElement> driver) throws InterruptedException 
	    {
	    	System.out.println("*************************************************************");
	    	System.out.println("MQTT dashboard status before receiving incoming event");
	    	System.out.println("*************************************************************");
	    	
	    	//Getting the color of the Network text view
	    	WebElement networkTxt = driverEspresso.findElement(MobileBy.id("textView_text_connection"));
	
	        int color = Integer.parseInt(backDoor.executeViaBackdoor(
	        		backDoor.getBackdoorObjGetMethod(networkTxt, "element", "getCurrentTextColor"), 
	    			driverEspresso,"mobile: backdoor"));
	    	String networkTxtColor = getHexColor(color);
	    	
	        System.out.println("Network Label text color = " + networkTxtColor + "\n");
	        
	        //Getting the color of the Switch panel
	        WebElement switchPanel = driverEspresso.findElement(MobileBy.xpath("//android.widget.Switch[@content-desc=\"mWidgetSwitch0\"]"));
	        String switchColor = backDoor.executeViaBackdoor(backDoor.getBackdoorObj("activity", "getSwitchColor"), driver,"mobile: backdoor");
	        System.out.println("Switch color before= " + switchColor);
	           
	        //Getting the color of the LED panel 
	        boolean switchstate = Boolean.parseBoolean(backDoor.executeViaBackdoor(
	        		backDoor.getBackdoorObjGetMethod(switchPanel, "element", "isChecked"), 
	    			driverEspresso,"mobile: backdoor"));
	    	
	        
	    	System.out.println("Switch State before = " + switchstate + "\n");
	    	          		         
	    }
	    
	    /*
	     * Validating the MQTT Dashboard after receiving incoming MQTT message on the subscribed topic
	     */
	    private void testEspressoAfter(AndroidDriver<WebElement> driver) throws InterruptedException 
	    {
	    	System.out.println("*********************************************************");
	    	System.out.println("MQTT dashboard status after receiving incoming event");
	    	System.out.println("*********************************************************");
	    	
	    	//Getting the Color of the switch panel
	        String switchColor = backDoor.executeViaBackdoor(backDoor.getBackdoorObj("activity", "getSwitchColor"), driver,"mobile: backdoor");
	    	System.out.println("Switch color after = " + switchColor);
	    	
	    	//Getting the State of the switch panel
	    	WebElement switchPanel = driverEspresso.findElement(MobileBy.xpath("//android.widget.Switch[@content-desc=\"mWidgetSwitch0\"]"));

	        boolean switchstate = Boolean.parseBoolean(backDoor.executeViaBackdoor(
	        		backDoor.getBackdoorObjGetMethod(switchPanel, "element", "isChecked"), 
	    			driverEspresso,"mobile: backdoor"));
	    	
	    	System.out.println("Switch State after = " + switchstate + "\n");
	    	
	    	//Getting the color of the LED panel
	    	List<WebElement> RGBLed = driverEspresso.findElements(MobileBy.id("widget_RGBLed"));
	      	
	    	int ledcolor = Integer.parseInt(backDoor.executeViaBackdoor(
	    			backDoor.getBackdoorObjGetMethod(RGBLed.get(2), "element", "isColorLight"), 
	    			driverEspresso,"mobile: backdoor"));
	
	    	String afterColorLed = getHexColor(ledcolor);
	    	System.out.println("RGB LED color after = " + afterColorLed+ "\n");
	    	
	    		    
	    }
	    
	    /*
	     * To create the driver based on capabilities
	     */
	    private AndroidDriver<WebElement> createDriver(String automationName, String packageName, String activityName, String port, String udid) throws MalformedURLException {

	    	File classpathRoot = new File(System.getProperty("user.dir"));
	    	File appDir = new File(classpathRoot, "src/test/resources");
	    	File app = new File(appDir, "app-debug.apk");
	    	
	        DesiredCapabilities capabilities = new DesiredCapabilities();
	        capabilities.setCapability("platformName", "Android");
	        capabilities.setCapability("deviceName", "Oneplus");
	        capabilities.setCapability("automationName", automationName);
	        capabilities.setCapability("appPackage", packageName);
	        capabilities.setCapability("appActivity", activityName);
	        capabilities.setCapability("udid", udid);
	        // To be uncommented if app needs to be installed everytime
	        //capabilities.setCapability("app", app.getAbsolutePath());
	        capabilities.setCapability("noReset", true);
	        String url = "http://localhost:" + port + "/wd/hub";
	        return new AndroidDriver<WebElement>(new URL(url), capabilities);
	    } 
	    
	    
	    /*
	     * To get the color in hhex format
	     */
	    private String getHexColor(int color)
	    {
	    	String hexColor = String.format("#%06X", (0xFFFFFF & color));
	        return hexColor;
	    }
}

package appium_demo;

import java.util.Arrays;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.android.AndroidDriver;

public class BackDoorImp {
	
	/*Execute backdoor command which will return object*/
	public Object executeViaBackdoorGetObj(ImmutableMap<String, Object> scriptArgs, AndroidDriver<WebElement> driver, String commandName)
	{
	    	return driver.executeScript(commandName, scriptArgs);
	}
	    
	/*Execute backdoor command which will return string*/   
	public String executeViaBackdoor(ImmutableMap<String, Object> scriptArgs, AndroidDriver<WebElement> driver, String commandName)
	{
	    return driver.executeScript(commandName, scriptArgs).toString();
	}
	   
	/*Execute backdoor command which will perform action*/
	public void executeViaBackdoorAction(ImmutableMap<String, String> scriptArgs, AndroidDriver<WebElement> driver, String commandName)
	{
	    	try
	    	{
	    		driver.executeScript(commandName, scriptArgs).toString();
	    	}
	    	catch(Exception ex)
	    	{
	    		// Added this block since execute script was returning null pointer exception
	    	}
    }
	
	
	/*Create the backdoor object to be performed on target Activity or Application*/
	public ImmutableMap<String, Object> getBackdoorObj(String target, String methodName)
    {
    	@SuppressWarnings("unchecked")
		ImmutableMap<String, Object> scriptArgs = ImmutableMap.of
	        	(
	    	            "target", target,
	    	            "methods", Arrays.asList(ImmutableMap.of(
	    		                "name", methodName
	    		            ))
	    		 );
    	
    	return scriptArgs;
    }
    
	/*Create the backdoor object to be performed on target element
	 * and calling methods with parameters*/
    public ImmutableMap<String, Object> getBackdoorObjSetMethod(WebElement ele, String target, String methodName, String argValue, String argType)
    {
    	@SuppressWarnings("unchecked")
		ImmutableMap<String, Object> scriptArgs = ImmutableMap.of(
	            "target", target,
	            "elementId", ((RemoteWebElement) ele).getId(),
	            "methods", Arrays.asList(ImmutableMap.of(
	                "name", methodName,
	                "args", Arrays.asList(ImmutableMap.of(
	                    "value", argValue,
	                    "type", argType
	                ))
	            ))
	        );

    	
    	return scriptArgs;
    }
    
    /*Create the backdoor object to be performed on target element
	 * and calling methods without parameters*/
    public ImmutableMap<String, Object> getBackdoorObjGetMethod(WebElement ele, String target, String methodName)
    {
    	@SuppressWarnings("unchecked")
		ImmutableMap<String, Object> scriptArgs = ImmutableMap.of(
	            "target", target,
	            "elementId", ((RemoteWebElement) ele).getId(),
	            "methods", Arrays.asList(ImmutableMap.of(
	                "name", methodName
	            ))
	        );

    	
    	return scriptArgs;
    }
    
    /*Create the backdoor object to be performed on target element
	 * and performing action*/
    public ImmutableMap<String, String> getBackdoorObjMobileClick(WebElement ele, String target, String cord)
    {
    	ImmutableMap<String, String> scriptArgs = ImmutableMap.of(
	            "target", target,
	            "element", ((RemoteWebElement) ele).getId(),
	            "coordinatesProvider", cord
	            );

    	
    	return scriptArgs;
    }
    
   

}

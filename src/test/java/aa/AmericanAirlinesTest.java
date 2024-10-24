package aa;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;



public class AmericanAirlinesTest {
    private static final String ONE_WAY_RADIO_ID = "flightSearchForm.tripType.oneWay";
    private static final String ORIGINAL_AIRPORT_ID = "reservationFlightSearchForm.originAirport";
    private static final String DESTINATION_AIRPORT_ID = "reservationFlightSearchForm.destinationAirport";
    private static final String PASSENGER_COUNT_ID = "flightSearchForm.adultOrSeniorPassengerCount";
    private static final String SEARCH_BUTTON_ID = "flightSearchForm.button.reSubmit";
    private static final String LEAVING_ON_ID = "aa-leavingOn";
    private static final String NO_UPGRADE_BUTTON_ID = "btn-no-upgrade";
    private static final String MAIN_FLIGHT  = "flight-4-product-group-MAIN";
    private static final String MAIN_BASIC_ECONOMY = "slice-4-MAIN-basic-economy";
    private static final String TRIP_SUMMARY = "American Airlines - Your trip summary";
    private static final String FLIGHTCARD_SUMMARY  = "flightcard-summary";
    private String url;
    private WebDriver driver;
    //initialize url and driver
    @BeforeTest
    public void before(){
        url = "https://www.aa.com";
        driver= WebDriverManager.chromedriver().create(); //set up chrome driver
    }
    @DataProvider(name = "getData")
    public Object[][] getData() {
        return new Object[][]{
                {"SFO","LGA", "3", "12/25/2024"},
        };
    }
    @Test(dataProvider = "getData")
    public void searchTest(String from, String to, String count, String date) {
        try {
            // Maximum chrome window
            driver.manage().window().maximize();

            // Go to the american airline website
            driver.get(url);

            // Initialize WebDriverWait for handling dynamic elements
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(300));

            // Click on the "One Way" radio button
            WebElement oneWayButton = wait.until(ExpectedConditions.elementToBeClickable(By.id(ONE_WAY_RADIO_ID)));
            Actions actions = new Actions(driver);
            actions.moveToElement(oneWayButton).click().build().perform();

            // Enter the departure airport (JFK example)
            WebElement fromInput = wait.until(ExpectedConditions.elementToBeClickable(By.id(ORIGINAL_AIRPORT_ID)));
            fromInput.clear();
            fromInput.sendKeys(from);
            Thread.sleep(1000);  // Give time for suggestions to load

            // Enter the destination airport (LAX example)
            WebElement toInput = driver.findElement(By.id(DESTINATION_AIRPORT_ID));
            toInput.clear();
            toInput.sendKeys(to);
            Thread.sleep(1000);  // Give time for suggestions to load

            // Locate the passenger count drop down
            WebElement dropdownElement = driver.findElement(By.id(PASSENGER_COUNT_ID));

            Select dropdown = new Select(dropdownElement);

            // Select number of passenger
            dropdown.selectByValue(count);

            // Enter a departure date
            WebElement departureDate = driver.findElement(By.id(LEAVING_ON_ID));
            departureDate.clear();
            departureDate.sendKeys(date);
            //Search button click
            WebElement searchButton = driver.findElement(By.id(SEARCH_BUTTON_ID));
            searchButton.click();
            WebElement main = wait.until(ExpectedConditions.elementToBeClickable(By.id(MAIN_FLIGHT)));
            main.click();
            WebElement basicEconomy = wait.until(ExpectedConditions.elementToBeClickable(By.id(MAIN_BASIC_ECONOMY)));
            basicEconomy.click();
            WebElement noUpgrade = wait.until(ExpectedConditions.elementToBeClickable(By.id(NO_UPGRADE_BUTTON_ID)));
            noUpgrade.click();
            wait.until(ExpectedConditions.titleContains(TRIP_SUMMARY));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(FLIGHTCARD_SUMMARY)));
        } catch (Exception e) {
            System.out.println(e.getMessage()); //print out exception message
        }
    }

    @AfterTest
    void after() {
        driver.quit(); //close browser after test
    }
}

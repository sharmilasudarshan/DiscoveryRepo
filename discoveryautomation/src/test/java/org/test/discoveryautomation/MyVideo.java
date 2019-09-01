package org.test.discoveryautomation;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Hello world!
 *
 */
public class MyVideo {
	private static final Logger logger = Logger.getLogger(MyVideo.class);
	protected static Properties discovery_config;

	public MyVideo() throws Exception {
		initiaLize();
		// TODO Auto-generated constructor stub
	}

	@Test
	public static void Video() throws InterruptedException {

		WebDriver webDriver = null;
		try {

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir").concat("/chromedriver_mac"));
			webDriver = new ChromeDriver();
			logger.info("Opened Chrome driver");
			WebDriverWait wait = new WebDriverWait(webDriver, 20);

			webDriver.get(discovery_config.getProperty("ss_URL"));
			logger.info("Launched the Discovery URL : " + discovery_config.getProperty("ss_URL"));

			List<WebElement> lt = webDriver.findElements(By.xpath(discovery_config.getProperty("ss_videos_recc")));

			int no_videos_fav = Integer.parseInt(discovery_config.getProperty("ss_no_videos_add_fav"));
			ArrayList<String> st = new ArrayList<String>();
			ArrayList<String> sd = new ArrayList<String>();

			for (int i = 1; i <= no_videos_fav; i++) {

				webDriver.findElement(By.xpath(
						"(//div[contains(text(),'Recommended')]//ancestor::section//div[@class='showTileSquare__content']//span[@class='tooltip-wrapper']/i)["
								+ i + "]"))
						.click();

				st.add(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
						"(//div[contains(text(),'Recommended')]//ancestor::section//div[@class='showTileSquare__content']//span[@class='tooltip-wrapper']/i)["
								+ i + "]/ancestor::div/h3/div")))
						.getText());
				sd.add(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
						"(//div[contains(text(),'Recommended')]//ancestor::section//div[@class='showTileSquare__content']//span[@class='tooltip-wrapper']/i)["
								+ i + "]/ancestor::div/div[@class='showTileSquare__description']/div")))
						.getText());
				logger.info("Selected favorite video title : " + st.get(i-1));
				logger.info("Selected favorite video Description : " + sd.get(i-1));
			}
			Collections.sort(st);
			Collections.sort(sd);
			logger.info("Number of videos added as favorites from Recommended for you section : " + no_videos_fav);

			webDriver.findElement(By.xpath(discovery_config.getProperty("ss_menu"))).click();
			logger.info("Clicked on Menu icon");

			webDriver.findElement(By.xpath(discovery_config.getProperty("ss_my_videos_menu"))).click();
			logger.info("Clicked on My Videos");

			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(discovery_config.getProperty("ss_fav_vidoes"))));

			List<WebElement> lt1 = webDriver.findElements(By.xpath(discovery_config.getProperty("ss_fav_vidoes")));
			logger.info("Number of video's under Favorite shows : " + lt1.size());

			if (lt1.size() == no_videos_fav) {
				logger.info(
						"Same number of videos added as Favorite from recommended section are displayed in My Video's Favorite shows");

				ArrayList<String> st_af_add = new ArrayList<String>();
				ArrayList<String> sd_af_add = new ArrayList<String>();

				for (int i = 1; i <= lt1.size(); i++) {
					Actions act = new Actions(webDriver);

					WebElement element_title = webDriver.findElement(By.xpath(
							"(//h2[contains(text(),'Favorite Shows')]//ancestor::section//div[@class='carousel-tile-wrapper carousel__tileWrapper'])["
									+ i + "]//h3/div"));
					act.moveToElement(element_title).build().perform();
					WebElement element_desc = webDriver.findElement(By.xpath(
							"(//h2[contains(text(),'Favorite Shows')]//ancestor::section//div[@class='carousel-tile-wrapper carousel__tileWrapper'])["
									+ i + "]//div[@class='showTileSquare__description']/div"));
					act.moveToElement(element_desc).build().perform();

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
							"//h2[contains(text(),'Favorite Shows')]//ancestor::section//div[@class='carousel-tile-wrapper carousel__tileWrapper']["
									+ i + "]")));

					st_af_add.add(element_title.getText());
					sd_af_add.add(element_desc.getText());
					
					logger.info("Favorite show titles in My Video : " + st_af_add.get(i-1));
					logger.info("Favorite show Descriptions in My Video  : " + sd_af_add.get(i-1));

				}
				Collections.sort(st_af_add);
				Collections.sort(sd_af_add);
				

				for (int i = 0; i < no_videos_fav; i++) {
					Assert.assertEquals(st.get(i), st_af_add.get(i));
					logger.info("Title of the added recommended video : "+st.get(i)+" matched with the favorite video title : "+st_af_add.get(i));
				}
				for (int i = 0; i < no_videos_fav; i++) {
					Assert.assertEquals(sd.get(i), sd_af_add.get(i));
					logger.info("Description of the added recommended video : "+sd.get(i)+" matched with the favorite video description : "+sd_af_add.get(i));
				}
			} else {
				logger.error(
						"Same number of videos added as Favorite from recommended section are not displayed in My Video's Favorite shows");
			}
		} catch (Exception e) {
			logger.error(" Execution failed " + e.getMessage());
		} finally {
			webDriver.quit();
		}
	}

	public void initiaLize() throws Exception {
		discovery_config = new Properties();
		discovery_config.load(new FileInputStream(
				System.getProperty("user.dir").concat("/src/test/resources/discovery_config.properties")));

	}
}

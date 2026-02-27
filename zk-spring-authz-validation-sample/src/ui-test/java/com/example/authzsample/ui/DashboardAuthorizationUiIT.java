package com.example.authzsample.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DashboardAuthorizationUiIT {

    // -------------------------------------------------------
    // Single-place tuning knobs (change via -D system props)
    // -------------------------------------------------------
    private static final Duration TIMEOUT = Duration.ofSeconds(
            Long.parseLong(System.getProperty("ui.timeoutSeconds", "12"))
    );

    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(
            Long.parseLong(System.getProperty("ui.pageLoadTimeoutSeconds", "15"))
    );

    private static final Duration LOGOUT_TIMEOUT = Duration.ofSeconds(
            Long.parseLong(System.getProperty("ui.pageLoadTimeoutSeconds", "5"))
    );

    // Slow-motion: set -Dui.slowMs=1000 to watch steps; set 0 to disable
    private static final long SLOW_MS = Long.getLong("ui.slowMs", 1000L);

    static String baseUrl;
    static String driverMode;
    static String remoteUrl;
    static boolean headless;

    WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        baseUrl = System.getProperty("app.baseUrl", "http://localhost:8080");
        driverMode = System.getProperty("ui.driver", "local");
        remoteUrl = System.getProperty("ui.remoteUrl", "http://localhost:4444/wd/hub");
        headless = Boolean.parseBoolean(System.getProperty("ui.headless", "false"));

        if (!"remote".equalsIgnoreCase(driverMode)) {
            WebDriverManager.chromedriver().setup();
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        driver = createDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    void alice_sees_createPayment_button_and_can_logout() {
        login("alice", "password");
        openDashboardAndWaitReady();

        boolean visible = awaitCreatePaymentVisibleState(true);
        assertThat(visible).isTrue();

        logoutAndAssertLoginPage();
    }

    @Test
    void bob_does_not_see_createPayment_button_and_can_logout() {
        login("bob", "password");
        openDashboardAndWaitReady();

        boolean visible = awaitCreatePaymentVisibleState(false);
        assertThat(visible).isFalse();

        logoutAndAssertLoginPage();
    }

    private WebDriver createDriver() throws Exception {
        ChromeOptions options = new ChromeOptions();
        if (headless) options.addArguments("--headless=new");

        if ("remote".equalsIgnoreCase(driverMode)) {
            // Remote runs in container; you won't see a local window.
            return new RemoteWebDriver(new URL(remoteUrl), options);
        }
        return new ChromeDriver(options);
    }

    private void login(String username, String password) {
        driver.get(baseUrl + "/login");
        pause();

        WebElement u = driver.findElement(By.name("username"));
        WebElement p = driver.findElement(By.name("password"));
        u.clear();
        u.sendKeys(username);
        pause();
        p.clear();
        p.sendKeys(password);
        pause();

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        pause();

        // Wait until we leave /login (don’t over-assume exact next URL)
        new WebDriverWait(driver, TIMEOUT)
                .until(d -> !d.getCurrentUrl().contains("/login"));
    }

    private void openDashboardAndWaitReady() {
        driver.get(baseUrl + "/zul/dashboard.zul");
        pause();

        waitForDomReady();

        // ZK marker exists in your zul: <label ... sclass="dashboard-ready"/>
        new WebDriverWait(driver, TIMEOUT).until(d -> {
            List<WebElement> ready = d.findElements(By.cssSelector(".dashboard-ready"));
            return !ready.isEmpty() && ready.get(0).isDisplayed();
        });

        pause();
    }

    /**
     * Evaluate the button state after the page is ready.
     * - alice: eventually becomes visible
     * - bob: eventually absent or hidden
     *
     * This avoids "wait until visible" before ZK finishes rendering.
     */
    private boolean awaitCreatePaymentVisibleState(boolean expectedVisible) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        wait.until(d -> {
            boolean visibleNow = isCreatePaymentVisibleNow(d);
            return expectedVisible ? visibleNow : !visibleNow;
        });

        pause();
        return isCreatePaymentVisibleNow(driver);
    }

    private boolean isCreatePaymentVisibleNow(WebDriver d) {
        List<WebElement> buttons = d.findElements(By.cssSelector("#createPaymentBtn, .create-payment"));
        // If bob: composer may remove it OR hide it => visible should be false in both cases
        return buttons.stream().anyMatch(WebElement::isDisplayed);
    }

    /**
     * Logout coverage:
     * - click the link if possible
     * - fallback: direct GET /logout
     * - assert login page by presence of username field
     */
    private void logoutAndAssertLoginPage() {
        String before = driver.getCurrentUrl();

        WebElement logoutLink = findFirst(
                By.cssSelector("a[href='/logout'], a[href$='/logout']"),
                By.linkText("Logout")
        );

        if (logoutLink != null) {
            scrollIntoView(logoutLink);
            pause();
            safeClick(logoutLink);
            pause();
        } else {
            // If link not found for any reason, still try direct logout
            driver.get(baseUrl + "/logout");
            pause();
        }

        // If click didn’t trigger navigation (rare), force the logout URL
        if (!isOnLoginPage()) {
            driver.get(baseUrl + "/logout");
            pause();
        }

        new WebDriverWait(driver, TIMEOUT)
                .until(d -> !d.findElements(By.name("username")).isEmpty());

        assertThat(driver.findElements(By.name("username"))).isNotEmpty();
        assertThat(driver.getCurrentUrl())
                .as("Expected to reach login page after logout. Before=%s, After=%s"
                        .formatted(before, driver.getCurrentUrl()))
                .contains("/login");
    }


    private boolean isOnLoginPage() {
        return !driver.findElements(By.name("username")).isEmpty()
                || driver.getCurrentUrl().contains("/login");
    }

    private void waitForDomReady() {
        new WebDriverWait(driver, TIMEOUT).until(d -> {
            try {
                Object state = ((JavascriptExecutor) d).executeScript("return document.readyState");
                return "complete".equals(state);
            } catch (Exception e) {
                return false;
            }
        });
    }

    private WebElement findFirst(By... locators) {
        for (By locator : locators) {
            List<WebElement> found = driver.findElements(locator);
            if (!found.isEmpty()) return found.get(0);
        }
        return null;
    }

    private void scrollIntoView(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el
            );
        } catch (Exception ignored) {
        }
    }

    private void safeClick(WebElement el) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(d -> el.isDisplayed() && el.isEnabled());
            el.click();
            return;
        } catch (ElementNotInteractableException e) {
            // fall through to JS click
        } catch (WebDriverException e) {
            // fall through to JS click (intercepted/stale/etc.)
        }

        jsClick(el);
    }

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void pause() {
        if (SLOW_MS <= 0) return;
        try {
            Thread.sleep(SLOW_MS);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
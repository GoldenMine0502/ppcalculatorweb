package kr.goldenmine.ppcalculatorweb.osuapi.api.selenium

import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities
import java.io.File
import java.util.*
import kotlin.system.exitProcess

object SeleniumClient {
    init {

    }


}

fun downloadReplays(id: String, pw: String, folder: File, scoreIds: List<Long>, debug: Boolean = true) {
    val prefs: MutableMap<String, Any> = HashMap()
    prefs["download.default_directory"] = folder.absolutePath
    val caps = DesiredCapabilities.chrome()
    val options = ChromeOptions()
    options.setExperimentalOption("prefs", prefs)
    if (!debug) {
        options.addArguments("--headless")
    }

    caps.setCapability("goog:chromeOptions", options)

    val driver: WebDriver = ChromeDriver(options)
    Runtime.getRuntime().addShutdownHook(object : Thread() {
        override fun run() {
            try {
                driver.quit()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    })

    driver.manage().window().size = Dimension(1920, 1000)
    login(driver, id, pw)

    for(scoreId in scoreIds) {
        val link = "https://osu.ppy.sh/scores/osu/$scoreId"

        driver[link]

        Thread.sleep(800L)
        driver.findElements(By.className("score-buttons")).filter { it.text.contains("리플레이 다운로드") }.forEach {
            it.click()
        }
        Thread.sleep(800L)
    }

    driver.close()
    driver.quit()
}

fun mapCrawling(link: String, id: String, pw: String, debug: Boolean = true, maxCount: Int = -1): Set<Int> {
    val options = ChromeOptions()
    if (!debug) {
        options.addArguments("--headless")
    }
    val driver: WebDriver = ChromeDriver(options)
    Runtime.getRuntime().addShutdownHook(object : Thread() {
        override fun run() {
            try {
                driver.quit()
            } catch (ex: Exception) {
            }
        }
    })
    driver.manage().window().size = Dimension(1920, 1000)
    login(driver, id, pw)

    driver[link]
    Thread.sleep(2000L)

    val maps = HashSet<Int>()

    val jsx = driver as JavascriptExecutor

    val element_osuFileList = driver.findElement(By.className("osu-layout__row")).findElement(By.className("beatmapsets__content"))
    val element_boxInfo = element_osuFileList.findElement(By.tagName("div"))

    var lastPaddingTop = -1
    var lastUpdatePaddingTop = -1

    var equalTime = 0

    var overlap = true
    val start = System.currentTimeMillis()
    jsx.executeScript("window.scrollBy(0, 2000)")
    jsx.executeScript("window.scrollBy(0," + Random().nextInt(1000) + ")")
    println("started")
    while (equalTime <= 150) {
        Thread.sleep(8L)
        var edited = false
        val currentPaddingTop = getPaddingTopLen(element_boxInfo.getAttribute("style"))
        if (lastPaddingTop == currentPaddingTop) {
            ++equalTime
            if (overlap) {
                jsx.executeScript("window.scrollBy(0, " + (Random().nextInt(1300) + 255) + ")")
                overlap = false
                println("scroll$equalTime")
            } else {
                jsx.executeScript("window.scrollBy(0, " + (Random().nextInt(205) + 50) + ")")
                println("edit overlap$equalTime")
                Thread.sleep(150L)
            }
        } else {
            equalTime = 0
            lastPaddingTop = currentPaddingTop
            if (currentPaddingTop - lastUpdatePaddingTop >= 1000) {
                lastUpdatePaddingTop = lastPaddingTop
                edited = true
            }
        }
        if (edited) {
            element_osuFileList.findElements(By.className("beatmapsets__items")).forEach {element_beatmap ->
                element_beatmap.findElements(By.className("beatmapsets__items-row")).forEach {element_beatmap_row ->
                    element_beatmap_row.findElements(By.className("beatmapsets__item")).forEach { element_beatmap_item ->
                        try {
                            var href = element_beatmap_item.findElement(By.className("beatmapset-panel__content"))
                                    .findElement(By.tagName("a")).getAttribute("href")
                            href = href.substring(href.lastIndexOf("/") + 1, href.length)

                            val ranked = href.toInt()
                            if (!maps.contains(ranked)) {
                                maps.add(ranked)
                                if(maxCount != -1 && maps.size >= maxCount) {
                                    driver.close()
                                    driver.quit()
                                    return maps
                                }
                                println(ranked.toString() + ", " + maps.size + ", " + (System.currentTimeMillis() - start) / 1000.0)
                            } else {
                                overlap = true
                            }
                        } catch(ex: Exception) {
//                            ex.printStackTrace()
                            println("cant find beatmapset-panel__content")
                        }
                    }
                }
            }
        }
    }
    driver.close()
    driver.quit()

    return maps
}

fun login(driver: WebDriver, id: String, pw: String) {
    driver["https://osu.ppy.sh/home"]
    val loginButtonElement = driver.findElement(By.className("js-user-login--menu"))
    Thread.sleep(1000L)
    loginButtonElement.click()
    var form_loginId: WebElement? = null
    var form_loginPw: WebElement? = null
    var form_loginSubmit: WebElement? = null
    for (el in driver.findElements(By.className("login-box__form-input"))) {
        val placeholder = el.getAttribute("name")
        if (placeholder != null) {
            if (placeholder == "username") {
                form_loginId = el
            }
            if (placeholder == "password") {
                form_loginPw = el
            }
        }
    }
    for (el in driver.findElements(By.className("login-box__action"))) {
        try {
            if (el.findElement(By.tagName("button")) != null) {
                form_loginSubmit = el
                break
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("A")
        }
    }
    Thread.sleep(1000L)
    if(form_loginId != null)
        sendKeys(form_loginId, id, 50, 10)
    else {
        println("cant find loginid field, please restart the application")
        exitProcess(2)
    }

    Thread.sleep(1000L)
    if(form_loginPw != null)
        sendKeys(form_loginPw, pw, 50, 10)
    else {
        println("cant find loginpw field, please restart the application")
        exitProcess(3)
    }
    Thread.sleep(1000L)
    form_loginSubmit!!.click()
    Thread.sleep(5000L)
}

fun getPaddingTopLen(style: String): Int {
    val list = style.split(";[ |]".toRegex()).toTypedArray()
    list[list.size - 1] = list[list.size - 1].substring(0, list[list.size - 1].length - 1)
    for (s in list) {
        val keyValue = s.split(":[ |]".toRegex()).toTypedArray()
        if (keyValue[0] == "padding-top") {
            return parseIntNoChar(keyValue[1])
        }
    }
    return -1
}

fun parseIntNoChar(data: String): Int {
    val sb = StringBuilder()
    for (element in data) {
        if (element in '0'..'9') {
            sb.append(element)
        }
    }
    return sb.toString().toInt()
}

@Throws(InterruptedException::class)
fun sendKeys(element: WebElement, str: String, sleep: Int, updown: Int) {
    for (i in str.indices) {
        element.sendKeys(str[i].toString())
        Thread.sleep(sleep + (if (updown > 0) getRandom(-updown, updown) else 0).toLong())
    }
}

val r = Random()

fun getRandom(start: Int, finish: Int): Int {
    return r.nextInt(finish - start + 1) + start
}
#Program to implement beautiful soup and selenium
import io

#selenium drivers
from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
from time import sleep
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions as ec

#BeautifulSoup libraries
from bs4 import BeautifulSoup
from bs4 import SoupStrainer

driver = webdriver.Firefox() #new instance of firefox web driver
driver.get("http://www.magicbricks.com/property-for-sale-rent-in-Noida/residential-real-estate-Noida") #url to scrape data

print driver.title

documentHeight = driver.execute_script("return $(document).height();")

flag = True
while flag:
	windowHeight = driver.execute_script("return $(window).height();")
	windowScrollTop = driver.execute_script("return $(window).scrollTop()")
	print documentHeight
	print windowHeight
	print windowScrollTop
	if((windowScrollTop + windowHeight) == (4411 + 673)):
		flag = False
		break
	else:
		driver.execute_script("window.scrollBy(0, 500)")
		sleep(2)
		

#elementLink.click()
soup = BeautifulSoup(driver.page_source)
for link in soup.find_all('a', href = True):
	print link['href']

#usefulLinks = []
requiredDiv = soup.select("div.boxSize")
for reqLink in requiredDiv:
	html_link = reqLink.find_all("a", href = True)
	for usefulLink in html_link:
		print usefulLink.get_text()

#print usefulLinks




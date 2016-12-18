#Program to get the list of real estate developers in noida from the site : www.proptiger.com/noida
import io
import json

#libraries to form url
import urllib
from urlparse import urlparse

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

driver.get("https://www.proptiger.com/noida/all-builders?page=24") #url to scrape data

print driver.title

page_no = 28
fileName = open("BuildersListInNoida.json", "a")
dataBuilder = []
soup = BeautifulSoup(driver.page_source)
index = 0
requiredDiv = soup.select("div.b-card")
for link in requiredDiv:
	html_link = link.find_all('a', class_ = "no-ajaxy name put-ellipsis", href = True)
	for importantLink in html_link:
		dataBuilder.append({
				"PageNo" : page_no,
				"IndexVal" : index,
				"Title": importantLink.get_text(),
				"Url": "https://www.proptiger.com/noida" + importantLink['href'] 
			})
			"Title": importantLink.get_text(),
			"Index" : index,
			"Url": "https://www.proptiger.com/noida" + importantLink['href']
		print "Url : " + "https://www.proptiger.com/noida" + importantLink['href']
		print "Title : " + importantLink.get_text()	
		index += 1

json.dump(dataBuilder, fileName)

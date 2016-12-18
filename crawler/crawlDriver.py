#---Script to oopwn webdriver crawl and store result-----#

#importing io and json files
import io
import json

#importing libraries for selenium drivers
from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
from time import sleep
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions as ec

#importing beautiful soup library
from bs4 import BeautifulSoup
from bs4 import SoupStrainer

#importing self mad module to form url
import makeUrl

#storing data in file
fileName = open("BuildersList.json", "a")
builderData = [] #list to store builder information


def scrapeDataUsingBS(page_source, page_no):
	index = 0
	soup = BeautifulSoup(page_source)
	requiredDiv = soup.select("div.b-card")
	for link in requiredDiv:
		html_link = link.find_all('a', class_ = "no-ajaxy name put-ellipsis", href = True)
        	for importantLink in html_link:
                	builderData.append({
                        	"PageNo" : page_no,
                                "IndexVal" : index,
                                "Title": importantLink.get_text(),
                                "Url": "https://www.proptiger.com/noida" + importantLink['href']
                        })
                        print "Url : " + "https://www.proptiger.com/noida" + importantLink['href']
                	print "Title : " + importantLink.get_text()
                	index += 1
	print builderData

def openWebDriver():
	page_no = 1
	while(page_no <= 28):
		driver = webdriver.Firefox() #new instance of firefox web driver
		url = makeUrl.formUrl(page_no)
		driver.get(url)
		print driver.title
		scrapeDataUsingBS(driver.page_source, page_no)
		driver.quit()
		page_no += 1;
		
	print "Opened All Pages...."

openWebDriver()
json.dump(builderData, fileName)

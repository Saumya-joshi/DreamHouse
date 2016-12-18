#importing library
import io
import json
import getpass #to get user password
import cssutils #to process style sheet of page

#importing selenium library
from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
from time import sleep
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions as ec
from selenium.common.exceptions import NoSuchElementException

#importing beautiful soup library
from bs4 import BeautifulSoup
from bs4 import SoupStrainer

#importing pretty printer
from pprint import pprint

HousesLink = []

def getPropertyLink(page_source, type_property, type_buy):
        soupProperty = BeautifulSoup(page_source, 'html5lib')
        divPropertyInfoContainer = soupProperty.find_all('div', attrs = {"class" : "srpBlockListRow"})
        for info in divPropertyInfoContainer:
                heading = info.find_all('p', attrs = {"class" : "proHeading"})
                for data in heading:
                        links = data.find_all('a', href = True)
                for link in links:
                        print link.get_text()
                        print link['href']
                        propertyURL = 'http://www.magicbricks.com' + link['href']
                        print propertyURL

                try:
                        propertyImgUrlStyle = info.find('div', attrs = {"class" : "thumbnailBG"})['style']
                        style = cssutils.parseStyle(propertyImgUrlStyle)
                        propertyImgUrl = style['background-image']
                        propertyImgUrl = propertyImgUrl.replace('url(', '').replace(')', '')
                        print propertyImgUrl
                        imgURL = propertyImgUrl
                        HousesLink.append({
                        	"PropertyUrl" : propertyURL,
                                "PropertyImg" : imgURL,
                                "BuyType" : type_buy
                                })
		except:
			HousesLink.append({
                                "PropertyUrl" : propertyURL,
                                "PropertyImg" : '',
                                "BuyType" : type_buy
                                })
                        
	                                                                          

def storePropertyLink(type_property, type_buy, url):
        options = webdriver.ChromeOptions()
        options.add_argument("--start-maximized")
        path = '/usr/PriyanshuSinha/B.TECH(2014-2018)/V-Sem/MINOR/crawler/chromedriver'
        magicBrickDriver = webdriver.Chrome(executable_path = path, chrome_options = options)
        magicBrickDriver.get(url)
        print magicBrickDriver.title
        sleep(5)
        while True:
                try:
                        viewMore = magicBrickDriver.find_element_by_xpath('//*[@id="viewMoreButton"]/a')
                        print viewMore.text
                        innerHeight = magicBrickDriver.execute_script("return window.innerHeight") #java script
                        scroll_y = magicBrickDriver.execute_script("return window.scrollY") #java script
                        offsetHeight = magicBrickDriver.execute_script("return document.body.offsetHeight") #java script
                        print innerHeight
                        print scroll_y
                        print offsetHeight
                        magicBrickDriver.execute_script("arguments[0].scrollIntoView(true);", viewMore)
                        magicBrickDriver.execute_script("arguments[0].click();", viewMore)
                        sleep(7)
                        if viewMore.text == '':
                                break
                except NoSuchElementException:
                        break

        getPropertyLink(magicBrickDriver.page_source, type_property, type_buy)
        magicBrickDriver.quit()


def loadFromFile():
	fileSale = open("Property_for_rent.json", "r")
	data = json.load(fileSale)
	for index in data:
		if index['linkName'] == 'Houses for rent in Noida':
			storePropertyLink('Houses', 'Rent', index['linkUrl'])

def main():
	loadFromFile()
	fileHouse = open("HouseRent.json", "a")
	json.dump(HousesLink, fileHouse)
	print 'Successfully written to file'

main()


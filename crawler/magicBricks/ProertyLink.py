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

rentLink = [] #list to store link of properties for rent
saleLink = [] #list to store link of properties for sale

def getUrls(page_souce):
        soupMB = BeautifulSoup(page_souce, 'html5lib') #html5lib parser is used for parsing data
        divSaleContainer = soupMB.find_all('div', attrs = {"class" : "boxSize color_1 card_shadow"})
        for div in divSaleContainer:
                heading = div.find('h4', attrs = {"class" : "font-type-3 fo_16px text-uppercase"})
                #print heading.get_text()
                ul = div.find('ul')
                #print ul
                li = div.find_all('li')
                for links in li:
                        link = links.find('a')
                        linkName = link.get_text() #Text of link
                        linkUrl = 'http://www.magicbricks.com' + link['href']
                        saleLink.append({
                                'linkName' : linkName,
                                'linkUrl' : linkUrl
                        }) #making a dictionary to for having key value pair(link text & url)
                        #print linkName
                        #print linkUrl

        divRentContainer = soupMB.find_all('div', attrs = {"class" : "boxSize color_2 card_shadow"})
        for div in divRentContainer:
                heading = div.find('h4', attrs = {"class" : "font-type-3 fo_16px text-uppercase"})
                #print heading.get_text()
                ul = div.find('ul')
                #print ul
                li = div.find_all('li')
                for links in li:
                        link = links.find('a')
                        linkName = link.get_text() #Text of link
                        linkUrl = 'http://www.magicbricks.com' + link['href']
                        rentLink.append({
                                'linkName' : linkName,
                                'linkUrl' : linkUrl
                        }) #making a dictionary to for having key value pair(link text & url)
                        #print linkName
                        #print linkUrl


def crawlMB():
        url = 'http://www.magicbricks.com/property-for-sale-rent-in-Noida/residential-real-estate-Noida' # We are considering noida only for our project. Later on we can add various cities. 
        magicBrickDriver = webdriver.Firefox() #opening instance of firefox
        magicBrickDriver.maximize_window() #maximizing the size of firefox window
        magicBrickDriver.get(url)
        print magicBrickDriver.title
        #scroling down to the bottom of page
        magicBrickDriver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        sleep(5) #waiting for 5 seconds to for user to view
        getUrls(magicBrickDriver.page_source) #passing page source
        magicBrickDriver.quit()

def main():
	crawlMB()
	filePropertySale = open("Property_for_sale.json", "a")
	filePropertyRent = open("Property_for_rent.json", "a")
	json.dump(saleLink, filePropertySale)
	json.dump(rentLink, filePropertyRent)
	print 'Successfully written to file'

main()

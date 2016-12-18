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
flatsLink = [] #list to store link of all flats on magic bricks
housesLink = [] #list to store houses Links
villasLink = [] #list to store villas links
pgsLink = [] #list to store pgs link

#type_property -> whether property is flat, houses, villas or pgs
#type_buy -> whether property is for sale or rent
 
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
			if type_property == 'Flat':
				flatsLink.append({
					"PropertyUrl" : propertyURL,
					"PropertyImg" : imgURL,
					"BuyType" : type_buy
				})

			elif type_property == 'Houses':
				housesLink.append({
                        		"PropertyUrl" : propertyURL,
                        		"PropertyImg" : imgURL,
                        		"BuyType" : type_buy
                		})

			elif type_property == 'Villas':
				villasLink.append({
                        		"PropertyUrl" : propertyURL,
                        		"PropertyImg" : imgURL,
                        		"BuyType" : type_buy
                		})
	
			elif type_property == 'PG':
				pgsLink.append({
                        		"PropertyUrl" : propertyURL,
                        		"PropertyImg" : imgURL,
                        		"BuyType" : type_buy
                		})
		
		except:
			pass
		

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

def processSaleLink():
	for index in saleLink:
                if index['linkName'] == 'Flats in Noida':
                        storePropertyLink('Flat', 'Sale', index['linkUrl'])
                elif index['linkName'] == 'Houses in Noida':
                        storePropertyLink('Houses', 'Sale', index['linkUrl'])
                elif index['linkName'] == 'Villas in Noida':
                        storePropertyLink('Villas', 'Sale', index['linkUrl'])

def processRentLink():
	for index in rentLink:
                if index['linkName'] == 'Flats for rent in Noida':
                        storePropertyLink('Flat', 'Rent', index['linkUrl'])
                elif index['linkName'] == 'Houses for rent in Noida':
                        storePropertyLink('Houses', 'Rent', index['linkUrl'])
                elif index['linkName'] == 'PGs for rent in Noida':
                        storePropertyLink('PG', 'Rent', index['linkUrl'])

def storeInFile():
	fileFlat = open("Flats.json", "a")
	json.dump(flatsLink, fileFlat)
	fileHouses = open("Houses.json", "a")
	josn.dump(housesLink, fileHouses)
	fileVillas = open("Villas.json", "a")
	json.dump(villasLink, fileVillas)
	filePG = open("PG.json", "a")
	json.dump(pgsLink, filePG)

def main():
	crawlMB()
	#processing rent links
	processRentLink()
	#processing sale links
	processSaleLink()
	#storing in file
	storeInFile()
	#for index in saleLink:
        #        if index['linkName'] == 'Flats in Noida':
        #                storePropertyLink('Flat', 'Sale', index['linkUrl'])

main() 

print flatsLink
print villasLink
print housesLink
print pgsLink

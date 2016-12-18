#importing library
import io
import json
import getpass #to get user password
import cssutils #to process style sheet of page
import signal #to handle signal
import urllib #to open web url
import requests 
import unicodedata
from decimal import *
from random import randint

#importing beautiful soup library
from bs4 import BeautifulSoup
from bs4 import SoupStrainer

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
from tbselenium.tbdriver import TorBrowserDriver

#importing pretty printer
from pprint import pprint

#importing mysql
import MySQLdb
import uuid

flatURL = [] #to store link

flatData = [] #to store data

hostName = 'localhost'
userName = 'root'
password = 'pri2si17'
databaseName = 'dreamHouse'

getcontext().prec = 12

def makeDBConnection():
	try:
		dbConn = MySQLdb.connect(hostName, userName, password, databaseName)
                print 'Success'
                return dbConn

	except:
		print 'Error while Connecting'


def insertIntoProjects(dbConn, project_name, builder_id):
	cursor = dbConn.cursor()
	sql_check = "SELECT COUNT(*) FROM real_estates_project where project_name = '%s'" % (project_name)
	cursor.execute(sql_check)
	result_count = cursor.fetchone()
	count = result_count[0]
	if(count == 0):
		project_id = uuid.uuid4()
		sql_query = "INSERT INTO real_estates_project(project_id, project_name) values('%s', '%s')" % (project_id, project_name)
		cursor.execute(sql_query)
		dbConn.commit()
		print 'Successfully inserted in projects.'
		insertIntoBuilderProjects(dbConn, builder_id, project_id)
	else:
		sql_get_project_id = "SELECT project_id from real_estates_project where project_name = '%s'" % (project_name)
                cursor.execute(sql_get_project_id)
                resultSet = cursor.fetchone()
                project_id = resultSet[0]
                print project_id
                insertIntoBuilderProjects(dbConn, builder_id, project_id)

def insertIntoBuilder(dbConn, builder_name, project_name):
	cursor = dbConn.cursor()
	sql_check = "SELECT COUNT(*) FROM mbBuilder where builder_name = '%s'" % (builder_name)
	cursor.execute(sql_check)
	result_count = cursor.fetchone()
	count =  result_count[0]
	if(count == 0):
		builder_id = uuid.uuid4()
        	sql_query = "INSERT INTO mbBuilder(builder_id, builder_name) values('%s', '%s')" % (builder_id, builder_name)
        	cursor.execute(sql_query)
        	dbConn.commit()
        	print 'Successfully inserted in builders.'
		insertIntoProjects(dbConn, project_name, builder_id)	
	else:
		sql_get_builder_id = "SELECT builder_id from mbBuilder where builder_name = '%s'" % (builder_name)
		cursor.execute(sql_get_builder_id)
		resultSet = cursor.fetchone()
		builder_id = resultSet[0]
		print builder_id
		insertIntoProjects(dbConn, project_name, builder_id)
        
def insertIntoBuilderProjects(dbConn, builder_id, project_id):
	cursor = dbConn.cursor()
	sql_check = "SELECT COUNT(*) from builder_project_link where builder_id = '%s' and project_id = '%s'" % (builder_id, project_id)
	cursor.execute(sql_check)
	result_count = cursor.fetchone()
	count = result_count[0]
	print count
	if(count == 0):
        	sql_query = "INSERT INTO builder_project_link(builder_id, project_id) values('%s', '%s')" % (builder_id, project_id)
        	cursor.execute(sql_query)
        	dbConn.commit()
        	print 'Successfully inserted in builder_project_link.'
       		saveFlatData(dbConn, project_id)
	else:
		saveFlatData(dbConn, project_id)

def saveFlatData(dbConn, project_id):
	cursor = dbConn.cursor()
	sql_query_save = "INSERT INTO flat_data(property_url, image_url, buy_type, property_type, property_region, property_locality, lattitude, longitude, property_id, bhk, area, project_name, builder_name, currency_type, price) values('%s', '%s', '%s', '%s', '%s', '%s', '%f', '%f', '%s', '%d', '%d', '%s', '%s', '%s', '%s')" % (flatData[0]['propertyUrl'], flatData[0]['propertyImage'], flatData[0]['BuyType'], flatData[0]['PropertyType'], flatData[0]['PropertyRegion'], flatData[0]['PropertyLocality'], flatData[0]['Latitude'], flatData[0]['Longitude'], flatData[0]['PropertyID'], flatData[0]['PropertyBHK'], flatData[0]['PropertyArea'], flatData[0]['PropertyProjectName'], flatData[0]['PropertyBuilderName'], flatData[0]['CurrencyType'], flatData[0]['PropertyPrice'])
	cursor.execute(sql_query_save)
	dbConn.commit()
	print 'Successfully Saved Data'
	makeProjectPropertyLink(dbConn, flatData[0]['PropertyID'], project_id)

def makeProjectPropertyLink(dbConn, property_id, project_id):
	cursor = dbConn.cursor()
	sql_query = "INSERT INTO property_project_link(project_id, property_id) values('%s', '%s')" % (project_id, property_id)
	try:
		cursor.execute(sql_query)
		dbConn.commit()
		print 'Successfully created link'
	except:
		dbConn.rollback()

	dbConn.close()

def getLinkFromFile():
	fileSale = open('FlatSale.json', 'r')
	data = json.load(fileSale)
	for index in data:
		flatURL.append({
			"url" : index['PropertyUrl'],
			"img" : index['PropertyImg'], 
			"type" : index['BuyType']})	


def scrapePage(pageSource, url, imgUrl, bType):
	PropertySoup = BeautifulSoup(pageSource, 'html5lib')
	#print PropertySoup.prettify()
	DivnOverview11 = PropertySoup.find_all('div', attrs = {"class" : "nOverview11"})
	#print DivnOverview11
	for nOverview11 in DivnOverview11:
		#-----To get Property Type-----#
		DivnOverviewnBlockBorder = nOverview11.find_all('div', attrs = {"class" : "nOverview nBlockBorder"})
		#print DivnOverviewnBlockBorder
		for data in DivnOverviewnBlockBorder:
			DivPhtotoBlock = data.find_all('div', attrs = {"class" : "phtotoBlock"})
			for nPropVerify in DivPhtotoBlock:
				DivPropVerify = nPropVerify.find_all('div', attrs = {"class" : "nPropVerify"})
				for pType in DivPropVerify:
					DivpType = pType.find_all('div', attrs = {"class" : "nPType"})
					for span in DivpType:
						PropertyType = span.find_all('span', attrs = {"class" : "pTypePlatinum"})
						for propertyType in PropertyType:
							pType = propertyType.get_text()
							#print pType
			DivAddress = data.find_all('div', attrs = {"itemtype" : "http://schema.org/ApartmentComplex"})
			#print DivAddress
			for divs in DivAddress:
				DivPostalAddress = divs.find_all('div', attrs = {"itemtype" : "http://schema.org/PostalAddress", "itemprop" : "address"})
				#for spans in DivPostalAddress:
				#	addressLocality = spans.find_all('span', attrs = {"itemprop" : "addressLocality"})
				#	addressRegion = spans.find_all('span', attrs = {"itemprop" : "addressRegion"})
				#	print addressLocality
				#	print addressRegion
				
				for inputs in DivPostalAddress:
					addressLocality = inputs.find('input', attrs = {"type" : "hidden", "itemprop" : "addressLocality"}).get('value')
					addressRegion = inputs.find('input', attrs = {"type" : "hidden", "itemprop" : "addressRegion"}).get('value')
					#print addressLocality
					#print addressRegion

				DivGeoAddress = divs.find_all('div', attrs = {"itemtype" : "http://schema.org/GeoCoordinates", "itemprop" : "geo"})
				for inputs in DivGeoAddress:
					longitude = Decimal(inputs.find('input', attrs = {"type" : "hidden", "itemprop" : "longitude"}).get('value'))
					latitude = Decimal(inputs.find('input', attrs = {"type" : "hidden", "itemprop" : "latitude"}).get('value'))
					#print longitude
					#print latitude
			DivDetails = data.find_all('div', attrs = {"class" : "nDatabock ctaFixedBtn"})
			for divs in DivDetails:
				propIDnPDate = divs.find('div', attrs = {"class" : "propIDnPDate"}).get_text()
				propertyIdL = propIDnPDate.split('|', 1)
				#print propIDnPDate
				#print propertyIdL[0]
				propertyId = propertyIdL[0].split(':')
				pID = propertyId[1]
				#print pID
				bxdetls = divs.find_all('div', attrs = {"class" : "bxdetls"})
				for details in bxdetls:
					baseNpriceInfo = details.find_all('h1', attrs = {"class" : "baseNpriceInfo"})
					#print baseNpriceInfo
					for priceInfo in baseNpriceInfo:
						bhkAreaDtls = priceInfo.find('div', attrs = {"class" : "priceInfo"}).get_text()
						#print bhkAreaDtls
						bhkAreaDtlsL = bhkAreaDtls.split('\n')
						#print bhkAreaDtlsL[0]
						BHK = bhkAreaDtlsL[0].split(' ')
						pBHK = int(BHK[0])
						#print pBHK
						#print bhkAreaDtlsL[1]
						Area = bhkAreaDtlsL[1].split(' ')
						pAREA = int(Area[0])
						#print pAREA
						propLocDtls = priceInfo.find_all('div', attrs = {"class" : "nProjNmLoc"})
						for dtls in propLocDtls:
							a = dtls.find_all('a', href = True)
							for info in a:
								ProjectName = info.get_text()
								ProjectUrl = info['href']
								#print ProjectName
								#print ProjectUrl
							BuilderName = dtls.find('div', attrs = {"class" : "nBuilderNme"}).get_text()
							pbuilderName = BuilderName[3:]
							#print BuilderName
							#print pbuilderName
					newPriceBlock = details.find_all('div', attrs = {"class" : "newPriceBlock"})
					for info in newPriceBlock:
						divContainer = info.find_all('div', attrs = {"itemtype" : "http://schema.org/Offer"})
						for index in divContainer:
							currencyType = index.find('meta', attrs = {"itemprop" : "priceCurrency"}).get('content')
							#print currencyType
							totalPrice = index.find('input', attrs = {"type" : "hidden", "itemprop" : "price"}).get('value')
							#print totalPrice
						actualPriceContainer = info.find_all('div', attrs = {"class" : "nActualAmt"})
						actualPrice = actualPriceContainer[1].get_text()
						#print actualPrice
						nEMIPrice = info.find_all('div', attrs = {"class" : "nEMIblock"})
						for index in nEMIPrice:
							pricePerSqFt = index.find('span').get_text()
							actualPrice = unicodedata.normalize("NFKD", pricePerSqFt) #normailizing unicode data
							#print actualPrice
					propDetails = details.find_all('div', attrs = {"class" : "nPriceDetails"})
					for divs in propDetails:
						div_nInfoDataBlock = divs.find_all('div', attrs = {"class" : "nInfoDataBlock"})
						for divs in div_nInfoDataBlock:
							div_nDataRow = divs.find_all('div', attrs = {"class" : "nDataRow"})
							for divs in div_nDataRow:
								dataLabel = divs.find('div', attrs = {"class" : "dataLabel"}).get_text()
								dataValue = divs.find('div', attrs = {"class" : "dataVal"}).get_text()
								#print dataLabel
								#print dataValue
	 	
		Div_nBlockBordernOverview = nOverview11.find_all('div', attrs = {"class" : "nBlockBorder nOverview"})
		for divs in Div_nBlockBordernOverview:
			div_nAboutMew100 = divs.find_all('div', attrs = {"class" : "nAboutMe w100"})	
			for divsI in div_nAboutMew100:
				divHeading = divsI.find('div', attrs = {"class" : "nBlockHeading nlAboutTop"}).get_text()
				#print divHeading
				div_nAboutBrf = divsI.find_all('div', attrs = {"class" : "nAboutBrf"})
				#print div_nAboutBrf
				for spans in div_nAboutBrf:
					getSpan = spans.find_all('span', attrs = {"class" : "dDetail"})
					for value in getSpan:
						brfAbt = value.get_text()
						#print brfAbt	
			div_nMoreListData = divs.find_all('div', attrs = {"class" : "nMoreListData"})
			for divsII in div_nMoreListData:
				div_nDataRow = divsII.find_all('div', attrs = {"class" : "nDataRow"})
				for results in div_nDataRow:
					try:
						DataLabel = results.find('div', attrs = {"class" : "dataLabel"}).get_text()
						#print DataLabel
						DataValue = results.find('div', attrs = {"class" : "dataVal"}).get_text()
						#print DataValue
					except AttributeError:
						pass
	
	#print url
	#print imgUrl
	#print bType
	#print pType
	#print addressRegion
	#print addressLocality
	#print longitude
	#print latitude
	#print pID
	#print pBHK
	#print pAREA
	#print ProjectName
	#print pbuilderName
	#print currencyType
	#print totalPrice
	flatData.append({
		"propertyUrl" : url,
		"propertyImage" : imgUrl,
		"BuyType" : bType,
		"PropertyType" : pType,
		"PropertyRegion" : addressRegion,
		"PropertyLocality" : addressLocality,
		"Latitude" : latitude,
		"Longitude" : longitude,
		"PropertyID" : pID,
		"PropertyBHK" : pBHK,
		"PropertyArea" : pAREA,
		"PropertyProjectName" : ProjectName,
		"PropertyBuilderName" : pbuilderName,
		"CurrencyType" : currencyType,
		"PropertyPrice" : totalPrice})

	#print flatData
	dbConn = makeDBConnection()
	insertIntoBuilder(dbConn, pbuilderName, ProjectName)
	#dbConn = makeDBConnection()
	#saveFlatData(dbConn)
			
def loadPage(index, url, imgUrl, bType):
	print index
	result = requests.get(url) #load the url
	pageSource = result.content #get the page data
	status = result.status_code #get stauts code. If success code is 200
	print status
	if status == 200:
		scrapePage(pageSource, url, imgUrl, bType)
	else:
		print 'Error while fetching page'
	#options = webdriver.ChromeOptions()
        #options.add_argument("--start-maximized")
        #path = '/usr/PriyanshuSinha/B.TECH(2014-2018)/V-Sem/MINOR/crawler/chromedriver'
        #magicBrickDriver = webdriver.Chrome(executable_path = path, chrome_options = options)
        #magicBrickDriver.get(url)
        #print magicBrickDriver.title
        #sleep(25)
	#scrapePage(magicBrickDriver.page_source, url, imgUrl, bType)
	#sleep(2)
	#magicBrickDriver.quit()
	#with TorBrowserDriver("/usr/PriyanshuSinha/B.TECH(2014-2018)/V-Sem/MINOR/crawler/magicBricks/tor-browser_en-US/") as driver:
    	#	driver.get(url)
	#	scrapePage(driver.page_source, url, imgUrl, bType)
	#	driver.quit()		
	
def main():
	getLinkFromFile()
	print len(flatURL)
	value = randint(106, len(flatURL))
	print value
	
	#print flatURL[value]['url']

	loadPage(169, flatURL[205]['url'], flatURL[205]['img'], flatURL[205]['type'])
	#print flatURL[value]['url']

main()

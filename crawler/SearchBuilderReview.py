#---------Python Script to search each builder on google and visit the link----------------#
#Importing io and json library
import io
import json

#importing seleinum library
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

#importing pretty printer(prints data in formatted order)
from pprint import pprint

#browser key control
#CTRL+T --> New Tab
#CTRL+TAB --> Switching Tab
#context_click --> Right mouse click
#ARROW_DOWN --> Down key
#ENTER --> Carriage Return

#opening json file to read builder names
fileName = open("BuildersList.json", "r")

#list to store the builders name
builderName = []

def getBuildersName():
	fileBuilder = open("Builders.txt", "a")
	Data = json.load(fileName)
	#pprint(Data)
	for title in Data:
		builderName.append(title['Title'])
		fileBuilder.write("%s\n" %title['Title'])
	

getBuildersName()

#print builderName
#print len(builderName)

review_data = set()
ratingBuilder = []

#extract review if there is 'Read More Link'
def extractReviewBS(page_source):
	#print 'Extract Review Called'
	soup = BeautifulSoup(page_source)
	reviewDiv = soup.find_all(itemprop="description")
	for review in reviewDiv:
		rev =  str(review.get_text())
		review_data.add(rev)
		print rev
	return True

#extract review if there is no 'Read More Link'
def extractReview(page_source):
	soup = BeautifulSoup(page_source)
	review_div = soup.find_all('div.summary')
	for review in review_div:
		rev = str(review.get_text())
		review_data.add(rev)
		print rev
	return True

def getRatingReviewCount(page_source, builderName):
	ReviewCount = None
	soup = BeautifulSoup(page_source)
	if(soup.find("ul", class_ = "prod-info")): 
		ratingReviewCount = soup.find_all("ul", class_ = "prod-info")
		for element in ratingReviewCount:
			divRatingReview = element.find_all(itemprop = "aggregateRating")
			for div in divRatingReview:
				rating_value = div.find("meta", itemprop = "ratingValue")['content']
				rating = str(rating_value)
				ratingBuilder.append({
					"BuilderName" : builderName,
					"Rating": rating
				})		
				totalReviews = div.find_all("span", itemprop = "reviewcount")
				for count in totalReviews:
					ReviewCount = count.get_text()
	#print ratingReviewCount.get_text()	
		return ReviewCount
	else:
		return False

def writeInFile(FileName):
	file_review = open(FileName + ".txt", "a")
	for reviews in review_data:
		file_review.write("%s\n" % reviews)
	file_review.close()

def googleBuilderReviews(index):
	print builderName[index]
	url = "https://www.google.co.in/" #open google to search
	driver = webdriver.Firefox() #openeing new instance of firefox.
	driver.maximize_window() #maximize the size of window

	#driver = webdriver.Chrome('/usr/PriyanshuSinha/B.TECH(2014-2018)/V-Sem/MINOR/crawler/chromedriver') #opening instance of chrome browser
	driver.get(url)
	action_chain = ActionChains(driver) #making action chains to perform actions in succession
	googleSearchResultWindow = driver.current_window_handle #saving the current state of tab
	#print driver.title
	getSearchBox = driver.find_element(By.NAME, "q") #'q' is name of input type search box
	searchQuery = builderName[index] + 'Builders Reviews'
	#print searchQuery
	getSearchBox.send_keys(searchQuery)
	getSearchBox.submit()
	WebDriverWait(driver, 30).until(ec.title_contains(searchQuery))
	#print driver.title
	divLinkClass = driver.find_element_by_css_selector('div.g')
	getLink = divLinkClass.find_element_by_tag_name("a")
	#print divLinkClass.text
	Link = getLink.get_attribute("href")
	action_chain.context_click(getLink).send_keys(Keys.ARROW_DOWN).send_keys(Keys.ENTER).send_keys(Keys.CONTROL + Keys.TAB)
	action_chain.perform()
	driver.switch_to_window(googleSearchResultWindow)
	totalReviews = getRatingReviewCount(driver.page_source, builderName[index])
	if(totalReviews == False):
		driver.quit()
	#totalCount = int(totalReviews)
	else:
		count = 0
		proceed = True
		while proceed:
			try:
				#selectDiv = driver.find_elements_by_tag_name('li')
				for div in driver.find_elements_by_tag_name('li'):
					get_li = div.find_elements_by_class_name('reviewdetails')
					for li in get_li:
						if(li.find_element_by_link_text('Read complete review')):
							getMoreReviewLink = li.find_element_by_link_text('Read complete review')
							get_link =  getMoreReviewLink.get_attribute("href")
							print get_link
							try:
								getMoreReviewLink.click()
								sleep(8)							
								driver.find_element_by_tag_name('body').send_keys(Keys.CONTROL + Keys.TAB)
								driver.switch_to_window(googleSearchResultWindow)
								print driver.title
								count += 1
								print count
								retVal = extractReviewBS(driver.page_source)
								sleep(10)
							except:
								pass
						else:
							retVal = extractReview(driver.page_source)
						if retVal:
							sleep(5)
							driver.find_element_by_tag_name('body').send_keys(Keys.CONTROL + 'w')
						driver.switch_to_window(googleSearchResultWindow)
				#driver.switch_to_window(googleSearchResultWindow)
				paging = driver.find_element_by_class_name('paging')
				next_page = paging.find_element_by_tag_name('span')
				linkNextPage = next_page.find_element_by_class_name('Next')
				nextLink = linkNextPage.get_attribute("href")
				print paging.text	
				print next_page.text
				print nextLink
				driver.execute_script("window.open('"+nextLink+"', '_parent');")
			except:
				print 'Finished All Pages'
				proceed = False
		print 'Out of While Loop'
		driver.quit()
		print 'Dumping Data to File'
		writeInFile(builderName[index])
		review_data.clear()

def main():
	totalBuilders = len(builderName)
	for index in range(0, len(builderName)-1):
		googleBuilderReviews(index)
	ratingFile = open("BuildersRating.json", "a")
	json.dump(ratingBuilder, ratingFile)

main()

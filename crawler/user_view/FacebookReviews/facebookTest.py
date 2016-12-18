#importing library
import io
import json
import getpass #to get user password

#importing selenium library
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

#importing pretty printer
from pprint import pprint

fb_rating_builder = []

def scrapeReviews(name, page_source):
	file_review = open(name + ".txt", "a")
	count = 0
	GaursonSoup = BeautifulSoup(page_source, "html5lib")
	#print GaursonSoup.prettify()
	divRating = GaursonSoup.find_all('div', attrs = {"class" : "_4uyi"})
	for rating in divRating:
		divInnerRating = GaursonSoup.find_all('div',  attrs={"class" : "_3-ma _2bne"})
		for value in divInnerRating:
			fb_rating_builder.append({
				"BuilderName" : name,
				"Rating": str(value.get_text())
			})
			print value.get_text()
	
	containerReview = GaursonSoup.find_all('div', attrs = {"class" : "_5pbx userContent"})
	for review in containerReview:
		#print 'inside_1'
		count += 1
		print count
		print review.get_text()
		userView = u' '.join(review.get_text()).encode('utf-8') #unicodeEncodeError
		file_review.write(userView+'\n'+'\n')
		

def crawlFB(name, fbURL):
	#userName = raw_input('User Name : ') #getting usename 
	#userPass = getpass.getpass('Password : ') #getting userpassword
	
	userName = 'pksinha181@rediffmail.com'
	userPass = 'ms dhoni born in 7 7 1981'
	driver = webdriver.Firefox()
	driver.maximize_window()
	url = 'https://www.facebook.com/'
	driver.get(url)
	print driver.title

	while True:
		elementEmail = driver.find_element_by_id("email")
		elementEmail.clear()
		elementEmail.send_keys(userName)

		elementPwd = driver.find_element_by_id("pass")
		elementPwd.clear()
		elementPwd.send_keys(userPass)

		elementPwd.send_keys(Keys.RETURN)
	
		print driver.title
		if driver.title == 'Log in to Facebook | Facebook':
			print 'You have entered wrong credentials.. Please Reenter!'
			userName = raw_input('User Name : ') #getting usename 
			userPass = getpass.getpass('Password : ') #getting userpassword

		else:
			break

	facebookWindow = driver.current_window_handle #facebook tab. Saving this window
	selectBody = driver.find_element_by_tag_name('body').send_keys(Keys.CONTROL + Keys.TAB)
	#driver.get('https://www.facebook.com/GaursonsIndiaLimited/reviews/?ref=page_internal')
	driver.get(fbURL)
	print driver.title

	#scrolling to the bottom of page
	while True:
		innerHeight = driver.execute_script("return window.innerHeight") #java script
		scroll_y = driver.execute_script("return window.scrollY") #java script
		offsetHeight = driver.execute_script("return document.body.offsetHeight") #java script

		print innerHeight
		print scroll_y
		print offsetHeight
		driver.execute_script("window.scrollTo(0, document.body.scrollHeight);") #java script
		sleep(2) #explicitly waiting for 5 seconds
		if innerHeight + scroll_y >= offsetHeight:
			break

	print 'At the bottom of page'
	scrapeReviews(name, driver.page_source)
	driver.quit()

def main():
	#crawlFB('Nitya','https://www.facebook.com/pg/TheNityaGroup/reviews/?ref=page_internal')
	arr = []
	fileFbLink = open("buildersFbUrl.json", "r")
	data = json.load(fileFbLink)
	for link in data:
		crawlFB(link['Name'], link['Url'])
	ratingFile = open("BuildersRating.json", "a")
        json.dump(fb_rating_builder, ratingFile)

main()

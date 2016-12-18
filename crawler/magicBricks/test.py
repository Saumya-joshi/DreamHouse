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

def loadFromFile():
        fileSale = open("FlatRent.json", "r")
        data = json.load(fileSale)
	print len(data)

loadFromFile()
        

#-------Script to form Url--------#
#importing necessary libraries to form url
import urllib
from urlparse import urlparse

def formUrl(page_no):
	#base url for crawlinhg builders name
	url = "https://www.proptiger.com/noida/all-builders"
	params = {'page' : page_no}
	url += ('&' if urlparse(url).query else '?') + urllib.urlencode(params)
	return url


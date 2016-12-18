import urllib

from urlparse import urlparse

url = "https://www.proptiger.com/noida/all-builders"

value = 28 

params = {'page':value}

url += ('&' if urlparse(url).query else '?') + urllib.urlencode(params)

print url

#importing libraries
import io
import json

buildersLink = [] #list to store name and link

fileFbLink = open("buildersFbUrl.json", "a")

def makeJSON(BuilderName, facebookLink):
	buildersLink.append({
		"Name" : BuilderName,
		"Url" : facebookLink
	})
	
def main():
	while True:
		name = raw_input('Enter Builder Name : ')
		url = raw_input('Enter Facebook Page Link : ')
		makeJSON(name, url)
		proceed = raw_input('Do you want to proceed? (y/n) ')
		if proceed == 'N' or proceed == 'n':
			break

main()
print buildersLink
json.dump(buildersLink, fileFbLink)	

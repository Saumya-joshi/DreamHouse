#importing library
import io
import json
import uuid

import MySQLdb

host = 'localhost'
user = 'root'
password = 'pri2si17'
database = 'dreamHouse'

list_builders = []

def getName():
	file_name = open("buildersFbUrl.json", "r")
	data = json.load(file_name)
	for name in data:
		list_builders.append(name['Name'])
	
def makeDBConn():
	try:
                dbConn = MySQLdb.connect(host, user, password, database)
                print 'Success'
                return dbConn
        except:
                print 'Error'

def saveInDB(DBConn, builder_id, data):
	cursor = DBConn.cursor()
        sql = "INSERT INTO builders(builder_id, builder_name) values ('%s', '%s')" % (builder_id, data)
        try:
                cursor.execute(sql)
                DBConn.commit()
                print 'Successfully inserted'
        except:
                DBConn.rollback()

        DBConn.close()


def main():
	getName()
	#print list_builders
	#print uuid.uuid4()
	#DBConn = makeDBConn()
	for index in range(0, len(list_builders)-1):
		DBConn = makeDBConn()
		builder_id = uuid.uuid4()
		saveInDB(DBConn, builder_id, list_builders[index])
	print 'Back to main'
	
main()

import MySQLdb

host = 'localhost'
user = 'root'
password = 'pri2si17'
database = 'testAndroid'

data = []

def ConnectToDB():
	try:
		db = MySQLdb.connect(host, user, password, database)
		print 'Success'
		return db
	except:
		print 'Error'

def insertData(DBConn):
	print data
	print data[0]
	print data[1]
	cursor = DBConn.cursor()
	sql = "INSERT INTO testPython(id, name) values ('%d', '%s')" % (data[0], data[1])
	try:
		cursor.execute(sql)
		DBConn.commit()
		print 'Successfully inserted'
	except:
		DBConn.rollback()

	DBConn.close()	

def main():
	DBConn = ConnectToDB()
	print DBConn
	cur = DBConn.cursor()
	cur.execute("SELECT VERSION()")
	ver = cur.fetchone()
	print ver
	Id = 1
	Name = 'Priyanshu'
	data.append(Id)
	data.append(Name)
	insertData(DBConn)
	print 'Back to main'	

main()

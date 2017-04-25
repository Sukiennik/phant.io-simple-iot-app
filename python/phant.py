import urllib
import urllib2
import sqlite3
import csv
import time

def writer_factory(endpoint, *plain, **converters):
    for p in plain:
        converters[p] = lambda x: x
    def committer(**values):
        converted = {}
        if set(converters.keys()) ^ set(values.keys()):
            raise TypeError('Value mismatch (expected: %s)' % (
                ', '.join(converters.keys())
            ))
        for k, v in values.items():
            try:
                converted[k] = converters[k](v)
            except ValueError:
                raise ValueError(
                    'Parameter "%s" can not be converted with "%s"' % (
                    k, converters[k].__name__
                ))
        url = endpoint + '&' + urllib.urlencode(converted)
        return bool(int(urllib2.urlopen(url).read(1)))
    return committer

def _safe(field):
    if set(field) & set('\\\'\x00"'):
        raise ValueError('Invalid field name: ' + field)
    field = field.encode("utf-8", 'strict').decode("utf-8")
    return '"%s"' % field

def first_read(url, name, conn=None, **converters):
    if conn is None:
        conn = sqlite3.connect(':memory:')
	
    try:
    	csv_reader = csv.DictReader(urllib2.urlopen(url + '.csv'))
    	fn = csv_reader.fieldnames
    except urllib2.HTTPError as err:
    	if err.code == 404:
    		print "\n##HTTP ERROR CODE 404 - Probably STREAM has been CLEARED!"
    		print "##SLEEPING FOR 1 SECOND!"
    		time.sleep(1)
    		return first_read(url, name, conn=None, **converters)
    	if err.code == 502:
    		print "\n##HTTP ERROR CODE 502 - Something went wrong during sending data from Phant WEBSERVER!!"
    		print "##SLEEPING FOR 1 SECOND AND REPEATING!"
    		time.sleep(1)
    		return first_read(url, name, conn=None, **converters)
    	if err.code == 503:
    		print "\n##HTTP ERROR CODE 503 - Phant WEBSERVER overloaded!"
    		print "##SLEEPING FOR 1 SECOND AND REPEATING!"
    		time.sleep(1)
    		return first_read(url, name, conn=None, **converters)
    	else:
    		raise

    conn.execute('CREATE TABLE %s (%s)' % (
        name, ', '.join([_safe(x) for x in fn])))
            	
    for row in csv_reader:
    	values = [converters.get(key, lambda x: x)(row[key]) for key in fn]
    	conn.execute('INSERT INTO "%s" VALUES (%s)' % (name,
    	    ', '.join(['?' for _ in fn])), values)

    return conn
    
def read(url, name, conn, **converters):

    try:
    	csv_reader = csv.DictReader(urllib2.urlopen(url + '.csv'))
    	fn = csv_reader.fieldnames
    except urllib2.HTTPError as err:
    	if err.code == 404:
    		print "\n##HTTP ERROR CODE 404 - Probably STREAM has been CLEARED!"
    		print "##SLEEPING FOR 1 SECOND AND REPEATING!"
    		time.sleep(1)
    		return read(url, name, conn, **converters)
    	if err.code == 502:
    		print "\n##HTTP ERROR CODE 502 - Something went wrong during sending data from Phant WEBSERVER!!"
    		print "##SLEEPING FOR 1 SECOND AND REPEATING!"
    		time.sleep(1)
    		return read(url, name, conn, **converters)
    	if err.code == 503:
    		print "\n##HTTP ERROR CODE 503 - Phant WEBSERVER overloaded!"
    		print "##SLEEPING FOR 1 SECOND AND REPEATING!"
    		time.sleep(1)
    		return read(url, name, conn, **converters)
    	else:
    		raise
           
    conn.execute('DELETE FROM %s' % (name))	
    for row in csv_reader:
    	values = [converters.get(key, lambda x: x)(row[key]) for key in fn]
    	conn.execute('INSERT INTO "%s" VALUES (%s)' % (name,
    	    ', '.join(['?' for _ in fn])), values)

    return conn

# database.py

import pymysql

class DBClass():
    def __init__(self):
        self.db = pymysql.connect(
            user        = 'root',
            passwd      = 'password',
            host        = '127.0.0.1',
            db          = 'backend_test',
            charset     = 'utf8',
            cursorclass = pymysql.cursors.DictCursor
        )
        self.cursor = self.db.cursor()

    def execute(self, query, args):
        self.cursor.execute(query, args)

    def executeAll(self, query, args):
        self.cursor.execute(query, args)
        row = self.cursor.fetchall()
        return row

    def executeOne(self, query, args):
        self.cursor.execute(query, args)
        row = self.cursor.fetchone()
        return row

    def commit(self):
        self.db.commit()
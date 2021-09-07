# database.py

import pymysql

# Mysql Connection을 위한 객체 정의
class DBClass():
    # mysql 계정 정보, 퀴리문 실행 후 리턴값은 dictionary형식으로 반환 
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

    # 쿼리를 수행한 결과를 전부 반환
    def executeAll(self, query, args):
        self.cursor.execute(query, args)
        row = self.cursor.fetchall()
        return row

    # 쿼리를 수행한 결과 중 최상위 row만 반환
    def executeOne(self, query, args):
        self.cursor.execute(query, args)
        row = self.cursor.fetchone()
        return row

    # commit
    def commit(self):
        self.db.commit()

    # 쿼리작성시 바로 commit 
    def execute_and_commit(self, query, args={"a":"a"}):
        try:
            self.cursor.execute(query, args)
        except:
            return 0
        self.db.commit()
        return 1